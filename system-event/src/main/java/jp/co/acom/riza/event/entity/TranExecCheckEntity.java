package jp.co.acom.riza.event.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;
@NamedQuery(name = "findTranExecCheckEntity", query = "select u from TranExecCheckEntity u where u.datetime <= :baseTimestamp")
/**
 * トランザクションイベントエンティティ
 *
 * @author vagrant
 *
 */
@Data
@Table(name = "TRANEXECCHECK")
@Entity
public class TranExecCheckEntity {
	/**
	 * イベントキークラス
	 */
	@Id
	private String eventKey;
	/**
	 * イベント挿入日時(yyyyMMddHHmmss)
	 */
	private String datetime;
}
