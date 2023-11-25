package edu.school42.fixme.broker.socket;

import edu.school42.fixme.broker.exception.BrokerException;
import edu.school42.fixme.broker.repository.FixMessagesRepository;
import edu.school42.fixme.broker.service.FixMessagesService;
import edu.school42.fixme.broker.simulation.BrokerSimulation;
import edu.school42.fixme.broker.validator.BrokerFixValidator;
import edu.school42.fixme.common.converter.FixMessageMapper;
import edu.school42.fixme.common.validation.FixMessageValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.net.Socket;

@Slf4j
@RequiredArgsConstructor
public class BrokerSocket implements Runnable {

	private static final String HOST = "localhost";

	private final int port;

	@Override
	public void run() throws BrokerException {
		try {
			Socket socket = new Socket(HOST, port);
			FixMessageMapper fixMessageMapper = new FixMessageMapper();
			FixMessageValidator fixMessageValidator = new BrokerFixValidator(fixMessageMapper);
			FixMessagesService fixMessagesService = new FixMessagesService(new FixMessagesRepository());
			log.info("Broker connected to database");

			new BrokerSimulation(socket, fixMessageValidator, fixMessageMapper, fixMessagesService).start();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new BrokerException(e.getMessage());
		}
	}
}
