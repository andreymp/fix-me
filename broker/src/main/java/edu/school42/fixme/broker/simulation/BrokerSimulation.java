package edu.school42.fixme.broker.simulation;

import edu.school42.fixme.broker.exception.BrokerException;
import edu.school42.fixme.broker.processor.InComingMessagesProcessor;
import edu.school42.fixme.broker.service.FixMessagesService;
import edu.school42.fixme.common.converter.FixMessageMapper;
import edu.school42.fixme.common.dto.FixMessageDto;
import edu.school42.fixme.common.exception.FixMessageValidationException;
import edu.school42.fixme.common.model.FixMessageEntity;
import edu.school42.fixme.common.model.Source;
import edu.school42.fixme.common.model.Status;
import edu.school42.fixme.common.validation.FixMessageValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;

@Slf4j
@RequiredArgsConstructor
public class BrokerSimulation {

	private final Socket socket;
	private final FixMessageValidator validator;
	private final FixMessageMapper mapper;
	private final FixMessagesService fixMessagesService;
	private final InComingMessagesProcessor inComingMessagesProcessor;

	public void start() {
		log.info("""
				Broker ready to read fix-messages

				FIX: tag=value|
				TAGS:
						1. 54 -> side: BUY or SELL
						2. 55 -> instrument
						3. 53 -> quantity
						4. 262 -> market destination ID
						5. 44 -> price
				""");
		try (
				PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
				BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()))
		) {
			log.info("received :: {}", br.readLine());
			Scanner sc = new Scanner(System.in);
			while (sc.hasNext()) {
				String message = sc.nextLine();
				FixMessageEntity entity = createMessageEntity(message);
				String incomingMessage = mapper.toFixString(validator.validate(message));

				updateStatus(entity, incomingMessage);
				pw.println(incomingMessage);
				log.info("sent :: {}", incomingMessage);
				updateStatus(entity);
				handleResponse(br);
			}
		} catch (FixMessageValidationException e) {
			log.error(e.getMessage(), e);
		} catch (IOException e) {
			fixMessagesService.close();
			log.error(e.getMessage(), e);
			throw new BrokerException(e.getMessage());
		} finally {
			fixMessagesService.close();
		}
	}

	private void handleResponse(BufferedReader br) throws IOException {
		String message = br.readLine();
		if (Objects.nonNull(message)) {
			log.info("received :: {}", message);

			if (!message.contains("35=C|") && !message.contains("35=V|")) {
				FixMessageEntity entity = fixMessagesService.findByBody(message);
				entity.setStatus(Status.COMPLETED);
				fixMessagesService.update(entity);
			};
		}
	}

	private void updateStatus(FixMessageEntity entity, String message) {
		entity.setStatus(Status.VALIDATED);
		entity.setBody(message);
		fixMessagesService.update(entity);
	}

	private void updateStatus(FixMessageEntity entity) {
		entity.setStatus(Status.SENT_TO_ROUTER);
		fixMessagesService.update(entity);
	}

	private FixMessageEntity createMessageEntity(String message) {
		FixMessageEntity entity = new FixMessageEntity();
		entity.setSource(Source.BROKER);
		entity.setStatus(Status.CREATED);
		entity.setBody(message);
		fixMessagesService.insert(entity);
		entity.setId(fixMessagesService.findId(entity));
		return entity;
	}
}
