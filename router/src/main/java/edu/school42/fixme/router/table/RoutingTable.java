package edu.school42.fixme.router.table;

import edu.school42.fixme.common.model.Source;
import edu.school42.fixme.router.source.ASource;
import lombok.extern.slf4j.Slf4j;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public class RoutingTable {

	private final List<ASource> sources = new ArrayList<>();

	public void add(ASource source) {
		sources.add(source);
		sources.sort(Comparator.comparingLong(ASource::getId).reversed());
	}

	public void replaceBrokerSourceId(ASource source, Long newId) {
		sources
				.stream()
				.filter(aSource -> aSource.equals(source))
				.findAny()
				.ifPresent(aSource -> aSource.setId(newId));
	}

	public void remove(ASource source) {
		sources.remove(source);
	}

	public AtomicReference<Socket> findSocket(long id, Source type) {
		return sources
				.stream()
				.filter(source -> source.getType() == type && source.getId() == id)
				.findAny()
				.map(ASource::getSocket)
				.orElse(null);
	}
}
