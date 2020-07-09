package jp.co.acom.riza.event.ita;

import static org.junit.Assert.assertEquals;

import org.hibernate.service.spi.InjectService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import jp.co.acom.riza.cep.CepMonitorService;
import jp.co.acom.riza.event.customer.entity.Customer;
import jp.co.acom.riza.event.service.CustomerService;
import jp.co.acom.riza.event.service.TradeService;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = AppConfig.class)
@AutoConfigureMockMvc
public class EventTest001 {

	@SpyBean
	KafkaTemplate<String,String> template;
	@Autowired
	MockKafkaTemplate  mockTemplate;
	
//	@MockBean(classes = KafkaTemplate.class)
//	KafkaTemplate<String,String> template;
	
	@MockBean
	CepMonitorService cepMonitorService;
	
	@Autowired
	CustomerService customerService;
	
	@Autowired
	TradeService tradeService;


	@Test
	public void test001() throws InterruptedException {
		Customer customer = new Customer();
		customer.setName("name");
		customer.setRank(5);
		customerService.save(customer);
		assertEquals(customer.getName(), "name");
		Thread.sleep(10000);
		customerService.findAll();
		Thread.sleep(10000);
	}
	@Test
	public void test002() throws InterruptedException {
		Customer customer = new Customer();
		customer.setName("name");
		customer.setRank(5);
		customerService.save(customer);
		assertEquals(customer.getName(), "name");
		Thread.sleep(10000);
	}
}
