package jp.co.acom.renove.utils.event;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.exception.NotAuditedException;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQueryCreator;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.InvalidPropertyException;
import org.springframework.beans.PropertyAccessorFactory;

/** 送信するイベントを保持する. */
public class PersistentEventHolder implements PersistentEventNotifier {
  private List<PersistentEvent> events = new ArrayList<>();
  private EntityManager entityManager;

  public PersistentEventHolder(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  public void notify(PersistentEvent event) {
    events.add(event);
  }

  /**
   * 保持しているイベントを取得する.
   *
   * @return イベントのリスト
   */
  public List<PersistentEvent> getEvents() {
    List<PersistentEvent> normalized = getNormalizedEvents();
    setEventRevisions(normalized);
    return normalized;
  }

  /**
   * 指定したイベント情報の変更前、変更後リビジョンを設定する.
   *
   * @param eventList イベントのリスト
   */
  private void setEventRevisions(List<PersistentEvent> eventList) {
    AuditQueryCreator queryCreator = AuditReaderFactory.get(entityManager).createQuery();
    for (PersistentEvent event : eventList) {
      List<Number> result = getRevisions(queryCreator, event);
      if (result == null) {
        continue;
      }
      if (event.getType() == PersistentEvent.Type.INSERT) {
        event.setNewRevision(result.get(0).intValue());
      } else if (event.getType() == PersistentEvent.Type.UPDATE) {
        event.setNewRevision(result.get(0).intValue());
        event.setOldRevision(result.get(1).intValue());
      } else if (event.getType() == PersistentEvent.Type.DELETE) {
        event.setOldRevision(result.get(0).intValue());
      }
    }
  }

  /**
   * トランザクション内で発生したイベントを正規化する.
   *
   * <p>１つのトランザクション内で同じデータに対する変更が複数あるものを１つのイベントに集約する.
   *
   * @return 正規化したイベントの一覧
   */
  private List<PersistentEvent> getNormalizedEvents() {
    Map<Serializable, List<PersistentEvent>> eventMap = new HashMap<>();
    events.forEach(
        it -> eventMap.computeIfAbsent(it.getEntityId(), key -> new ArrayList<>()).add(it));

    List<PersistentEvent> normalized = new ArrayList<>();
    for (List<PersistentEvent> sameIdEvents : eventMap.values()) {
      PersistentEvent first = sameIdEvents.get(0);
      PersistentEvent last = sameIdEvents.get(sameIdEvents.size() - 1);

      if (first.getType() == PersistentEvent.Type.INSERT) {
        if (last.getType() != PersistentEvent.Type.DELETE) {
          last.setType(PersistentEvent.Type.INSERT);
          normalized.add(last);
        }
      } else {
        if (last.getType() == PersistentEvent.Type.INSERT) {
          last.setType(PersistentEvent.Type.UPDATE);
        }
        normalized.add(last);
      }
    }
    return normalized;
  }

  /**
   * 指定したイベント情報が保持する Entity に関連するリビジョン番号を、最新と１つ前のものを取得する.
   *
   * @param queryCreator Audit用のクエリビルダ
   * @param event イベント情報
   * @return リビジョン番号のリスト. 降順で最大 2件. イベントが保持する Entity が対象外の場合は null.
   */
  @SuppressWarnings("unchecked")
  private List<Number> getRevisions(AuditQueryCreator queryCreator, PersistentEvent event) {
    BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(event.getEntity());

    Object version;
    try {
      version = wrapper.getPropertyValue("version");
    } catch (InvalidPropertyException e) {
      return null;
    }
    if (version == null) {
      return null;
    }
    try {
      return (List<Number>)
          queryCreator
              .forRevisionsOfEntity(event.getEntity().getClass(), false)
              .addProjection(AuditEntity.revisionNumber())
              .add(
                  AuditEntity.and(
                      AuditEntity.id().eq(event.getEntityId()),
                      AuditEntity.property("version").le(version)))
              .addOrder(AuditEntity.revisionNumber().desc())
              .setMaxResults(2)
              .getResultList();
    } catch (NotAuditedException e) {
      return null;
    }
  }
}
