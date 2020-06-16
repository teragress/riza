package jp.co.acom.riza.context;

import java.util.Date;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Component
@Scope(value = "transaction", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CommonContext {

	private String reqeustId;
	private String userId;
	private String flowid;
	private Date date;
	private String traceId;
	private String spanId;
}
