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

	private static final int BROKER_PORT = 5000;
	private static final int MARKET_PORT = 5001;

	private final ExecutorService executor = Executors.newFixedThreadPool(2);

	public void start() {
		try {
			RoutingTable table = new RoutingTable();
			FixMessageMapper mapper = new FixMessageMapper();
			MessageCreator messageCreator = MessageCreator.getInstance();

			executor.submit(new RouterSocket(BROKER_PORT, table, mapper, messageCreator));
			executor.submit(new RouterSocket(MARKET_PORT, table, mapper, messageCreator));

			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch	(Exception e) {
			throw new RouterException("Executor failed");
		}
	}
	public static void main(String[] args) {
		log.info("router started!");

		Router router = new Router();
		router.start();
	}
}