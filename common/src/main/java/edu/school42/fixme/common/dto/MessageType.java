package edu.school42.fixme.common.dto;

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
}
