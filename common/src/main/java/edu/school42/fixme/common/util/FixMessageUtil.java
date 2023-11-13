package edu.school42.fixme.common.util;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@UtilityClass
public class FixMessageUtil {
	public final int ID = 8;
	public final int BODY_LENGTH = 9;
	public final int ORDER_ID = 11;
	public final int TYPE = 35;
	public final int SENDERS_ID = 49;
	public final int TARGET_ID = 56;
	public final int SIDE = 54;
	public final int INSTRUMENT = 55;
	public final int QUANTITY = 53;
	public final int MARKET = 262;
	public final int PRICE = 44;
	public final int ORD_STATUS = 39;
	public final int ORD_REJ_REASON = 103;
	public final int SOURCE_ID = 42;
	public final int CHECKSUM = 10;

	public final List<Integer> BROKER_TAGS = new ArrayList<>(List.of(SIDE, INSTRUMENT, QUANTITY, MARKET, PRICE));
	public final List<Integer> MARKET_TAGS = new ArrayList<>(List.of(ORD_STATUS, ORD_REJ_REASON));

	public String makePair(int tag, Object value) {
		return String.valueOf(tag)
				.concat("=")
				.concat(String.valueOf(value))
				.concat("|");
	}
}
