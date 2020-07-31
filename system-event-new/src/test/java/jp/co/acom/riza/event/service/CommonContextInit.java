package jp.co.acom.riza.event.service;

import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import brave.Tracer;
import brave.propagation.TraceContext;
import jp.co.acom.riza.context.CommonContext;

@Component
@Scope(value = "transaction", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CommonContextInit {
	@Autowired CommonContext context;
	@Autowired
	Tracer tracer;

	@NewSpan
	public void initCommonContxt() {
		context.setLjcomDateTime(LocalDateTime.now());
		context.setBusinessProcess("InsertApl");
		context.setReqeustId(UUID.randomUUID().toString());
		TraceContext traceContext = tracer.currentSpan().context();
		context.setTraceId(traceContext.traceIdString());
		context.setSpanId(Long.toHexString(traceContext.spanId()));
		context.setUserId("userid");
	}
	
}
