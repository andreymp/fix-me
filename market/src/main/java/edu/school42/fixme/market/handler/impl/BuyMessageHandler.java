package edu.school42.fixme.market.handler.impl;

import edu.school42.fixme.common.converter.FixMessageMapper;
import edu.school42.fixme.common.dto.FixMessageDto;
import edu.school42.fixme.common.options.MarketOptions;
import edu.school42.fixme.market.Market;
import edu.school42.fixme.market.handler.MessageHandler;

public class BuyMessageHandler extends MessageHandler {

	private static final double MIN_BUY_PRICE = 1500.0D;

	public BuyMessageHandler(FixMessageMapper mapper) {
		super(mapper);
	}

	@Override
	public String handle(FixMessageDto dto) {
		FixMessageDto responseDto = createHeader(dto);
		if (!instruments.contains(dto.getInstrument())) {
			return mapper.toFixString(getReject(responseDto, "wrong instrument"));
		}
		if (dto.getQuantity() > quantity) {
			return mapper.toFixString(getReject(responseDto, "big quantity"));
		}
		if (dto.getMarket() != Market.ID) {
			return mapper.toFixString(getReject(responseDto, "wrong market"));
		}
		if (dto.getPrice() < MIN_BUY_PRICE) {
			return mapper.toFixString(getReject(responseDto, "low price"));
		}
		quantity -= dto.getQuantity();
		money += dto.getPrice();
		responseDto.setOrdStatus(MarketOptions.EXECUTED);
		return mapper.toFixString(responseDto);
	}
}
