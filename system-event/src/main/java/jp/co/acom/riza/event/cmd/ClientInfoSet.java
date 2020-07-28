package jp.co.acom.riza.event.cmd;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import brave.Tracer;
import brave.propagation.TraceContext;

import jp.co.acom.riza.system.CommonConstants;

/**
 * DB2クライアント情報設定
 * 
 * @author teratani
 *
 */
@Service
@Scope(value = CommonConstants.TRANSACTION_SCOPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ClientInfoSet {

	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private Tracer tracer;

	/**
	 * DB2クライアント情報設定
	 * @throws SQLException
	 */
	public void setClientInfo() throws SQLException {
		
		Connection conn = dataSource.getConnection();
		TraceContext traceContext = tracer.currentSpan().context();
		conn.setClientInfo("ApplicationName",traceContext.traceIdString());
		conn.setClientInfo("ClientHostname","DF" + Long.toHexString(traceContext.spanId()));
		conn.setClientInfo("ClientUser","rizacommand");
	}
}
