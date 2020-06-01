package jp.co.acom.renove.utils.event;

import java.io.Serializable;
import lombok.Data;

/** データ変更時のイベント情報. */
@Data
public class PersistentEvent {
  /** 変更の種類. */
  public enum Type {
    UPDATE,
    INSERT,
    DELETE
  }

  public PersistentEvent() {}

  /**
   * 変更の種類と対象エンティティを指定してオブジェクトを作成する.
   *
   * @param type 変更の種類
   * @param entity 対象エンティティ
   */
  public PersistentEvent(Type type, Object entity) {
    this.type = type;
    this.entity = entity;
  }

  private Type type;
  private Serializable entityId;
  private Object entity;
  private Integer oldRevision;
  private Integer newRevision;
}
