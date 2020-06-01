package com.example.eventnotify.event;

import java.io.Serializable;
import org.hibernate.EmptyInterceptor;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.type.Type;
import org.springframework.core.annotation.AnnotationUtils;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.ObjectMapper;

/** Hibernate での更新通知を受け、イベント通知を行う. */
public class PersistentEventInterceptor extends EmptyInterceptor {
  private String entityPackage = "";
  private PersistentEventNotifier eventNotifier;
//  @JsonTypeInfo(use = Id.CLASS,include=As.EXTERNAL_PROPERTY)
  //@JsonTypeInfo(use = Id.CLASS)
  Serializable printId;
  
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
      //@JsonTypeInfo(use = Id.CLASS,include=As.EXTERNAL_PROPERTY)
      //@JsonTypeInfo(use = Id.CLASS)
      Serializable id,
      Object[] currentState,
      Object[] previousState,
      String[] propertyNames,
      Type[] types) {
    notifyEvent(entity, id, PersistentEvent.Type.UPDATE);
    System.out.println("*********id=" + id);
    System.out.println("*********entity=" + entity);
    return super.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
  }

  @Override
  public boolean onSave(
      Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
	  
	  printId = id;  
	  ObjectMapper mapper = new ObjectMapper();
	  try {
	  System.out.println("**************jsontype id=" + mapper.writeValueAsString(printId));
	  System.out.println("**************key class=" + id.getClass().toString());
	  } catch (Exception ex) {
		  ex.printStackTrace();
	  }
	    System.out.println("*********id=" + id);
	    System.out.println("*********entity=" + entity);
    notifyEvent(entity, id, PersistentEvent.Type.INSERT);
    return super.onSave(entity, id, state, propertyNames, types);
  }

  @Override
  public void onDelete(
      Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
    notifyEvent(entity, id, PersistentEvent.Type.DELETE);
    super.onDelete(entity, id, state, propertyNames, types);
  }

  private void notifyEvent(Object entity, Serializable id, PersistentEvent.Type type) {
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
