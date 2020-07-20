package jp.co.acom.riza.event.entity;

import javax.persistence.Column;
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
//@Table(schema = "DP0", name = "EVENTCHECKPOINT")
@Table(name = "EVENTCHECKPOINT")
@Entity
public class EventCheckpointEntity {
	/**
	 * イベントキークラス
	 */
	@EmbeddedId
	private EventCheckpointEntityKey tranEventKey;
	/**
	 * 分割数
	 */
	private Integer cnt;
	/**
	 * イベントメッセージ
	 */
	@Column(length = 32000)
	private String eventMsg;

}
