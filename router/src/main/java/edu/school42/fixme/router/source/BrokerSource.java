package edu.school42.fixme.router.source;

import edu.school42.fixme.common.model.Source;

import java.net.Socket;
import java.util.concurrent.ThreadLocalRandom;

public class BrokerSource extends ASource {

	public BrokerSource(Socket socket) {
		super(socket);
		this.id = ThreadLocalRandom.current().nextInt(1_000_000, 1_000_000_000);
		if (this.id % 2 == 1) {
			++this.id;
		}
		this.type = Source.BROKER;
	}
}
