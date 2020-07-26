package jp.co.acom.riza.event.msg;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import jp.co.acom.riza.event.persist.EntityType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ヘッダーメッセージ
 * @author vagrant
 *
 */
@Getter
@Setter
@ToString
public class Header {

	/**
	 * リクエストID
	 */
	@JsonProperty("reqId")
	@JsonDeserialize(contentAs = String.class)
	private String reqeustId;
	
	/**
	 * ユーザーID
	 */
	@JsonProperty("uid")
	@JsonDeserialize(contentAs = String.class)
	private String userId;
	
	/**
	 * ビジネスプロセス名
	 */
	@JsonProperty("prc")
	@JsonDeserialize(contentAs = String.class)
	private String businessProcess;
}
