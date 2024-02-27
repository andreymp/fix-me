package edu.school42.fixme.market.socket;

import edu.school42.fixme.common.converter.FixMessageMapper;
import edu.school42.fixme.common.dto.FixMessageDto;
import edu.school42.fixme.common.model.FixMessageEntity;
import edu.school42.fixme.common.model.Source;
import edu.school42.fixme.common.model.Status;
import edu.school42.fixme.market.exception.MarketException;
import edu.school42.fixme.market.handler.impl.BuyMessageHandler;
import edu.school42.fixme.market.handler.impl.SellMessageHandler;
import edu.school42.fixme.market.repository.FixMessagesRepository;
import edu.school42.fixme.market.service.FixMessagesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
public class MarketSocket {

	private static final String HOST = "localhost";

	private final int port;

	private final FixMessageMapper mapper = new FixMessageMapper();
	private final FixMessagesService fixMessagesService = new FixMessagesService(new FixMessagesRepository());

	public void start() {
		try (
				Socket socket = new Socket(HOST, port);
				PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
				BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()))
		) {
			log.info("received :: {}", br.readLine());
			while (true) {
				String line = br.readLine();
				if (Objects.isNull(line)) {
					break ;
				}
				log.info("received :: {}", line);
				updateStatus(line, Status.COMPLETED);

				FixMessageDto dto = mapper.toDto(line);
				String incomingMessage = switch (dto.getSide()) {
					case BUY -> new BuyMessageHandler(mapper).handle(dto);
					case SELL -> new SellMessageHandler(mapper).handle(dto);
				};
				FixMessageEntity entity = new FixMessageEntity();
				entity.setBody(incomingMessage);
				entity.setSource(Source.MARKET);
				entity.setStatus(Status.CREATED);
				fixMessagesService.insert(entity);

				pw.println(incomingMessage);
				log.info("sent :: {}", incomingMessage);
				updateStatus(incomingMessage, Status.SENT_TO_ROUTER);
			}
		} catch (Exception e) {
			log.info(e.getMessage(), e);
			throw new MarketException(e.getMessage());
		}
	}

	private void updateStatus(String message, Status status) {
		FixMessageEntity entity = fixMessagesService.findByBody(message);
		entity.setStatus(status);
		fixMessagesService.update(entity);
	}
}
