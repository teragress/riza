package jp.co.acom.renove.event.core;

import java.io.Serializable;
import lombok.Data;

/** データ変更時のイベント情報. */
@Data
public class PersistentEvent {
  public PersistentEvent() {}

  /**
   * 変更の種類と対象エンティティを指定してオブジェクトを作成する.
   *
   * @param type 変更の種類
   * @param entity 対象エンティティ
   */
  public PersistentEvent(PersistentType type, Object entity) {
    this.type = type;
    this.entity = entity;
  }

  private PersistentType type;
  private Serializable entityId;
  private Object entity;
  private Integer revision;
}
