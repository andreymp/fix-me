package edu.school42.fixme.market.socket;

import edu.school42.fixme.common.converter.FixMessageMapper;
import edu.school42.fixme.market.exception.MarketException;
import edu.school42.fixme.market.repository.FixMessagesRepository;
import edu.school42.fixme.market.service.FixMessagesService;
import edu.school42.fixme.market.simulation.MarketSimulation;
import lombok.RequiredArgsConstructor;

import java.net.Socket;

@RequiredArgsConstructor
public class MarketSocket implements Runnable {

	private static final String HOST = "localhost";

	private final int port;

	@Override
	public void run() {
		try {
			Socket socket = new Socket(HOST, port);
			FixMessageMapper fixMessageMapper = new FixMessageMapper();
			FixMessagesService fixMessagesService = new FixMessagesService(new FixMessagesRepository());

			new MarketSimulation(socket, fixMessageMapper, fixMessagesService).start();
		} catch (Exception e) {
			throw new MarketException(e.getMessage());
		}
	}
}
