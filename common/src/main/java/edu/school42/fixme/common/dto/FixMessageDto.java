package edu.school42.fixme.common.dto;

import edu.school42.fixme.common.options.BrokerOptions;
import edu.school42.fixme.common.options.MarketOptions;
import edu.school42.fixme.common.util.FixMessageUtil;
import lombok.Data;

@Data
public class FixMessageDto {
	private int id;
	private int bodyLength;
	private String orderId;
	private MessageType type;
	private Long sendersId;
	private Long targetId;
	private BrokerOptions side;
	private String instrument;
	private Integer quantity;
	private Long market;
	private Double price;
	private MarketOptions ordStatus;
	private String ordRejReason;
	private int sourceId;
	private String checksum;


	public void countChecksum() {
		long checksumAsLong = countSectionChecksum(FixMessageUtil.ID, id)
				+ countSectionChecksum(FixMessageUtil.BODY_LENGTH, bodyLength)
				+ countSectionChecksum(FixMessageUtil.ORDER_ID, orderId)
				+ countSectionChecksum(FixMessageUtil.TYPE, type)
				+ countSectionChecksum(FixMessageUtil.SENDERS_ID, sendersId)
				+ countSectionChecksum(FixMessageUtil.TARGET_ID, targetId);
		if (type == MessageType.PLACE_ORDER) {
			checksumAsLong += countSectionChecksum(FixMessageUtil.SIDE, side)
					+ countSectionChecksum(FixMessageUtil.INSTRUMENT, instrument)
					+ countSectionChecksum(FixMessageUtil.QUANTITY, quantity)
					+ countSectionChecksum(FixMessageUtil.MARKET, market)
					+ countSectionChecksum(FixMessageUtil.PRICE, price);
		}
		if (type == MessageType.HANDLE_ORDER) {
			checksumAsLong += countSectionChecksum(FixMessageUtil.ORD_STATUS, ordStatus);
			if (ordStatus == MarketOptions.REJECTED) {
				checksumAsLong += countSectionChecksum(FixMessageUtil.ORD_REJ_REASON, ordRejReason);
			}
		}
		if (type == MessageType.ID_CONFIRMATION) {
			checksumAsLong += countSectionChecksum(FixMessageUtil.SOURCE_ID, sourceId);
		}
		checksumAsLong %= 256;
		checksum = String.valueOf(checksumAsLong);
		while (checksum.length() < 3) {
			checksum = "0".concat(checksum);
		}
	}

	public void countBodyLength() {
		bodyLength = countSectionBodyLength(FixMessageUtil.ORDER_ID, orderId)
				+ countSectionBodyLength(FixMessageUtil.TYPE, type)
				+ countSectionBodyLength(FixMessageUtil.SENDERS_ID, sendersId)
				+ countSectionBodyLength(FixMessageUtil.TARGET_ID, targetId);
		if (type == MessageType.PLACE_ORDER) {
			bodyLength += countSectionBodyLength(FixMessageUtil.SIDE, side)
					+ countSectionBodyLength(FixMessageUtil.INSTRUMENT, instrument)
					+ countSectionBodyLength(FixMessageUtil.QUANTITY, quantity)
					+ countSectionBodyLength(FixMessageUtil.MARKET, market)
					+ countSectionBodyLength(FixMessageUtil.PRICE, price);
		}
		if (type == MessageType.HANDLE_ORDER) {
			bodyLength += countSectionBodyLength(FixMessageUtil.ORD_STATUS, ordStatus);
			if (ordStatus == MarketOptions.REJECTED) {
				bodyLength += countSectionBodyLength(FixMessageUtil.ORD_REJ_REASON, ordRejReason);
			}
		}
		if (type == MessageType.ID_CONFIRMATION) {
			bodyLength += countSectionBodyLength(FixMessageUtil.SOURCE_ID, sourceId);
		}
	}

	private int countSectionBodyLength(int tag, Object value) {
		return FixMessageUtil.makePair(tag, value).length();
	}

	private long countSectionChecksum(int tag, Object value) {
		long sectionChecksum = 0;
		for(var ch : FixMessageUtil.makePair(tag, value).toCharArray()) {
			sectionChecksum += ch != '|' ? ch : 1;
		}
		return sectionChecksum;
	}
}
