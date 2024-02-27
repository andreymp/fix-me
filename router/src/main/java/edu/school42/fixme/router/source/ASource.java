package edu.school42.fixme.router.source;

import edu.school42.fixme.common.model.Source;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.net.Socket;

@Getter
@EqualsAndHashCode
@ToString
public abstract class ASource {
	protected long id;
	protected Source type;

	protected final Socket socket;

	protected ASource(Socket socket) {
		this.socket = socket;
	}
}
