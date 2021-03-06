package jp.co.acom.riza.event.persist;

import java.io.Serializable;

import org.hibernate.EmptyInterceptor;
import org.hibernate.Transaction;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.type.Type;
import org.springframework.core.annotation.AnnotationUtils;

import jp.co.acom.riza.event.msg.AuditEntity;
import jp.co.acom.riza.event.persist.PersistentHolder.AuditStatus;
import jp.co.acom.riza.system.object.annotation.ObjectAnnotation.AuditMessage;
import jp.co.acom.riza.system.utils.log.Logger;
import lombok.Getter;
import lombok.Setter;

/**
 * Hibernate でのエンティマネージャー単位の更新通知を受け、イベント通知を行う
 * 
 * @author teratani
 *
 */
@Getter
@Setter
public class PersistentInterceptor extends EmptyInterceptor {

	private static final long serialVersionUID = 1L;
	
	/**
	 * ロガー
	 */
	private static final Logger logger = Logger.getLogger(PersistentInterceptor.class);
	
	/**
	 * エンティティ格納パッケージ
	 */
	// 特定のパッケージ以下のもので、@RevisionEntity が付与されてい
	private String[] entityPackages;
	
	/**
	 * パーシステントインターセプター
	 */
	private PersistentEventNotifier eventNotifier;

	/**
	 *  
	 */
	private PostCommitPersistentNotifier postNotifier;

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
	 * 参照イベント処理
	 */
	@Override
	public boolean onLoad(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		notifyEvent(entity, id, PersistentType.SELECT);
		return super.onLoad(entity, id, state, propertyNames, types);
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
		if (isAuditObject(entity)) {
			AuditEntity auditEntity = new AuditEntity();
			auditEntity.setType(persistentType);
			auditEntity.setEntity(entity.getClass().getSimpleName());
			auditEntity.setKey(id);
			postNotifier.getAuditMessage().getAuditEntity().add(auditEntity);
		}
		if (isTargetEvent(entity)) {
			if (persistentType != PersistentType.SELECT) {
				EntityPersistent event = new EntityPersistent(persistentType, getEntityType(entity), entity, id);
				event.setEntityId(id);
				eventNotifier.notify(event,postNotifier.getCommonContext().getBusinessProcess());
			}
		}
	}

	/**
	 * リビジョン番号通知処理
	 * 
	 * @param entity
	 * @param idPersistent
	 */
	private void notifyRevision(Object entity, Serializable id) {
		logger.debug("notifyRevision() started. entity=" + entity);
		if (AnnotationUtils.findAnnotation(entity.getClass(), RevisionEntity.class) != null
				&& entity.getClass().getName().endsWith("Revinfo")) {
			eventNotifier.notify(Long.valueOf(id.toString()));
		}
//		if (AnnotationUtils.findAnnotation(entity.getClass(), Audited.class) != null
//				&& postNotifier.getAuditStatus() == AuditStatus.INIT) {
//			postNotifier.setAuditStatus(AuditStatus.AUDIT_ENTITY_ON);
//		}
	}

	/**
	 * エンティティがイベント送信の対象かを取得する.
	 *
	 * @param entity エンティティ
	 * @param id     エンティティのID
	 * @return 送信対象の場合は true、それ以外の場合は false.
	 */
	protected boolean isTargetEvent(Object entity) {
		Class<?> entityClass = entity.getClass();
		// 特定のパッケージ以下のもので、@RevisionEntity が付与されていないものが対象
		// （@RevisionEntity があるクラスは、リビジョンテーブル用）
		boolean targetPackage = false;
		for (String pkg:entityPackages) {
			if (entityClass.getPackage().getName().startsWith(pkg)) {
				targetPackage = true;
			}
		}
		return targetPackage
				&& AnnotationUtils.findAnnotation(entityClass, RevisionEntity.class) == null;
	}
	
	/**
	 * エンティティが監査オブジェクトかを取得する.
	 *
	 * @param entity エンティティ
	 * @param id     エンティティのID
	 * @return 送信対象の場合は true、それ以外の場合は false.
	 */
	protected boolean isAuditObject(Object entity) {
		Class<?> entityClass = entity.getClass();
		return AnnotationUtils.findAnnotation(entityClass, AuditMessage.class) != null;
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

	/**
	 * トランザクション完了前のインターセプター処理
	 */
	@Override
	public void beforeTransactionCompletion(Transaction tx) {
		logger.info("beforeTransactionComplition() started.");
		boolean allReady = true;
		eventNotifier.notify(AuditStatus.COMPLETE);
		
		for (PersistentHolder persistentEventHolder : postNotifier.getHolders()) {
			if (persistentEventHolder.getAuditStatus() != AuditStatus.COMPLETE) {
				allReady = false;
			}
		}
		if (allReady) {
			//MQ判定が必要と思われるここで
			if (postNotifier.isPersistentEvent()) {
				postNotifier.beforeEvent();
				for (PersistentHolder persistentEventHolder : postNotifier.getHolders()) {
					persistentEventHolder.setAuditStatus(AuditStatus.COMPLETE);
				}
			}
		}
		super.beforeTransactionCompletion(tx);
	}
}
