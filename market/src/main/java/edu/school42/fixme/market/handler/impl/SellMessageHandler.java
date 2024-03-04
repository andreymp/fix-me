package edu.school42.fixme.market.handler.impl;

import edu.school42.fixme.common.converter.FixMessageMapper;
import edu.school42.fixme.common.dto.FixMessageDto;
import edu.school42.fixme.common.options.MarketOptions;
import edu.school42.fixme.market.Market;
import edu.school42.fixme.market.dto.InstrumentDto;
import edu.school42.fixme.market.handler.MessageHandler;

import java.util.List;
import java.util.Objects;

public class SellMessageHandler extends MessageHandler {

	public SellMessageHandler(FixMessageMapper mapper) {
		super(mapper);
	}

	public String handle(FixMessageDto fixMessageDto) {
		FixMessageDto responseDto = createHeader(fixMessageDto);
		if (Market.MONEY - fixMessageDto.getPrice() * fixMessageDto.getQuantity() < 0) {
			return mapper.toFixString(getReject(responseDto, "market cannot afford this instrument"));
		}
		Market.MONEY -= fixMessageDto.getPrice() * fixMessageDto.getQuantity();

		int instrumentIdx = Market.INSTRUMENTS.indexOf(new InstrumentDto().setInstrument(fixMessageDto.getInstrument()));
		if (instrumentIdx == -1) {
			var instrumentDto = new InstrumentDto();
			instrumentDto.setInstrument(fixMessageDto.getInstrument());
			instrumentDto.setQuantity(fixMessageDto.getQuantity());
			Market.INSTRUMENTS.add(instrumentDto);
		} else {
			Market.INSTRUMENTS.get(instrumentIdx)
					.setQuantity(Market.INSTRUMENTS.get(instrumentIdx).getQuantity() + fixMessageDto.getQuantity());
		}
		responseDto.setOrdStatus(MarketOptions.EXECUTED);
		responseDto.countBodyLength();
		responseDto.countChecksum();
		return mapper.toFixString(responseDto);
	}
}
