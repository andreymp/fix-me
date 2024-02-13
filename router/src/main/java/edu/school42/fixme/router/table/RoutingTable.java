package edu.school42.fixme.router.table;

import edu.school42.fixme.common.model.Source;
import edu.school42.fixme.router.source.ASource;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RoutingTable {

	private final List<ASource> sources = new ArrayList<>();

	public void add(ASource source) {
		sources.add(source);
		sources.sort(Comparator.comparingInt(ASource::getId));
	}

	public void remove(ASource source) {
		sources.remove(source);
	}

	public Socket findSocket(long id, Source type) {
		return sources
				.stream()
				.filter(source -> source.getType() == type && source.getId() == id)
				.findFirst()
				.map(ASource::getSocket)
				.orElse(null);
	}
}
