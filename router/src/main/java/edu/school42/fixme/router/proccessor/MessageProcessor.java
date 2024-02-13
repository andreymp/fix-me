package edu.school42.fixme.router.proccessor;

import edu.school42.fixme.common.converter.FixMessageMapper;
import edu.school42.fixme.common.creator.MessageCreator;
import edu.school42.fixme.common.dto.FixMessageDto;
import edu.school42.fixme.common.model.FixMessageEntity;
import edu.school42.fixme.common.model.Source;
import edu.school42.fixme.common.model.Status;
import edu.school42.fixme.router.exception.RouterException;
import edu.school42.fixme.router.service.FixMessagesService;
import edu.school42.fixme.router.source.ASource;
import edu.school42.fixme.router.table.RoutingTable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
public class MessageProcessor implements Runnable {

	private static final int ROUTER_ID = 0;

	private final ASource source;
	private final Socket socket;
	private final RoutingTable routingTable;
	private final FixMessageMapper mapper;
	private final MessageCreator messageCreator;
	private final FixMessagesService fixMessagesService;

	@Override
	public void run() {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
			while (true) {
				String line = br.readLine();
				if (Objects.isNull(line)) {
					break ;
				}
				log.info("received :: {}", line);
				updateStatus(line, Status.RECEIVED_BY_ROUTER);
				FixMessageDto dto = mapper.toDto(line);
				if (validateByChecksum(dto)) {
					forwardMessage(dto);
				} else {
					sendValidationErrorMessage(dto);
				}
			}
		} catch (Exception e) {
			throw new RouterException(e.getMessage());
		} finally {
			routingTable.remove(source);
		}
	}

	private boolean validateByChecksum(FixMessageDto dto) {
		String currChecksum = dto.getChecksum();
		dto.countChecksum();
		String newChecksum = dto.getChecksum();
		return currChecksum.equals(newChecksum);
	}

	private void forwardMessage(FixMessageDto dto) {
		try {
			Socket targetSocket = switch (dto.getType()) {
				case PLACE_ORDER -> routingTable.findSocket(dto.getTargetId(), Source.BROKER);
				case HANDLE_ORDER -> routingTable.findSocket(dto.getTargetId(), Source.MARKET);
				default -> throw new RouterException("unknown message type");
			};
			if (Objects.isNull(targetSocket)) {
				sendValidationErrorMessage(dto);
				return;
			}
			try (PrintWriter pw = new PrintWriter(targetSocket.getOutputStream(), true)) {
				String message = mapper.toFixString(dto);
				pw.println(message);
				log.info("sent :: {}", message);
				updateStatus(message, Status.SENT_TO_DESTINATION);
			}
		} catch (Exception e) {
			throw new RouterException(e.getMessage());
		}
	}

	private void sendValidationErrorMessage(FixMessageDto dto) throws SocketException {
		try (PrintWriter pw = new PrintWriter(socket.getOutputStream(), true)) {
			pw.println(mapper.toFixString(messageCreator.validationErrorMessage(source.getId(), dto.getOrderId(), ROUTER_ID)));
		} catch (IOException e) {
			throw new SocketException(e.getMessage());
		}
	}

	private void updateStatus(String message, Status status) {
		FixMessageEntity entity = fixMessagesService.findByBody(message);
		entity.setStatus(status);
		fixMessagesService.update(entity);
	}
}
