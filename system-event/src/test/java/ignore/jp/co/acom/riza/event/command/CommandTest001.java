package ignore.jp.co.acom.riza.event.command;

import org.infinispan.Cache;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.concurrent.ListenableFuture;

import static org.junit.Assert.assertTrue;
//import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.concurrent.ExecutionException;

import com.fasterxml.jackson.databind.ObjectMapper;

import jp.co.acom.riza.event.cmd.EventRestCommand;
import jp.co.acom.riza.event.cmd.parm.KafkaMessageInfo;
import jp.co.acom.riza.event.cmd.parm.KafkaRecoveryParm;
import jp.co.acom.riza.event.kafka.KafkaCommandUtil;
import jp.co.acom.riza.system.utils.log.LogAssertAppender;

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
	
	@Autowired
	Environment env;

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
		LogAssertAppender.clear();
		
		KafkaRecoveryParm parm = new KafkaRecoveryParm();

		parm.getMsgInfo().add(sendKafkaTestData("testTopic01","test message001"));
		parm.getMsgInfo().add(sendKafkaTestData("testTopic01","test message002"));
		System.out.println("********************om=" + om.writeValueAsString(parm));
		mockMvc.perform(MockMvcRequestBuilders.put("/event/command/KafkaRecovery/Offset")
				.contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsString(parm)))
				.andExpect(status().isOk()).andExpect(jsonPath("rc").value("OK"));
		assertTrue(LogAssertAppender.assertString(".*RIZE0001I イベントコマンドが開始されました。.*"));
		assertTrue(LogAssertAppender.assertString(".*RIZAE006I KAFKAメッセージをリカバリー.*topic.*testTopic01.*offset.*partition.*topic.*testTopic01.*offset.*partition.*"));
		assertTrue(LogAssertAppender.assertString(".*RIZE0002I イベントコマンドが終了しました。.*"));
		LogAssertAppender.clear();

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