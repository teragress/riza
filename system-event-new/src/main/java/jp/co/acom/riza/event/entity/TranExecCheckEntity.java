package jp.co.acom.riza.event.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

@NamedQuery(name = TranExecCheckEntity.FIND_BY_CLEAN, query = "select u from TranExecCheckEntity u "
		+ "where u.datetime < :baseDatetime")
/**
 * トランザクションイベントエンティティ
 *
 * @author teratani
 *
 */
@Data
@Table(schema = "RP0", name = "TRANEXECCHECK")
@Entity
public class TranExecCheckEntity {

	public static final String FIND_BY_CLEAN = "tranExec.findByClean";
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
