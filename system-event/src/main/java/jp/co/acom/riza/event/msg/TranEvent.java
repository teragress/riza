package jp.co.acom.riza.event.msg;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * トランザクションイベントチェックポイント情報
 * @author teratani
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
	 * エンティティマネージャー単位の更新情報リスト
	 */
	@JsonDeserialize(as = ArrayList.class,contentAs = Manager.class)
	@JsonProperty("mgrs")
	private List<Manager> managers = new ArrayList<Manager>();

	/**
	 * KAFKAトピックメッセージリスト
	 */
	@JsonProperty("topics")
	@JsonDeserialize(as = ArrayList.class,contentAs = KafkaTopicMessage.class)
	private List<KafkaTopicMessage> topicMessages = new ArrayList<KafkaTopicMessage>();

	/**
	 * メッセージIDプレフィックス
	 */
	@JsonProperty(value = "mid")
	private byte[] messageIdPrefix;
}
