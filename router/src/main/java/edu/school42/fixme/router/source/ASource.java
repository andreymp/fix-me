package edu.school42.fixme.router.source;

import edu.school42.fixme.common.model.Source;
import lombok.Getter;

import java.net.Socket;

@Getter
public abstract class ASource {
	protected int id;
	protected Source type;

	protected final Socket socket;

	protected ASource(Socket socket) {
		this.socket = socket;
	}
}
