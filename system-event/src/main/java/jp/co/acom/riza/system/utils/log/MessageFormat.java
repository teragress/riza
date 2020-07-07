package jp.co.acom.riza.system.utils.log;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;

import javax.annotation.PostConstruct;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

@Component
public class MessageFormat {
	private static Logger logger = Logger.getLogger(MessageFormat.class);
	
	private static Properties messageProperties = new Properties();

	@PostConstruct
	public void initialize() throws IOException {
		logger.info("initialize() started.");
		ResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
		Collection<Resource> list = Arrays.asList(patternResolver.getResources("classpath*:/*message.properties"));
		for (Resource resource:list) {
			logger.info("resource file =" + resource);
			messageProperties.load(new FileInputStream(resource.getFile()));
		}
	}
	
	public static String get(String key) {
		return messageProperties.getProperty(key);
	}
}
