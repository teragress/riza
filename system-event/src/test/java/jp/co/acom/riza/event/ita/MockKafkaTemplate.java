package jp.co.acom.riza.event.ita;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class MockKafkaTemplate {
	public void send(String topic,String msg) {
		System.out.println("**********kafkatemplate*******************:" + topic + ":" + msg);
	}
	
}
