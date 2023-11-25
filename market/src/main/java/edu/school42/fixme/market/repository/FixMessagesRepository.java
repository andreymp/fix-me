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

	@Transactional
	public void insert(FixMessageEntity entity) {
		entityManager.persist(entity);
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
