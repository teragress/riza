package jp.co.acom.renove.kafka.event;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.UUID;
import javax.naming.NamingException;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.kafka.KafkaConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jp.co.acom.riza.utils.log.Logger;

/**
 * 受信ファイルイベントを処理するクラス。<br>
 * 受信ファイルを読み込み指定されたトピックにイベントを書き込む。
 * 
 * @author developer
 *
 */
@Component
public class FileEventProcess implements Processor {
	/**
	 * ロガー
	 */
	private static Logger logger = Logger.getLogger(FileEventProcess.class);

//	@Resource(name = "kafka-event-context")
//	private CamelContext camelContext;
	@Autowired
	CamelContext camelContext;
	
	/**
	 * 受信ファイルイベントの処理関数
	 */
	public void process(Exchange exchange) throws Exception {
		System.out.println("***************process() start *******************");
		logger.debug("process() start");
		FileEventParameter param = getFileEventParamater(exchange);
		produceFileEvent(param);

	}

	/**
	 * ファイル受信イベントパラメーターをBEANクラスに変換する。
	 * 
	 * @param exchange
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	private FileEventParameter getFileEventParamater(Exchange exchange)
			throws JsonParseException, JsonMappingException, IOException {
		logger.debug("getFileEventParamater() start");
		String body = exchange.getIn().getBody(String.class);

		ObjectMapper mapper = new ObjectMapper();
		FileEventParameter parm = mapper.readValue(body, FileEventParameter.class);
		return parm;
	}

	/**
	 * @param parm
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void produceFileEvent(FileEventParameter parm) throws FileNotFoundException, IOException,NamingException {


		//System.out.println("**************camel context " + camelContext);
		//camelContext = new DefaultCamelContext();
//		InitialContext initialContext = new InitialContext();
//		camelContext = (CamelContext) initialContext.lookup("java:jboss/camel/context/loan-event-context");
		ProducerTemplate producer = camelContext.createProducerTemplate();

		//KafkaMessageSendUtil util = new KafkaMessageSendUtil();
		String lineStr;
		try (BufferedReader reader = new BufferedReader(new FileReader(parm.getDataFilePath()))) {
			while ((lineStr = reader.readLine()) != null) {
				// 取り敢えずuuid設定
				String uuidStr = UUID.randomUUID().toString();
				producer.requestBodyAndHeader("direct:" + "LoanKeiyaku", lineStr, KafkaConstants.KEY,
						uuidStr, String.class);

			}
		}
	}
}
