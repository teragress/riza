package jp.co.acom.riza.event.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

/**
 * トランザクションイベントエンティティ
 *
 * @author vagrant
 *
 */
@Data
@Table(schema = "VAGRANT", name = "TRANEVENT")
@Entity
public class TranEventEntity {
	/**
	 * イベントキークラス
	 */
	@EmbeddedId
	private TranEventEntityKey tranEventKey;
	/**
	 * 分割数
	 */
	private Integer cnt;
	/**
	 * イベントメッセージ
	 */
	private String eventMsg;

}
