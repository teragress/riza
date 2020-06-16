package jp.co.acom.example.eventnotify.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.acom.riza.context.CommonContext;

@Service
public class CommonContextInit {
	@Autowired CommonContext context;

	public void initCommonContxt() {
		context.setDate(Date.valueOf(LocalDate.now()));
		context.setFlowid("testFlowId");
		context.setReqeustId("testRequestId");
		context.setSpanId(UUID.randomUUID().toString());
		context.setTraceId(UUID.randomUUID().toString());
		context.setUserId("userid");
	}
	
}
