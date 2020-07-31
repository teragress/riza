package jp.co.acom.riza.event.msg;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ドメインイベントメッセージ
 * @author teratani
 *
 */
@Getter
@Setter
@ToString
public class DomainEvent {

	/**
	 * ヘッダー
	 */
	@JsonDeserialize(contentAs = Header.class)
	private Header header;
	
	/**
	 * エンティティマネージャーr名
	 */
	@JsonDeserialize(contentAs = String.class)
	private String manager;
	
	/**
	 * ドメイン名
	 */
	@JsonDeserialize(contentAs = String.class)
	private String domain;
	
    /**
     * エンティティ更新情報リスト
     */
	@JsonDeserialize(as = ArrayList.class, contentAs = Entity.class)
	@JsonProperty(value = "ets")
	private List<Entity> entitys;
	
	/**
	 * リビジョン番号
	 */
	@JsonDeserialize(contentAs = Long.class)
	private Long revision;
}
