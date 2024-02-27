package edu.school42.fixme.market.handler.impl;

import edu.school42.fixme.common.converter.FixMessageMapper;
import edu.school42.fixme.common.dto.FixMessageDto;
import edu.school42.fixme.common.options.MarketOptions;
import edu.school42.fixme.market.Market;
import edu.school42.fixme.market.handler.MessageHandler;

public class SellMessageHandler extends MessageHandler {

	private static final int MIN_SELL_QUANTITY = 50;

	public SellMessageHandler(FixMessageMapper mapper) {
		super(mapper);
	}

	@Override
	public String handle(FixMessageDto dto) {
		FixMessageDto responseDto = createHeader(dto);
		if (!instruments.contains(dto.getInstrument())) {
			return mapper.toFixString(getReject(responseDto, "wrong instrument"));
		}
		if (dto.getQuantity() < MIN_SELL_QUANTITY) {
			return mapper.toFixString(getReject(responseDto, "low quantity"));
		}
		if (dto.getMarket() != Market.ID) {
			return mapper.toFixString(getReject(responseDto, "wrong market"));
		}
		if (dto.getPrice() > money) {
			return mapper.toFixString(getReject(responseDto, "high price"));
		}
		quantity += dto.getQuantity();
		money -= dto.getPrice();
		responseDto.setOrdStatus(MarketOptions.EXECUTED);
		responseDto.countBodyLength();
		responseDto.countChecksum();
		return mapper.toFixString(responseDto);
	}
}
