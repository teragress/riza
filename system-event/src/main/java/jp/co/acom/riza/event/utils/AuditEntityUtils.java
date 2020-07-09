package jp.co.acom.riza.event.utils;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.exception.NotAuditedException;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQueryCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import jp.co.acom.riza.event.core.EntityType;
import jp.co.acom.riza.event.msg.Entity;
import jp.co.acom.riza.event.msg.EntityEvent;

@Service
public class AuditEntityUtils {
	@Autowired
	ApplicationContext applicationContext;

	public Object getEventAuditEntity(EntityEvent entityEvent) throws ClassNotFoundException {

		EntityManager em = getEntityManager(entityEvent.getManager());
		Entity entity = entityEvent.getEntity();

		if (entity.getEntityType() == EntityType.EVENT) {
			return getEventEntity(em, entity.getEntity(), entity.getKey());
		}

		if (entity.getEntityType() == EntityType.RESOURCE) {
			return getResourceEntity(em, entity.getEntity(), entity.getKey());
		}

		AuditQueryCreator auditQueryCreator = getAuditQueryCreator(entityEvent.getManager());

		return null;
	}

	private Object getEventEntity(EntityManager em, String entityClassName, Serializable key)
			throws ClassNotFoundException {

		return em.find(Class.forName(entityClassName), key);

	}

	private Object getResourceEntity(EntityManager em, String entityClassName, Serializable key, Long revision)
			throws ClassNotFoundException {

		AuditQueryCreator queryCreator = AuditReaderFactory.get(em).createQuery();

		return queryCreator.forEntitiesAtRevision(Class.forName(entityClassName), revision).getSingleResult();

	}


	private Object getResourceBeforeEntity(EntityManager em, String entityClassName, Serializable key, Long revision)
			throws ClassNotFoundException {

		AuditQueryCreator queryCreator = AuditReaderFactory.get(em).createQuery();

		return queryCreator.forEntitiesAtRevision(Class.forName(entityClassName), revision).getSingleResult();

		RevisionsOfEntity(Class.forName(entityClassName).getClass(), revision);

		try {
			return (List<Number>) queryCreator.forRevisionsOfEntity(event.getEntity().getClass(), false)
					.addProjection(AuditEntity.revisionNumber())
					.add(AuditEntity.and(AuditEntity.id().eq(event.getEntityId()),
							AuditEntity.property("version").le(version)))
					.addOrder(AuditEntity.revisionNumber().desc()).setMaxResults(2).getResultList();
		} catch (NotAuditedException e) {
			return null;
		}

		return em.find(Class.forName(entityClassName), key);

	}

	private AuditQueryCreator getAuditQueryCreator(String entityManagerFactoryName) {

		EntityManagerFactory factory = (EntityManagerFactory) applicationContext.getBean(entityManagerFactoryName);
		EntityManager em = factory.createEntityManager();

		return AuditReaderFactory.get(em).createQuery();

	}

	private EntityManager getEntityManager(String entityManagerFactoryName) {

		EntityManagerFactory factory = (EntityManagerFactory) applicationContext.getBean(entityManagerFactoryName);
		return factory.createEntityManager();
	}

}
