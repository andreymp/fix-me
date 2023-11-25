package edu.school42.fixme.common.dto;

import edu.school42.fixme.common.exception.FixMessageValidationException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MessageType {

	PLACE_ORDER('D'),
	HANDLE_ORDER('8'),
	ID_CONFIRMATION('C'),
	VALIDATION_ERROR('V')
	;

	private final char abbr;

	public static MessageType decode(Character abbr) {
		return switch (abbr) {
			case 'D' -> PLACE_ORDER;
			case '8' -> HANDLE_ORDER;
			case 'C' -> ID_CONFIRMATION;
			case 'V' -> VALIDATION_ERROR;
			default -> throw new FixMessageValidationException("unknown message type");
		};
	}
}
