package jp.co.acom.riza.event.kafka;

import org.apache.camel.builder.RouteBuilder;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author vagrant
 *
 */
@Getter
@Setter
@ToString
public class KafkaRouteBuilder extends RouteBuilder {

	/**
	 * route定義のid
	 */
	private String id;
	/**
	 *　
	 */
	private String componentName;
	/**
	 * トピックリスト
	 */
	private String topicList[];

	/**
	 *
	 */
	@Override
	public void configure() throws Exception {

		from(componentName + ":" + String.join(",",topicList))
		.id(id)
		.to("dynamicBusinessExecuteProcess")
		.to("manualCommitProcess");

	}
}