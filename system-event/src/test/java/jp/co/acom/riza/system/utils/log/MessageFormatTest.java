package jp.co.acom.riza.system.utils.log;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
//@ExtendWith(SpringExtension.class)
@ComponentScan(basePackages="jp.co.acom.riza.system.utils")
@ContextConfiguration(classes = AppConfig.class)
@EnableConfigurationProperties
@PropertySource("classpath:message.properties")
public class MessageFormatTest {
	
//	@Autowired
//	CustomerService customerService;
	@Autowired
	MessageFormat format;

	@Test
	public void testMessageFormat() {
		assertEquals("RIZA0004I {}の{}が開始しました。",MessageFormat.get("RIZA0004"));
		assertEquals(null,MessageFormat.get("bbbb"));
		assertEquals("TEST0004I {}の{}が開始しました。",MessageFormat.get("TEST0004"));
	}
}
