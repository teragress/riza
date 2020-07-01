package jp.co.acom.riza.utils.log;

import java.util.Map;
import javax.annotation.PostConstruct;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(ignoreResourceNotFound = true, value = "classpath:message.properties")
@ConfigurationProperties(prefix = "msg")
public class MessageFormat {

	private Map<String,String> text;

	public void setText(Map<String, String> text) {
		this.text = text;
	}
	private static Map<String,String> sText;
	
	@PostConstruct
	public void initialize() {
		sText = text;
	}
	
	public static String get(String key) {
		return sText.get(key);
	}
}
