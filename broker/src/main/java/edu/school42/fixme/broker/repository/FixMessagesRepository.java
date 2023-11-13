package edu.school42.fixme.broker.repository;

import edu.school42.fixme.common.model.FixMessageEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.transaction.Transactional;

public class FixMessagesRepository {

	private final EntityManagerFactory factory;
	private final EntityManager entityManager;

	public FixMessagesRepository() {
		this.factory = Persistence.createEntityManagerFactory("fixMe");
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
	public Long findId(FixMessageEntity entity) {
		String query = String.format("""
    			SELECT f.id
    			FROM FixMessageEntity f
       			WHERE f.source = '%s' AND f.status = '%s'
       			AND f.body = '%s'
				""", entity.getSource(), entity.getStatus(), entity.getBody());
		return (Long) entityManager.createQuery(query)
				.getSingleResult();
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
