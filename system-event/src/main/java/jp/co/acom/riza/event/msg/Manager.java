package jp.co.acom.riza.event.msg;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * エンティティマネージャー単位のエンティティ更新情報
 * @author vagrant
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
	private String manager;
	
    /**
     * エンティティ更新情報リスト
     */
    @JsonTypeInfo(use = Id.CLASS)
//	@JsonDeserialize(contentAs = EntityType.class)
//	@JsonProperty("etype")    
	private List<Entity> entitys;
    
	/**
	 * リビジョン番号
	 */
	@JsonDeserialize(contentAs = Long.class)
	private Long revison;
}
