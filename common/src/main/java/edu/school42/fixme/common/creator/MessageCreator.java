package edu.school42.fixme.common.creator;

import edu.school42.fixme.common.dto.FixMessageDto;
import edu.school42.fixme.common.dto.MessageType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MessageCreator {

	private int msgId = 0;

	private static MessageCreator INSTANCE;

	public static MessageCreator getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new MessageCreator();
		}
		return INSTANCE;
	}

	public FixMessageDto confirmationOfIdMessage(long sourceId, long routerId) {
		FixMessageDto dto = new FixMessageDto();
		dto.setId(++msgId);
		dto.setType(MessageType.ID_CONFIRMATION);
		dto.setOrderId(String.valueOf(routerId));
		dto.setSendersId(routerId);
		dto.setTargetId(sourceId);
		dto.setSourceId((int) sourceId);

		dto.countBodyLength();
		dto.countChecksum();
		return dto;
	}

	public FixMessageDto validationErrorMessage(long sourceId, String orderId, long routerId) {
		FixMessageDto dto = new FixMessageDto();
		dto.setId(++msgId);
		dto.setType(MessageType.VALIDATION_ERROR);
		dto.setOrderId(orderId);
		dto.setSendersId(routerId);
		dto.setTargetId(sourceId);

		dto.countBodyLength();
		dto.countChecksum();
		return dto;
	}
}
