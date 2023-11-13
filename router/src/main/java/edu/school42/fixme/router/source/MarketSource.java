package edu.school42.fixme.router.source;

import edu.school42.fixme.common.model.Source;

import java.net.Socket;
import java.util.concurrent.ThreadLocalRandom;

public class MarketSource extends ASource {

	public MarketSource(Socket socket) {
		super(socket);
		this.id = ThreadLocalRandom.current().nextInt(1_000_000, 1_000_000_000);
		if (this.id % 2 == 0) {
			++this.id;
		}
		this.type = Source.MARKET;
	}
}
