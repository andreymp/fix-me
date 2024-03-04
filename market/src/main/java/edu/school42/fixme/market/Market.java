package edu.school42.fixme.market;

import edu.school42.fixme.market.dto.InstrumentDto;
import edu.school42.fixme.market.exception.MarketException;
import edu.school42.fixme.market.socket.MarketSocket;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
public class Market {

	public static final List<InstrumentDto> INSTRUMENTS = new ArrayList<>();

	public static double MONEY = ThreadLocalRandom.current().nextDouble(1_000_00, Integer.MAX_VALUE);

	private static final int MARKET_PORT = 5001;


	public void start() {
		try {
			new MarketSocket(MARKET_PORT).start();
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