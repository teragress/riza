package old.jp.co.acom.riza.cep.event;

import java.util.Date;
import lombok.Data;

/** 終了時のイベント情報. */
@Data
public class RizaCepEventFinish {
  /**
   * エントリーKey
   */
  private String entryKeyId;

  /**
   * イベント日時（終了）
   *
   * <p>null の場合は、イベント受信日時が使用される.
   */
  private Date eventDate;

}