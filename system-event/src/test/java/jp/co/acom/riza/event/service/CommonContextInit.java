package jp.co.acom.riza.event.service;

import java.util.Date;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import jp.co.acom.riza.context.CommonContext;

@Component
@Scope(value = "transaction", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CommonContextInit {
	@Autowired CommonContext context;

	public void initCommonContxt() {
		context.setDate(new Date());
		context.setFlowid("InsertApl");
		context.setReqeustId("testRequestId");
		context.setSpanId(UUID.randomUUID().toString());
		context.setTraceId(UUID.randomUUID().toString());
		context.setUserId("userid");
	}
	
}
