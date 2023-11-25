package edu.school42.fixme.common.validation;

import edu.school42.fixme.common.converter.FixMessageMapper;
import edu.school42.fixme.common.dto.FixMessageDto;
import edu.school42.fixme.common.exception.FixMessageValidationException;
import edu.school42.fixme.common.util.FixMessageUtil;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public abstract class FixMessageValidator {

	protected static final String SEPARATOR_KV = "=";
	protected static final String SEPARATOR_SECTION = "|";
	protected static final String ERROR_MESSAGE = "Validation error";

	private final FixMessageMapper mapper;


	public abstract FixMessageDto validate(String message) throws FixMessageValidationException;

	protected void validateNecessary(String message) throws FixMessageValidationException {
		if (message.isBlank() && !message.startsWith(String.valueOf(FixMessageUtil.ID).concat(SEPARATOR_KV))) {
			throw new FixMessageValidationException(ERROR_MESSAGE);
		}
	}

	protected void validateTags(String message, List<Integer> tags) throws FixMessageValidationException {
		long count = countTags(message, tags);
		if (count != tags.size()) {
			throw new FixMessageValidationException(ERROR_MESSAGE);
		}
	}

	protected void validateTagsAndSeparators(String message, List<Integer> tags) throws FixMessageValidationException {
		long countTags = countTags(message, tags);
		long countKv = countSeparators(message, FixMessageValidator.SEPARATOR_KV);
		long countSections = countSeparators(message, FixMessageValidator.SEPARATOR_SECTION);

		if (countTags * 2 != countKv + countSections) {
			throw new FixMessageValidationException(ERROR_MESSAGE);
		}
	}

	private long countTags(String message, List<Integer> tags) {
		return tags.stream()
				.filter(tag -> message.contains(SEPARATOR_SECTION.concat(String.valueOf(tag)).concat(SEPARATOR_KV))
						|| message.contains(String.valueOf(tag).concat(SEPARATOR_KV)))
				.count();
	}

	private long countSeparators(String message, String separator) {
		return message.chars().reduce(0, (sub, curr) -> {
			if (curr == separator.charAt(0)) {
				++sub;
			}
			return sub;
		});
	}
}
