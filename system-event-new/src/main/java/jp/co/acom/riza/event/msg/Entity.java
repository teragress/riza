package jp.co.acom.riza.event.msg;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import jp.co.acom.riza.event.persist.EntityType;
import jp.co.acom.riza.event.persist.PersistentType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 更新エンティティ情報
 * 
 * @author teratani
 *
 */
@Getter
@Setter
@ToString
public class Entity {

	/**
	 * パーシステントタイプ(挿入/更新/削除)
	 * 
	 */
	@JsonDeserialize(contentAs = PersistentType.class)
	private PersistentType type;
	
	/**
	 * エンティティタイプ(リソース/イベント)
	 */
	@JsonDeserialize(contentAs = EntityType.class)
	@JsonProperty("etype")
	private EntityType entityType;

	/**
	 * エンティティクラス名
	 */
	@JsonDeserialize(contentAs = String.class)
	private String entity;

	/**
	 * キー値
	 */
	@JsonTypeInfo(use = Id.CLASS)
	private Serializable key;
}
