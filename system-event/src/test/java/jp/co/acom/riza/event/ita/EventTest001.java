package jp.co.acom.riza.event.ita;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import jp.co.acom.riza.cep.CepMonitorService;
import jp.co.acom.riza.even.service.CustomerService;
import jp.co.acom.riza.even.service.TradeService;
import jp.co.acom.riza.event.customer.entity.Customer;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = AppConfig.class)
@EnableConfigurationProperties
@AutoConfigureMockMvc
public class EventTest001 {

	@MockBean
	KafkaTemplate<String, String> template;
	
	@MockBean
	CepMonitorService cepMonitorService;
	
	@Autowired
	CustomerService customerService;
	
	@Autowired
	TradeService tradeService;


	@Test
	public void test001() {
		Customer customer = new Customer();
		customer.setName("name");
		customer.setRank(5);
		customerService.save(customer);
		assertEquals(customer.getName(), "name");
	}
	@Test
	public void test002() {
		Customer customer = new Customer();
		customer.setName("name");
		customer.setRank(5);
		customerService.save(customer);
		assertEquals(customer.getName(), "name");
	}
}
