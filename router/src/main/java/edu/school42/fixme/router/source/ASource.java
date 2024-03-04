package edu.school42.fixme.router.source;

import edu.school42.fixme.common.model.Source;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.net.Socket;
import java.util.concurrent.atomic.AtomicReference;

@Getter
@EqualsAndHashCode
@ToString
public abstract class ASource {

	@Setter
	protected long id;
	protected Source type;

	protected final AtomicReference<Socket> socket;

	protected ASource(AtomicReference<Socket> socket) {
		this.socket = socket;
	}
}
