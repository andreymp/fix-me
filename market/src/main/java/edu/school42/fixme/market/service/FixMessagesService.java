package edu.school42.fixme.market.service;

import edu.school42.fixme.common.model.FixMessageEntity;
import edu.school42.fixme.market.repository.FixMessagesRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FixMessagesService {

	private final FixMessagesRepository fixMessagesRepository;

	public void update(FixMessageEntity entity) {
		fixMessagesRepository.update(entity);
	}

	public FixMessageEntity findByBody(String body) {
		return fixMessagesRepository.findByBody(body);
	}

	public void close() {
		fixMessagesRepository.close();
	}
}
