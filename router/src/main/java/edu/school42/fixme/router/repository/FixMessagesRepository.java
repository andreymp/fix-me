package edu.school42.fixme.router.repository;

import edu.school42.fixme.common.model.FixMessageEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.transaction.Transactional;

public final class FixMessagesRepository {

	private final EntityManagerFactory factory;
	private final EntityManager entityManager;

	public FixMessagesRepository() {
		this.factory = Persistence.createEntityManagerFactory("fixMe");
		this.entityManager = factory.createEntityManager();
	}

	@Transactional
	public void update(FixMessageEntity entity) {
		entityManager.merge(entity);
	}

	@Transactional
	public FixMessageEntity findByBody(String body) {
		String query = String.format("""
				SELECT f
				FROM FixMessageEntity f
				WHERE f.body = '%s'
				""", body);
		return (FixMessageEntity) entityManager.createQuery(query)
				.getSingleResult();
	}

	public void	close() {
		entityManager.close();
		factory.close();
	}
}
