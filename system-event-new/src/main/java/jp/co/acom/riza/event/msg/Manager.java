package jp.co.acom.riza.event.msg;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * エンティティマネージャー単位のエンティティ更新情報
 * @author teratani
 *
 */
@Getter
@Setter
@ToString
public class Manager {
	/**
	 * エンティティマネージャーファクトリー名
	 */
	@JsonDeserialize(contentAs = String.class)
	@JsonProperty(value = "mg")
	private String manager;
	
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
	@JsonProperty(value = "rev")
	private Long revison;
}
