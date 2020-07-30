package jp.co.acom.riza.event.utils;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQueryCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import jp.co.acom.riza.event.msg.DomainEvent;
import jp.co.acom.riza.event.msg.Entity;
import jp.co.acom.riza.event.msg.EntityEvent;
import jp.co.acom.riza.event.persist.EntityType;

/**
 * 監査（履歴）エンティティの取得ユーティリティ
 * 
 * @author teratani
 *
 */
@Service
public class AuditEntityUtils {
	/**
	 * アプリケーションコンテキスト
	 */
	@Autowired
	ApplicationContext applicationContext;

	/**
	 * リソースエンティティの場合、入力されたカレントのリビジョン番号とクラス情報をもとに履歴エンティティを検索する。<br>
	 * イベントエンティティの場合は、入力されたキーとクラス情報わもとにイベントエンティティを検索する。
	 * 
	 * @param entityEvent
	 * @return エンティティオブジェクト。検索できない場合はnull値。
	 * @throws ClassNotFoundException
	 */
	public Object getEventAuditEntity(EntityEvent entityEvent) throws ClassNotFoundException {

		EntityManager em = (EntityManager) applicationContext.getBean(entityEvent.getManager());
		Entity entity = entityEvent.getEntity();

		if (entity.getEntityType() == EntityType.EVENT) {
			return em.find(Class.forName(entity.getEntity()), entity.getKey());
		} else {
			AuditQueryCreator queryCreator = AuditReaderFactory.get(em).createQuery();

			return queryCreator.forRevisionsOfEntity(Class.forName(entity.getEntity()), true, true)
					.add(AuditEntity.id().eq(entity.getKey()))
					.add(AuditEntity.revisionNumber().eq(entityEvent.getRevision())).getSingleResult();
		}
	}

	/**
	 * リソースエンティティの指定されたリビジョンの一世代前の履歴をリストで返す。<br>
	 * 返却するエンティティリストの最初はカレントで2番目は一世代前を返す。<br>
	 * 1世代前が存在しない場合は、リスト件数が1件となる。検索にヒットしなかった場合はリスト件数がゼロとなる。
	 * 
	 * @param entityEvent
	 * @return リソースエンティティリスト
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public List<Object> getResourceCurrentAndBeforeEntity(EntityEvent entityEvent) throws ClassNotFoundException {
		EntityManager em = (EntityManager) applicationContext.getBean(entityEvent.getManager());
		Entity entity = entityEvent.getEntity();

		AuditQueryCreator queryCreator = AuditReaderFactory.get(em).createQuery();

		return queryCreator.forRevisionsOfEntity(Class.forName(entity.getEntity()), true, true)
				.add(AuditEntity.id().eq(entity.getKey()))
				.add(AuditEntity.revisionNumber().le(entityEvent.getRevision()))
				.addOrder(AuditEntity.revisionNumber().desc()).setMaxResults(2).getResultList();
	}

	/**
	 * ドメインイベントオブジェクトから指定されたエンティティのクラス名と一致するイベントエンティティのリストを返す
	 * 
	 * @param domainEvent   ドメインイベントオブジェクト
	 * @param findClassName サーチするクラス名
	 * @return イベントエンティティリスト
	 * @throws ClassNotFoundException
	 */
	public List<Entity> getEntitys(DomainEvent domainEvent, String findClassName) throws ClassNotFoundException {
		List<Entity> list = new ArrayList<Entity>();
		for (Entity entity : domainEvent.getEntitys()) {
			if (findClassName.equals(entity.getEntity())) {
				list.add(entity);
			}
		}
		return list;
	}

	/**
	 * ドメインイベントオブジェクトから指定されたエンティティのクラス名と一致するイベントエンティティを返す
	 * 
	 * @param domainEvent   ドメインイベントオブジェクト
	 * @param findClassName サーチするクラス名
	 * @return イベントエンティティリスト
	 * @throws ClassNotFoundException
	 */
	public Entity getEntity(DomainEvent domainEvent, String findClassName) throws ClassNotFoundException {
		for (Entity entity : domainEvent.getEntitys()) {
			if (findClassName.equals(entity.getEntity())) {
				return entity;
			}
		}
		return null;
	}

}
