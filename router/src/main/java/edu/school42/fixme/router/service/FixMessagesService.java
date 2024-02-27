package edu.school42.fixme.router.service;

import edu.school42.fixme.common.model.FixMessageEntity;
import edu.school42.fixme.router.repository.FixMessagesRepository;
import lombok.RequiredArgsConstructor;

import javax.transaction.Transactional;

@RequiredArgsConstructor
public class FixMessagesService {

	private final FixMessagesRepository fixMessageRepository;

	public FixMessageEntity findByBody(String body) {
		return fixMessageRepository.findByBody(body);
	}

	public void update(FixMessageEntity entity) {
		fixMessageRepository.update(entity);
	}

	public void close() {
		fixMessageRepository.close();
	}
}
