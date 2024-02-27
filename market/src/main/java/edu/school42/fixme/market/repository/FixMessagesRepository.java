package edu.school42.fixme.market.repository;

import edu.school42.fixme.common.model.FixMessageEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.Transactional;

public class FixMessagesRepository {

	private final EntityManagerFactory factory;
	private final EntityManager entityManager;

	public FixMessagesRepository() {
		this.factory = Persistence.createEntityManagerFactory("fixMeMarket");
		this.entityManager = factory.createEntityManager();
	}

	public void insert(FixMessageEntity entity) {
		entityManager.getTransaction().begin();
		entityManager.persist(entity);
		entityManager.getTransaction().commit();
	}

	public void update(FixMessageEntity entity) {
		entityManager.getTransaction().begin();
		entityManager.merge(entity);
		entityManager.getTransaction().commit();
	}

	public FixMessageEntity findByBody(String body) {
		String query = String.format("""
				SELECT f
				FROM FixMessageEntity f
				WHERE f.body = '%s'
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
