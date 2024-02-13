package edu.school42.fixme.router;

import edu.school42.fixme.common.converter.FixMessageMapper;
import edu.school42.fixme.common.creator.MessageCreator;
import edu.school42.fixme.router.exception.RouterException;
import edu.school42.fixme.router.repository.FixMessagesRepository;
import edu.school42.fixme.router.service.FixMessagesService;
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

	private final ExecutorService executor = Executors.newFixedThreadPool(1_000);

	public void start() {
		try {
			RoutingTable table = new RoutingTable();
			FixMessageMapper mapper = new FixMessageMapper();
			MessageCreator messageCreator = MessageCreator.getInstance();
			FixMessagesService fixMessagesService = new FixMessagesService(new FixMessagesRepository());
			log.info("Router connected to database");

			executor.submit(new RouterSocket(BROKER_PORT, table, mapper, messageCreator, fixMessagesService, executor));
			executor.submit(new RouterSocket(MARKET_PORT, table, mapper, messageCreator, fixMessagesService, executor));

			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch	(Exception e) {
			log.error(e.getMessage(), e);
			throw new RouterException(e.getMessage());
		}
	}
	public static void main(String[] args) {
		log.info("router started!");

		Router router = new Router();
		router.start();
	}
}