package edu.school42.fixme.market.handler;

import edu.school42.fixme.common.converter.FixMessageMapper;
import edu.school42.fixme.common.dto.FixMessageDto;
import edu.school42.fixme.common.dto.MessageType;
import edu.school42.fixme.common.options.MarketOptions;
import edu.school42.fixme.market.Market;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
public abstract class MessageHandler {

	protected final List<String> instruments = List.of("SRR", "OAS", "CAS", "IP", "SE");

	protected int quantity = ThreadLocalRandom.current().nextInt(100, 1_000);
	protected double money = ThreadLocalRandom.current().nextDouble(1_000.0D, 1_000_000.0D);

	protected final FixMessageMapper mapper;

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
