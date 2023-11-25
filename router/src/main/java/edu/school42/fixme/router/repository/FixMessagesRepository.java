package edu.school42.fixme.router.repository;

import edu.school42.fixme.common.model.FixMessageEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.Transactional;

public final class FixMessagesRepository {

	private final EntityManagerFactory factory;
	private final EntityManager entityManager;

	public FixMessagesRepository() {
		this.factory = Persistence.createEntityManagerFactory("fixMeRouter");
		this.entityManager = factory.createEntityManager();
	}

	@Transactional
	public void update(FixMessageEntity entity) {
		entityManager.getTransaction().begin();
		entityManager.merge(entity);
		entityManager.getTransaction().commit();
	}

	@Transactional
	public FixMessageEntity findByBody(String body) {
		String query = String.format("""
				SELECT m
				FROM FixMessageEntity m
				WHERE m.body = '%s'
				""", body);
		entityManager.getTransaction().begin();
		FixMessageEntity entity = entityManager.createQuery(query, FixMessageEntity.class)
				.getSingleResult();
		entityManager.getTransaction().commit();
		return entity;
	}

	public void	close() {
		entityManager.close();
		factory.close();
	}
}
