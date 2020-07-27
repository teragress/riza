package jp.co.acom.riza.event.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * トランザクションイベントエンティティ
 *
 * @author vagrant
 *
 */
@NamedQueries({
	@NamedQuery(name = EventCheckpointEntity.FIND_BY_DATETIME_FIRST, query = "select u from EventCheckpointEntity u "
		+ "where u.tranEventKey.datetime >= :fromDateTime and u.tranEventKey.datetime < :toDateTime and u.tranEventKey.seq = 0 "
		+ "order by datetime , tranId"),
	@NamedQuery(name = EventCheckpointEntity.FIND_BY_DATETIME_NEXT, query = "select u from EventCheckpointEntity u "
		+ "where concat(u.tranEventKey.datetime, u.tranEventKey.tranId) > :fromDateTimeTranId and u.tranEventKey.datetime < :toDateTime "
		+ "and u.tranEventKey.seq = 0 "
		+ "order by datetime , tranId"),
	@NamedQuery(name = EventCheckpointEntity.FIND_BY_CLEAN, query = "select u from EventCheckpointEntity u "
		+ "where u.tranEventKey.datetime < :dateTime")})

@Data
//@Table(schema = "DP0", name = "EVENTCHECKPOINT")
@Table(name = "EVENTCHECKPOINT")
@Entity
public class EventCheckpointEntity {
	public static final String FIND_BY_DATETIME_FIRST = "eventCheckpoint.findByDatetimeFirst";
	public static final String FIND_BY_DATETIME_NEXT = "eventCheckpoint.findByDatetimeNext";
	public static final String FIND_BY_CLEAN = "eventCheckpoint.findByClean";
	
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
