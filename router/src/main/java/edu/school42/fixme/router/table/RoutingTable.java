package edu.school42.fixme.router.table;

import edu.school42.fixme.router.exception.RouterException;
import edu.school42.fixme.router.source.ASource;
import edu.school42.fixme.router.source.BrokerSource;
import edu.school42.fixme.router.source.MarketSource;

import java.util.ArrayList;
import java.util.Collections;
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

	public MarketSource findMarketSocket(long marketId) {
		return sources.stream()
				.filter(source -> source instanceof MarketSource  && source.getId() == marketId)
				.map(MarketSource.class::cast)
				.findAny()
				.orElseThrow(() -> new RouterException("Unknown Market ID"));
	}
	
	public BrokerSource findBrokerSource(long brokerId) {
		return sources.stream()
				.filter(source -> source instanceof BrokerSource && source.getId() == brokerId)
				.map(BrokerSource.class::cast)
				.findAny()
				.orElseThrow(() -> new RouterException("Unknown Broker ID"));
	}
}
