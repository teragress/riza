package jp.co.acom.riza.cep.event;

import java.time.LocalDateTime;

import lombok.Data;

/** 開始時のイベント情報. */
@Data
public class RizaCepEventStart {
  /**
   * エントリーKey
   */
  private String entryKeyId;

  /**
   * 処理待ち期限
   */
  private long expireLimit;

  /**
   * イベント日時（開始）
   *
   * <p>null の場合は、イベント受信日時が使用される.
   */
  private LocalDateTime eventDateTime;

}