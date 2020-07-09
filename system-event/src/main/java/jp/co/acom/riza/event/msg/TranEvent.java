package jp.co.acom.riza.event.msg;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * トランザクションイベントチェックポイント情報
 * @author vagrant
 *
 */
@Getter
@Setter
@ToString
public class TranEvent {
	/**
	 * ヘッダー
	 */
	@JsonDeserialize(contentAs = Header.class)
	private Header header;
	
	/**
	 * ビジネスプロセスID
	 */
	@JsonDeserialize(contentAs = String.class)
	@JsonProperty("prc")
	private String businessProcess;
	
	/**
	 * エンティティマネージャー単位の更新情報リスト
	 */
	@JsonTypeInfo(use = Id.CLASS)
	private List<Manager> managers;

	/**
	 * これ何....
	 * @param entityClassName
	 * @return
	 */
//	public List<Entity> getEntityPersistence(String entityClassName) {
//		List<Entity> list = new ArrayList<Entity>();
//		for (Manager emp : managers) {
//			for (Entity per : emp.getEntitys()) {
//				if (entityClassName.equals(per.getEntity())) {
//					list.add(per);
//				}
//			}
//		}
//		return list;
//	}
}
