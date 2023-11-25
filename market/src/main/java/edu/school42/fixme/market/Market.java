package edu.school42.fixme.market;

import edu.school42.fixme.market.exception.MarketException;
import edu.school42.fixme.market.socket.MarketSocket;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Slf4j
public class Market {

	public static final long ID = System.currentTimeMillis();
	private static final int MARKET_PORT = 5001;

	private final Executor executor = Executors.newSingleThreadExecutor();

	public void start() {
		try {
			executor.execute(new MarketSocket(MARKET_PORT));
		} catch	(Exception e) {
			log.error(e.getMessage(), e);
			throw new MarketException(e.getMessage());
		}
	}

	public static void main(String[] args) {
		log.info("market started");

		Market market = new Market();
		market.start();
	}
}