package jp.co.acom.riza.utils.log;

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
@ComponentScan(basePackages="jp.co.acom.riza.utils")
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
		assertEquals(null,MessageFormat.get("aaaaa"));
//		Customer customer = new Customer();
//		customer.setName("name");
//		customer.setRank(5);
//		customerService.save(customer);
//		assertEquals(customer.getName(), "name");
	}
	@Test
	public void test002() {
		assertEquals(null,MessageFormat.get("bbbb"));
	}
}
