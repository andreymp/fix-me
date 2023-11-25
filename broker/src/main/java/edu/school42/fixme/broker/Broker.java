package edu.school42.fixme.broker;

import edu.school42.fixme.broker.exception.BrokerException;
import edu.school42.fixme.broker.socket.BrokerSocket;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
public class Broker {

	public static final long ID = System.currentTimeMillis();
	private static final int BROKER_PORT = 5000;

	private final Executor executor = Executors.newSingleThreadExecutor();

	public void start() {
		try {
			log.info("broker started with id :: {}", ID);
			executor.execute(new BrokerSocket(BROKER_PORT));
		} catch	(BrokerException e) {
			throw new BrokerException(e.getMessage());
		}
	}
	public static void main(String[] args) {
		log.info("broker started");

		Broker broker = new Broker();
		broker.start();
	}
}