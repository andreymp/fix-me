package edu.school42.fixme.broker.service;

import edu.school42.fixme.broker.repository.FixMessagesRepository;
import edu.school42.fixme.common.model.FixMessageEntity;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FixMessagesService {

	private final FixMessagesRepository fixMessagesRepository;

	public void insert(FixMessageEntity entity) {
		fixMessagesRepository.insert(entity);
	}

	public void update(FixMessageEntity entity) {
		fixMessagesRepository.update(entity);
	}

	public Long findId(FixMessageEntity entity) {
		return fixMessagesRepository.findId(entity);
	}

	public FixMessageEntity findByBody(String body) {
		return fixMessagesRepository.findByBody(body);
	}

	public void close() {
		fixMessagesRepository.close();
	}

}
