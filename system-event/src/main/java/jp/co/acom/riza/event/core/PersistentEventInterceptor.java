package jp.co.acom.riza.event.core;

import java.io.Serializable;

import org.hibernate.EmptyInterceptor;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.type.Type;
import org.springframework.core.annotation.AnnotationUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Hibernate でのエンティマネージャー単位の更新通知を受け、イベント通知を行う
 * @author vagrant
 *
 */
@Getter
@Setter
@ToString
public class PersistentEventInterceptor extends EmptyInterceptor {
	/**
	* エンティティ格納パッケージ
	*/
	private String entityPackage = "";
	/**
	 *　イベント通知
	 */
	private PersistentEventNotifier eventNotifier;

	/**
	* 更新イベント処理
	*/
	@Override
	public boolean onFlushDirty(
			Object entity,
			Serializable id,
			Object[] currentState,
			Object[] previousState,
			String[] propertyNames,
			Type[] types) {
		notifyEvent(entity, id, PersistentType.UPDATE);
		return super.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
	}

	/**
	*　挿入イベント処理
	*/
	@Override
	public boolean onSave(
			Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		notifyRevision(entity, id);
		notifyEvent(entity, id, PersistentType.INSERT);
		return super.onSave(entity, id, state, propertyNames, types);
	}

	/**
	*　削除イベント処理
	*/
	@Override
	public void onDelete(
			Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		notifyEvent(entity, id, PersistentType.DELETE);
		super.onDelete(entity, id, state, propertyNames, types);
	}

	/**
	* 通知処理
	* @param entity エンティティ
	* @param id エンティティのID
	* @param type 更新タイプ
	*/
	private void notifyEvent(Object entity, Serializable id, PersistentType persistentType) {
		if (isTargetEvent(entity, id)) {
			PersistentEvent event = new PersistentEvent(persistentType, getEntityType(entity), entity, id);
			event.setEntityId(id);
			eventNotifier.notify(event);
		}
	}
	/**
	 * リビジョン番号通知処理
	 * @param entity
	 * @param id
	 */
	private void notifyRevision(Object entity, Serializable id) {
		if (AnnotationUtils.findAnnotation(entity.getClass(),RevisionEntity.class) != null &
				entity.getClass().getName().endsWith("Revinfo")) {
			eventNotifier.notify(Long.valueOf(id.toString()));
		}
	}

	/**
	 * エンティティがイベント送信の対象かを取得する.
	 *
	 * @param entity エンティティ
	 * @param id エンティティのID
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
}