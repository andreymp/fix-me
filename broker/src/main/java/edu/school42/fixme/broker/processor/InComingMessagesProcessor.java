package edu.school42.fixme.broker.processor;

import edu.school42.fixme.broker.exception.BrokerException;
import edu.school42.fixme.broker.service.FixMessagesService;
import edu.school42.fixme.common.model.FixMessageEntity;
import edu.school42.fixme.common.model.Source;
import edu.school42.fixme.common.model.Status;
import edu.school42.fixme.common.util.FixMessageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

@Slf4j
@RequiredArgsConstructor
public class InComingMessagesProcessor implements Runnable {

	private final InputStream inputStream;
	private final FixMessagesService fixMessagesService;

	@Override
	public void run() {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
			String message = br.readLine();
			log.info(message);

			if (!message.contains("35=C|")) {
				FixMessageEntity entity = fixMessagesService.findByBody(message);
				entity.setStatus(Status.COMPLETED);
				fixMessagesService.update(entity);
			}
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			throw new BrokerException(e.getMessage());
		}
	}
}
