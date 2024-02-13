package edu.school42.fixme.broker.processor;

import edu.school42.fixme.broker.service.FixMessagesService;
import edu.school42.fixme.common.model.FixMessageEntity;
import edu.school42.fixme.common.model.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
public class InComingMessagesProcessor {

	private final Socket socket;
	private final FixMessagesService fixMessagesService;

	public void process() throws IOException {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
			while (true) {
				String message = br.readLine();
				if (Objects.isNull(message)) {
					break ;
				}
				log.info("received :: {}", message);

				if (!message.contains("35=C|") && !message.contains("35=V|")) {
					FixMessageEntity entity = fixMessagesService.findByBody(message);
					entity.setStatus(Status.COMPLETED);
					fixMessagesService.update(entity);
				}
			}
		}
	}
}
