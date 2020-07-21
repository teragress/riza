package jp.co.acom.riza.event.ita;

import static org.junit.Assert.assertEquals;

import java.util.Properties;

import org.apache.camel.impl.DefaultProducerTemplate;
import org.apache.kafka.common.message.ProduceRequestData;
import org.h2.command.dml.MergeUsing.When;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import jp.co.acom.riza.cep.CepMonitorService;
import jp.co.acom.riza.event.customer.entity.Customer;
import jp.co.acom.riza.event.kafka.KafkaEventProducer;
import jp.co.acom.riza.event.service.CustomerService;
import jp.co.acom.riza.event.service.TradeService;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = AppConfig.class)
@AutoConfigureMockMvc
public class EventTest001 {

	@Autowired
	private MockMvc mockMmvc;
	
// @InjectMocks
// KafkaTemplate<String,String> template = (KafkaTemplate<String, String>) new jp.co.acom.riza.event.command.KafkaTemplate<String, String>(null));
// KafkaTemplate<String,String> template = new KafkaTemplate<String, String>(new DefaultKafkaProducerFactory(new Properties()));
// KafkaTemplate<String,String> template;

//	
//	@SpyBean
//	KafkaEventProducer eventProducer;
	
//	@Captor
//	ArgumentCaptor<String> topicCaptor;
//	
//	@Captor
//	ArgumentCaptor<String> msgCaptor;
//	
//	@SpyBean
//	KafkaEventProducer eventProducer;
	
//	@Autowired
//	MockKafkaTemplate  mockTemplate;
	
//	@MockBean(classes = KafkaTemplate.class)
//	KafkaTemplate<String,String> template;
	
//	@MockBean 
//	CepMonitorService cepMonitorService;
	
	@Autowired
	CustomerService customerService;
	
	@Autowired
	TradeService tradeService;


	@Test
	public void test001() throws InterruptedException {

//		Mockito.verify(eventProducer,Mockito.times(3)).send(Mockito.anyString(), Mockito.anyString());
//		Mockito.when(eventProducer.send(Mockito.anyString(),Mockito.anyString())).thenReturn(null);
		
		Customer customer = new Customer();
		customer.setName("name");
		customer.setRank(5);
		customerService.save(customer);
		assertEquals(customer.getName(), "name");
		System.out.println("**********************************************assert data******************");
		

		Thread.sleep(100000);
	//	customerService.findAll();
		Thread.sleep(10000);
	}
//	@Test
	public void test002() throws InterruptedException {
		Customer customer = new Customer();
		customer.setName("name");
		customer.setRank(5);
		customerService.save(customer);
		assertEquals(customer.getName(), "name");
		Thread.sleep(10000);
	}
}
