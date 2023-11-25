package edu.school42.fixme.common.converter;

import edu.school42.fixme.common.dto.FixMessageDto;
import edu.school42.fixme.common.dto.MessageType;
import edu.school42.fixme.common.options.BrokerOptions;
import edu.school42.fixme.common.options.MarketOptions;
import edu.school42.fixme.common.util.FixMessageUtil;

public class FixMessageMapper {

	public String toFixString(FixMessageDto dto) {
		StringBuilder sb = new StringBuilder(FixMessageUtil.makePair(FixMessageUtil.ID, dto.getId()))
				.append(FixMessageUtil.makePair(FixMessageUtil.BODY_LENGTH, dto.getBodyLength()))
				.append(FixMessageUtil.makePair(FixMessageUtil.ORDER_ID, dto.getOrderId()))
				.append(FixMessageUtil.makePair(FixMessageUtil.TYPE, dto.getType().getAbbr()))
				.append(FixMessageUtil.makePair(FixMessageUtil.SENDERS_ID, dto.getSendersId()))
				.append(FixMessageUtil.makePair(FixMessageUtil.TARGET_ID, dto.getTargetId()));
		if (dto.getType() == MessageType.PLACE_ORDER) {
				sb.append(FixMessageUtil.makePair(FixMessageUtil.SIDE, dto.getSide()))
					.append(FixMessageUtil.makePair(FixMessageUtil.INSTRUMENT, dto.getInstrument()))
					.append(FixMessageUtil.makePair(FixMessageUtil.QUANTITY, dto.getQuantity()))
					.append(FixMessageUtil.makePair(FixMessageUtil.MARKET, dto.getMarket()))
					.append(FixMessageUtil.makePair(FixMessageUtil.PRICE, dto.getPrice()));
		}
		if (dto.getType() == MessageType.HANDLE_ORDER) {
			sb.append(FixMessageUtil.makePair(FixMessageUtil.ORD_STATUS, dto.getOrdStatus()));
			if (dto.getOrdStatus() == MarketOptions.REJECTED) {
				sb.append(FixMessageUtil.makePair(FixMessageUtil.ORD_REJ_REASON, dto.getOrdRejReason()));
			}
		}
		if (dto.getType() == MessageType.ID_CONFIRMATION) {
			sb.append(FixMessageUtil.makePair(FixMessageUtil.SOURCE_ID, dto.getSourceId()));
		}
		sb.append(FixMessageUtil.makePair(FixMessageUtil.CHECKSUM, dto.getChecksum()));
		return sb.toString();
	}

	public FixMessageDto toDto(String fixString) {
		FixMessageDto dto = new FixMessageDto();
		for (String section : fixString.split("\\|")) {
			if (section.isEmpty()) {
				continue ;
			}
			String[] splitSection = section.split("=");
			if (Integer.parseInt(splitSection[0]) == FixMessageUtil.ID) {
				dto.setId(Integer.parseInt(splitSection[1]));
			}
			if (Integer.parseInt(splitSection[0]) == FixMessageUtil.BODY_LENGTH) {
				dto.setBodyLength(Integer.parseInt(splitSection[1]));
			}
			if (Integer.parseInt(splitSection[0]) == FixMessageUtil.ORDER_ID) {
				dto.setOrderId(splitSection[1]);
			}
			if (Integer.parseInt(splitSection[0]) == FixMessageUtil.TYPE) {
				dto.setType(MessageType.decode(splitSection[1].charAt(0)));
			}
			if (Integer.parseInt(splitSection[0]) == FixMessageUtil.SENDERS_ID) {
				dto.setSendersId(Long.parseLong(splitSection[1]));
			}
			if (Integer.parseInt(splitSection[0]) == FixMessageUtil.TARGET_ID) {
				dto.setTargetId(Long.parseLong(splitSection[1]));
			}
			if (Integer.parseInt(splitSection[0]) == FixMessageUtil.CHECKSUM) {
				dto.setChecksum(splitSection[1]);
			}
			if (dto.getType() == MessageType.PLACE_ORDER) {
				if (Integer.parseInt(splitSection[0]) == FixMessageUtil.SIDE) {
					dto.setSide(BrokerOptions.valueOf(splitSection[1]));
				}
				if (Integer.parseInt(splitSection[0]) == FixMessageUtil.INSTRUMENT) {
					dto.setInstrument(splitSection[1]);
				}
				if (Integer.parseInt(splitSection[0]) == FixMessageUtil.QUANTITY) {
					dto.setQuantity(Integer.parseInt(splitSection[1]));
				}
				if (Integer.parseInt(splitSection[0]) == FixMessageUtil.MARKET) {
					dto.setMarket(Long.parseLong(splitSection[1]));
				}
				if (Integer.parseInt(splitSection[0]) == FixMessageUtil.PRICE) {
					dto.setPrice(Double.parseDouble(splitSection[1]));
				}
			}
			if (dto.getType() == MessageType.HANDLE_ORDER) {
				if (Integer.parseInt(splitSection[0]) == FixMessageUtil.ORD_STATUS) {
					dto.setOrdStatus(MarketOptions.valueOf(splitSection[1]));
				}
				if (dto.getOrdStatus() == MarketOptions.REJECTED &&  Integer.parseInt(splitSection[0]) == FixMessageUtil.ORD_REJ_REASON) {
					dto.setOrdRejReason(splitSection[1]);
				}
			}
			if (dto.getType() == MessageType.ID_CONFIRMATION && (Integer.parseInt(splitSection[0]) == FixMessageUtil.SOURCE_ID)) {
				dto.setSourceId(Integer.parseInt(splitSection[1]));
			}
		}
		return dto;
	}
}
