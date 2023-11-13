package edu.school42.fixme.broker.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MarketSimulationProperties {
	private List<String> instruments = new ArrayList<>();
	private int quantity;
	private int market;
	private double price;
}
