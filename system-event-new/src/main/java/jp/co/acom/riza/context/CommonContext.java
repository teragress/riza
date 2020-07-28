package jp.co.acom.riza.context;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import jp.co.acom.riza.system.CommonConstants;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Component
@Scope(value = CommonConstants.TRANSACTION_SCOPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CommonContext {

	private String reqeustId;
	private String userId;
	private String businessProcess;
	private LocalDate ljcomDate;
	private LocalTime ljcomTime;
	private LocalDateTime ljcomDateTime;
	private String traceId;
	private String spanId;
}
