package edu.school42.fixme.market.handler.impl;

import edu.school42.fixme.common.converter.FixMessageMapper;
import edu.school42.fixme.common.dto.FixMessageDto;
import edu.school42.fixme.common.options.MarketOptions;
import edu.school42.fixme.market.Market;
import edu.school42.fixme.market.dto.InstrumentDto;
import edu.school42.fixme.market.handler.MessageHandler;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class BuyMessageHandler extends MessageHandler {

	public BuyMessageHandler(FixMessageMapper mapper) {
		super(mapper);
	}

	public String handle(FixMessageDto fixMessageDto) {
		FixMessageDto responseDto = createHeader(fixMessageDto);
		int instrumentIdx = Market.INSTRUMENTS.indexOf(new InstrumentDto().setInstrument(fixMessageDto.getInstrument()));
		if (instrumentIdx == -1) {
			return mapper.toFixString(getReject(responseDto, "instrument not found"));
		}
		int quantity = Market.INSTRUMENTS.get(instrumentIdx).getQuantity() - fixMessageDto.getQuantity();
		if (quantity < 0) {
			return mapper.toFixString(getReject(responseDto, "not enough quantity"));
		}
		Market.INSTRUMENTS.get(instrumentIdx).setQuantity(quantity);
		Market.MONEY += fixMessageDto.getPrice() * quantity;

		responseDto.setOrdStatus(MarketOptions.EXECUTED);
		responseDto.countBodyLength();
		responseDto.countChecksum();
		return mapper.toFixString(responseDto);
	}
}
