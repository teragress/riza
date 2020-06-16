package jp.co.acom.riza.event.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * トランザクションイベントエンティティ
 *
 * @author vagrant
 *
 */
@Getter
@Setter
@ToString
@Table(schema = "VAGRANT", name = "TRANEVENT")
@Entity
public class TranEventEntity {
	/**
	 * イベントキークラス
	 */
	@EmbeddedId
	private TranEventEntityKey tranEventKey;
	/**
	 * イベントメッセージ
	 */
	private String eventMsg;

}
