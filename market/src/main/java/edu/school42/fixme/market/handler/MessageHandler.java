package edu.school42.fixme.market.handler;

import edu.school42.fixme.common.converter.FixMessageMapper;
import edu.school42.fixme.common.dto.FixMessageDto;
import edu.school42.fixme.common.dto.MessageType;
import edu.school42.fixme.common.options.MarketOptions;
import edu.school42.fixme.market.Market;
import edu.school42.fixme.market.dto.InstrumentDto;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
public abstract class MessageHandler {
	protected final FixMessageMapper mapper;

	protected List<InstrumentDto> instruments = new ArrayList<>();
	protected double money = 0;

	public abstract String handle(FixMessageDto dto);

	protected FixMessageDto createHeader(FixMessageDto dto) {
		FixMessageDto header = new FixMessageDto();
		header.setId(ThreadLocalRandom.current().nextInt(Integer.MIN_VALUE, 0));
		header.setOrderId(dto.getOrderId());
		header.setType(MessageType.HANDLE_ORDER);
		header.setSendersId(Market.ID);
		header.setTargetId(dto.getSendersId());
		return header;
	}

	protected FixMessageDto getReject(FixMessageDto responseDto, String message) {
		responseDto.setOrdStatus(MarketOptions.REJECTED);
		responseDto.setOrdRejReason(message);
		responseDto.countBodyLength();
		responseDto.countChecksum();
		return responseDto;
	}
}
