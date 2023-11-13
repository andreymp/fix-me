package edu.school42.fixme.market;

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
		executor.execute(new MarketSocket(MARKET_PORT));
	}

	public static void main(String[] args) {
		log.info("market started");

		Market market = new Market();
		market.start();
	}
}