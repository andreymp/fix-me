package edu.school42.fixme.router;

import edu.school42.fixme.common.converter.FixMessageMapper;
import edu.school42.fixme.common.creator.MessageCreator;
import edu.school42.fixme.router.exception.RouterException;
import edu.school42.fixme.router.socket.RouterSocket;
import edu.school42.fixme.router.table.RoutingTable;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("ResultOfMethodCallIgnored")
@Slf4j
public class Router {
	public static final RoutingTable ROUTING_TABLE = new RoutingTable();
	public static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(100);

	private static final int BROKER_PORT = 5000;
	private static final int MARKET_PORT = 5001;


	public void start() {
		try {
			FixMessageMapper mapper = new FixMessageMapper();
			MessageCreator messageCreator = MessageCreator.getInstance();
			log.info("Router connected to database");

			EXECUTOR_SERVICE.submit(new RouterSocket(BROKER_PORT, mapper, messageCreator));
			EXECUTOR_SERVICE.submit(new RouterSocket(MARKET_PORT, mapper, messageCreator));

			EXECUTOR_SERVICE.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch	(Exception e) {
			throw new RouterException(e.getMessage());
		}
	}
	public static void main(String[] args) {
		log.info("router started!");

		Router router = new Router();
		router.start();
	}
}