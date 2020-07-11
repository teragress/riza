package jp.co.acom.riza.system.object.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class ObjectAnnotation {
	@Retention(RetentionPolicy.RUNTIME)
	public @interface AuditMessage {
	}
	@Retention(RetentionPolicy.RUNTIME)
	public @interface NoEvent {
	}
}
