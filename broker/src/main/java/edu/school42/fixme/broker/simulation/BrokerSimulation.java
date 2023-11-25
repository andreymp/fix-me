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

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Slf4j
@RequiredArgsConstructor
public class BrokerSimulation {

	private final Socket socket;
	private final FixMessageValidator validator;
	private final FixMessageMapper mapper;
	private final FixMessagesService fixMessagesService;

	private final Executor executor = Executors.newSingleThreadExecutor();

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

		try {
			executor.execute(new InComingMessagesProcessor(socket.getInputStream(), fixMessagesService));
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			throw new BrokerException(e.getMessage());
		}
		try (PrintWriter pw = new PrintWriter(socket.getOutputStream(), true)) {
			Scanner sc = new Scanner(System.in);
			while (sc.hasNext()) {
				String message = sc.nextLine();
				FixMessageEntity entity = createMessageEntity(message);
				FixMessageDto dto = validator.validate(message);
				String incomingMessage = mapper.toFixString(dto);

				updateStatus(entity, Status.VALIDATED, incomingMessage);
				pw.println(incomingMessage);
				log.info("sent message :: {}", incomingMessage);
				updateStatus(entity, Status.SENT_TO_ROUTER);
			}
		}  catch (FixMessageValidationException e) {
			log.error(e.getMessage(), e);
		} catch (IOException e) {
			fixMessagesService.close();
			log.error(e.getMessage(), e);
			throw new BrokerException(e.getMessage());
		}
		fixMessagesService.close();
	}

	private void updateStatus(FixMessageEntity entity, Status status, String message) {
		entity.setStatus(status);
		entity.setBody(message);
		fixMessagesService.update(entity);
	}

	private void updateStatus(FixMessageEntity entity, Status status) {
		entity.setStatus(status);
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
