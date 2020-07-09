package jp.co.acom.riza.event.utils;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.hibernate.envers.AuditReaderFactory;
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
			return em.find(Class.forName(entity.getEntity()), entity.getKey());
		} else {
			AuditQueryCreator queryCreator = AuditReaderFactory.get(em).createQuery();

			return queryCreator.forRevisionsOfEntity(Class.forName(entity.getEntity()), false, true)
					.add(AuditEntity.id().eq(entity.getKey()))
					.add(AuditEntity.revisionNumber().eq(entityEvent.getRevision())).getSingleResult();
		}
	}

	@SuppressWarnings("unchecked")
	public List<Object> getResourceCurrentAndBeforeEntity(EntityEvent entityEvent) throws ClassNotFoundException {
		EntityManager em = getEntityManager(entityEvent.getManager());
		Entity entity = entityEvent.getEntity();

		AuditQueryCreator queryCreator = AuditReaderFactory.get(em).createQuery();

		return queryCreator.forRevisionsOfEntity(Class.forName(entity.getEntity()), false, true)
				.add(AuditEntity.id().eq(entity.getKey()))
				.add(AuditEntity.revisionNumber().le(entityEvent.getRevision()))
				.addOrder(AuditEntity.revisionNumber().desc()).setMaxResults(2).getResultList();
	}

	private EntityManager getEntityManager(String entityManagerFactoryName) {

		EntityManagerFactory factory = (EntityManagerFactory) applicationContext.getBean(entityManagerFactoryName);
		return factory.createEntityManager();
	}

}
