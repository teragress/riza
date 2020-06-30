package jp.co.acom.riza.event.entity;

import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

/**
 * トランザクションイベントエンティティ
 *
 * @author vagrant
 *
 */
@Data
@Table(name = "TRANECHECK")
@Entity
public class TranCheckEntity {
	/**
	 * イベントキークラス
	 */
	@Id
	private String eventKey;
	/**
	 * イベント挿入日時
	 */
	private Timestamp datetime;
}
