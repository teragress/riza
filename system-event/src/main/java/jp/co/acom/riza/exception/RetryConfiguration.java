package jp.co.acom.riza.exception;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import jp.co.acom.riza.system.utils.log.Logger;

@Component
public class RetryConfiguration extends RouteBuilder {
	/**
	 * ロガー
	 */
	private static Logger logger = Logger.getLogger(RetryConfiguration.class);

	@Autowired
	Environment env;
	/**
	 * ルート例外リトライの定義
	 */
	@Override
	public void configure() throws Exception {
		logger.debug("configure() start");
		onException(RetryableException.class)
		.maximumRedeliveries(10).delayPattern("0:1000;5:3000")
		.handled(true);

	}
}
