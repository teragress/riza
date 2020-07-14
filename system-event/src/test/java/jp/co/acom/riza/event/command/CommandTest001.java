package jp.co.acom.riza.event.command;

import org.infinispan.Cache;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.concurrent.ListenableFuture;

//import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.concurrent.ExecutionException;

import com.fasterxml.jackson.databind.ObjectMapper;

import jp.co.acom.riza.event.command.parm.KafkaMessageInfo;
import jp.co.acom.riza.event.command.parm.KafkaRecoveryParm;
import jp.co.acom.riza.event.kafka.KafkaCommandUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(classes = AppConfig.class)
@AutoConfigureMockMvc
public class CommandTest001 {

	private MockMvc mockMvc;
	private ObjectMapper om;

	@Autowired
	EventRestCommand target;
	
	@Autowired
	KafkaCommandUtil kafkaUtil;
	
	@Autowired
	KafkaTemplate<String, String> kafkaTemplate;

	@BeforeClass
	public static void initialize() {

	}

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(target).build();
		om = new ObjectMapper();
	}

	@Test
	public void case001_StartEventNormalTest() throws Exception {

		mockMvc.getDispatcherServlet().log("+++++ Test case 001 : new entry ++++++");

		KafkaRecoveryParm parm = new KafkaRecoveryParm();

		parm.getMsgInfo().add(sendKafkaTestData("testTopic01","test message001"));
		parm.getMsgInfo().add(sendKafkaTestData("testTopic01","test message002"));
		System.out.println("********************om=" + om.writeValueAsString(parm));
		mockMvc.perform(MockMvcRequestBuilders.put("/event/command/KafkaRecovery/Offset")
				.contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsString(parm)))
				.andExpect(status().isOk()).andExpect(jsonPath("rc").value("OK"));

	}
	private KafkaMessageInfo sendKafkaTestData(String topic,String message) {
		
		KafkaMessageInfo msgInfo = new KafkaMessageInfo();
		ListenableFuture<SendResult<String, String>> sendResultList = kafkaTemplate.send(topic,message);
		// .get(10L,TimeUnit.SECONDS);
		try {
			msgInfo.setPartition(sendResultList.get().getRecordMetadata().partition());
			msgInfo.setOffset(sendResultList.get().getRecordMetadata().offset());
			msgInfo.setTopic(topic);
		} catch (InterruptedException | ExecutionException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return msgInfo;	
	}
}