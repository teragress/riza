package jp.co.acom.riza.event.core;

import java.io.Serializable;

import org.hibernate.EmptyInterceptor;
import org.hibernate.Transaction;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.type.Type;
import org.springframework.core.annotation.AnnotationUtils;

import jp.co.acom.riza.event.core.PostCommitPersistentEventNotifier.AuditStatus;
import jp.co.acom.riza.utils.log.Logger;
import lombok.Getter;
import lombok.Setter;

/**
 * Hibernate でのエンティマネージャー単位の更新通知を受け、イベント通知を行う
 * 
 * @author vagrant
 *
 */
@Getter
@Setter
public class PersistentEventInterceptor extends EmptyInterceptor {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(PersistentEventInterceptor.class);
	/**
	 * エンティティ格納パッケージ
	 */
	private String entityPackage = "";
	/**
	 * イベント通知
	 */
	private PersistentEventNotifier eventNotifier;

	private PostCommitPersistentEventNotifier postNotifier;

	/**
	 * 更新イベント処理
	 */
	@Override
	public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState,
			String[] propertyNames, Type[] types) {
		logger.debug("onflushDirecty() started. entity=" + entity);
		notifyEvent(entity, id, PersistentType.UPDATE);
		return super.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
	}

	/**
	 * 挿入イベント処理
	 */
	@Override
	public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		logger.debug("onSave() started. entity=" + entity);
		notifyRevision(entity, id);
		notifyEvent(entity, id, PersistentType.INSERT);
		return super.onSave(entity, id, state, propertyNames, types);
	}

	/**
	 * 削除イベント処理
	 */
	@Override
	public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		logger.debug("onDelete() started. entity=" + entity);
		notifyEvent(entity, id, PersistentType.DELETE);
		super.onDelete(entity, id, state, propertyNames, types);
	}

	/**
	 * 通知処理
	 * 
	 * @param entity エンティティ
	 * @param id     エンティティのID
	 * @param type   更新タイプ
	 */
	private void notifyEvent(Object entity, Serializable id, PersistentType persistentType) {
		logger.debug("notifyEvent() started. entity=" + entity);
		if (isTargetEvent(entity, id)) {
			PersistentEvent event = new PersistentEvent(persistentType, getEntityType(entity), entity, id);
			event.setEntityId(id);
			eventNotifier.notify(event);
		}
	}

	/**
	 * リビジョン番号通知処理
	 * 
	 * @param entity
	 * @param id
	 */
	private void notifyRevision(Object entity, Serializable id) {
		logger.debug("notifyRevision() started. entity=" + entity);
		if (AnnotationUtils.findAnnotation(entity.getClass(), RevisionEntity.class) != null
				&& entity.getClass().getName().endsWith("Revinfo")) {
			if (postNotifier.getAuditStatus() == AuditStatus.AUDIT_ENTITY_ON) {
				postNotifier.setAuditStatus(AuditStatus.AUDIT_ENTITY_WRITE);
			}
			eventNotifier.notify(Long.valueOf(id.toString()));
		}
		if (AnnotationUtils.findAnnotation(entity.getClass(), Audited.class) != null
				&& postNotifier.getAuditStatus() == AuditStatus.INIT) {
			postNotifier.setAuditStatus(AuditStatus.AUDIT_ENTITY_ON);
		}
	}

	/**
	 * エンティティがイベント送信の対象かを取得する.
	 *
	 * @param entity エンティティ
	 * @param id     エンティティのID
	 * @return 送信対象の場合は true、それ以外の場合は false.
	 */
	protected boolean isTargetEvent(Object entity, Serializable id) {
		Class<?> entityClass = entity.getClass();
		// 特定のパッケージ以下のもので、@RevisionEntity が付与されていないものが対象
		// （@RevisionEntity があるクラスは、リビジョンテーブル用）
		return entityClass.getPackage().getName().startsWith(entityPackage)
				&& AnnotationUtils.findAnnotation(entityClass, RevisionEntity.class) == null;
	}

	/**
	 * @param entity
	 * @return エンティティの種類
	 */
	protected EntityType getEntityType(Object entity) {
		if (AnnotationUtils.findAnnotation(entity.getClass(), Audited.class) == null) {
			return EntityType.EVENT;
		} else {
			return EntityType.RESOURCE;
		}
	}

	@Override
	public void beforeTransactionCompletion(Transaction tx) {
		logger.info("**********************************beforeTransactionComplition() started.*******************");
		;
		logger.info("AuditStatus=" + postNotifier.getAuditStatus());
		;
		if (postNotifier.getAuditStatus() == AuditStatus.AUDIT_ENTITY_WRITE) {
			postNotifier.beforeEvent();
			postNotifier.setAuditStatus(AuditStatus.COMPLETE);
		}
		super.beforeTransactionCompletion(tx);
	}
}
