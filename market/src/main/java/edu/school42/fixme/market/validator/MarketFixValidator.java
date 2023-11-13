package edu.school42.fixme.market.validator;

import edu.school42.fixme.common.converter.FixMessageMapper;
import edu.school42.fixme.common.dto.FixMessageDto;
import edu.school42.fixme.common.exception.FixMessageValidationException;
import edu.school42.fixme.common.validation.FixMessageValidator;

public class MarketFixValidator extends FixMessageValidator {

	public MarketFixValidator(FixMessageMapper mapper) {
		super(mapper);
	}

	@Override
	public FixMessageDto validate(String message) throws FixMessageValidationException {
		return null;
	}
}
