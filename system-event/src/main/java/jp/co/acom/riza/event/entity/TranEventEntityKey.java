package jp.co.acom.riza.event.entity;

import java.util.Date;

import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * トランザクションイベントテーブルキー
 * @author vagrant
 *
 */
@Getter
@Setter
@ToString
@Embeddable
public class TranEventEntityKey {
	/**
	 * 日時
	 */
	private Date date;
	/**
	 * トランザクション識別
	 */
	private String tranId;


}
