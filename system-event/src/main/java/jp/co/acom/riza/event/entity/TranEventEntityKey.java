package jp.co.acom.riza.event.entity;

import java.io.Serializable;
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
public class TranEventEntityKey implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 日時
	 */
	private Date date;
	/**
	 * トランザクション識別
	 */
	private String tranId;


}
