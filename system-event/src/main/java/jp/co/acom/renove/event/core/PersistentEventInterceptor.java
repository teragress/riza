package jp.co.acom.renove.event.core;

import java.io.Serializable;
import org.hibernate.EmptyInterceptor;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.type.Type;
import org.springframework.core.annotation.AnnotationUtils;

/** Hibernate での更新通知を受け、イベント通知を行う. */
public class PersistentEventInterceptor extends EmptyInterceptor {
  private String entityPackage = "";
  private PersistentEventNotifier eventNotifier;

  public PersistentEventNotifier getEventNotifier() {
    return eventNotifier;
  }

  public void setEventNotifier(PersistentEventNotifier eventNotifier) {
    this.eventNotifier = eventNotifier;
  }

  public String getEntityPackage() {
    return entityPackage;
  }

  public void setEntityPackage(String entityPackage) {
    this.entityPackage = entityPackage;
  }

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

  @Override
  public boolean onSave(
      Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
    notifyEvent(entity, id, PersistentType.INSERT);
    return super.onSave(entity, id, state, propertyNames, types);
  }

  @Override
  public void onDelete(
      Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
    notifyEvent(entity, id, PersistentType.DELETE);
    super.onDelete(entity, id, state, propertyNames, types);
  }

  private void notifyEvent(Object entity, Serializable id, PersistentType type) {
    if (isTargetEvent(entity, id)) {
      PersistentEvent event = new PersistentEvent(type, entity);
      event.setEntityId(id);
      eventNotifier.notify(event);
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
}
