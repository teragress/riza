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
	@JsonProperty("hed")
	private Header header;
	
	/**
	 * ビジネスプロセスID
	 */
	@JsonDeserialize(contentAs = String.class)
	@JsonProperty("prc")
	private String businessProcess;
	
	/**
	 * MQPUTメッセージ数
	 */
	@JsonDeserialize(contentAs = Integer.class)
	@JsonProperty("mq")
	private Integer mqCount;
	
	/**
	 * エンティティマネージャー単位の更新情報リスト
	 */
	@JsonDeserialize(as = ArrayList.class,contentAs = Manager.class)
	@JsonProperty("mgrs")
	private List<Manager> managers;

	/**
	 * KAFKAトピックメッセージリスト
	 */
	@JsonProperty("topics")
	@JsonDeserialize(as = ArrayList.class,contentAs = KafkaTopics.class)
	private List<KafkaTopics> topicMessages;

	/**
	 * メッセージIDプレフィックス
	 */
	@JsonProperty(value = "mid")
	private byte[] messageIdPrefix;
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
