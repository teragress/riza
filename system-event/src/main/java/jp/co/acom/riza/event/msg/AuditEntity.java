package jp.co.acom.riza.event.msg;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import jp.co.acom.riza.event.persist.PersistentType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 監査エンティティ情報
 * 
 * @author teratani
 *
 */
@Getter
@Setter
@ToString
public class AuditEntity {

	/**
	 * パーシステントタイプ(参照/挿入/更新/削除)
	 * 
	 */
	@JsonDeserialize(contentAs = PersistentType.class)
	private PersistentType type;

	/**
	 * エンティティクラス名
	 */
	private String entity;

	/**
	 * キー値
	 */
	private Serializable key;
}
