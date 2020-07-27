package jp.co.acom.riza.event.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;
import lombok.Data;
/**
 * トランザクションイベントテーブルキー
 * @author teratani
 *
 */
@Data
@Embeddable
public class EventCheckpointEntityKey implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 日時(YYYYMMDDHHMMSS)
	 */
	private String datetime;
	/**
	 * トランザクション識別
	 */
	private String tranId;
	/**
	 * シーケンス番号
	 */
	private int seq;


}
