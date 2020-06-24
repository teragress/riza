package jp.co.acom.riza.event.entity;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Embeddable;
import lombok.Data;
/**
 * トランザクションイベントテーブルキー
 * @author vagrant
 *
 */
@Data
@Embeddable
public class TranEventEntityKey implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 日時
	 */
	private Timestamp datetime;
	/**
	 * トランザクション識別
	 */
	private String tranId;


}
