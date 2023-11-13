package edu.school42.fixme.broker;

import edu.school42.fixme.broker.socket.BrokerSocket;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Slf4j
public class Broker {

	public static final long ID = System.currentTimeMillis();
	private static final int BROKER_PORT = 5000;

	private final Executor executor = Executors.newSingleThreadExecutor();

	public void start() {
		executor.execute(new BrokerSocket(BROKER_PORT));
	}
	public static void main(String[] args) {
		log.info("broker started");

		Broker broker = new Broker();
		broker.start();
	}
}