package edu.school42.fixme.broker.repository;

import edu.school42.fixme.common.model.FixMessageEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class FixMessagesRepository {

	private final EntityManagerFactory factory;
	private final EntityManager entityManager;

	public FixMessagesRepository() {
		this.factory = Persistence.createEntityManagerFactory("fixMeBroker");
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

	public Long findId(FixMessageEntity entity) {
		String query = String.format("""
    			SELECT f.id
    			FROM FixMessageEntity f
       			WHERE f.source = '%s' AND f.status = '%s'
       			AND f.body = '%s'
				""", entity.getSource(), entity.getStatus(), entity.getBody());
		entityManager.getTransaction().begin();
		Long id = (Long) entityManager.createQuery(query)
				.getSingleResult();
		entityManager.getTransaction().commit();
		return id;
	}

	public FixMessageEntity findByBody(String body) {
		String query = String.format("""
				SELECT f
				FROM FixMessageEntity f
				WHERE f.body = '%s'
				""", body);
		entityManager.getTransaction().begin();
		FixMessageEntity entity = (FixMessageEntity) entityManager.createQuery(query)
				.getSingleResult();
		entityManager.getTransaction().commit();
		return entity;
	}

	public void	close() {
		entityManager.close();
		factory.close();
	}

}
