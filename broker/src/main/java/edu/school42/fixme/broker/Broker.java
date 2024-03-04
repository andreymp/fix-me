package edu.school42.fixme.broker;

import edu.school42.fixme.broker.exception.BrokerException;
import edu.school42.fixme.broker.socket.BrokerSocket;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Broker {

	public static final long ID = System.currentTimeMillis();
	private static final int BROKER_PORT = 5000;


	public void start() {
		try {
			new BrokerSocket(BROKER_PORT).start();
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