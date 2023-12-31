package edu.school42.fixme.market.simulation;

import edu.school42.fixme.common.converter.FixMessageMapper;
import edu.school42.fixme.common.dto.FixMessageDto;
import edu.school42.fixme.common.model.FixMessageEntity;
import edu.school42.fixme.common.model.Status;
import edu.school42.fixme.market.exception.MarketException;
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
public class MarketSimulation {

	private final Socket socket;
	private final FixMessageMapper mapper;
	private final FixMessagesService fixMessagesService;

	public void start() {
		try (
				PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
				BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()))
		) {
			while (true) {
				String line = br.readLine();
				if (Objects.isNull(line)) {
					break ;
				}
				updateStatus(line, Status.COMPLETED);

				FixMessageDto dto = mapper.toDto(line);
				switch (dto.getSide()) {
					case BUY -> ;
					case SELL -> ;
				}
			}
		} catch (Exception e) {
			throw new MarketException(e.getMessage());
		}
	}

	private void updateStatus(String message, Status status) {
		FixMessageEntity entity = fixMessagesService.findByBody(message);
		entity.setStatus(status);
		fixMessagesService.update(entity);
	}

	private
}
