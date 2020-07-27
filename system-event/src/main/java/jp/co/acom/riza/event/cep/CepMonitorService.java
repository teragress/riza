package jp.co.acom.riza.event.cep;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jp.co.acom.riza.cep.data.RizaCepEventResponse;
import jp.co.acom.riza.cep.event.RizaCepEventFinish;
import jp.co.acom.riza.cep.event.RizaCepEventStart;
import jp.co.acom.riza.event.config.EventMessageId;
import jp.co.acom.riza.system.utils.log.Logger;
import jp.co.acom.riza.system.utils.log.MessageFormat;

/**
 * CEPリクエストサービス
 *
 * @author teratani
 *
 */
@Service
@Scope
public class CepMonitorService {
	/**
	 * ロガー
	 */
	private static Logger logger = Logger.getLogger(CepMonitorService.class);

	/**
	 * 環境変数取得用
	 */
	@Autowired
	private Environment env;

	/**
	 * 処理待ち時間(秒)
	 */
	private int expireLimit;

	/**
	 * HttpHeaders
	 */
	private HttpHeaders headers;

	/**
	 * CEP監視開始用URI
	 */
	private URI startUri;

	/**
	 * CEP監視終了用URI
	 */
	private URI endUri;

	/**
	 * REST要求用テンプレート
	 */
	private RestTemplate restTemplate;

	/**
	 * テスト用のモック指定
	 */
	private Boolean mock;

	/**
	 * デフォルトのCEP URI
	 */
	private static final String MONITOR_DEFAULT_CEP_URI = "http://localhost:8080/rest/sep";

	/**
	 * 初期化
	 */
	@PostConstruct
	public void initialize() {

		mock = env.getProperty(CepConstants.CEP_MOCK, Boolean.class, false);
		if (mock) {
			return;
		}

		String cepBaseUri = env.getProperty(CepConstants.CEP_BASE_URI, String.class, MONITOR_DEFAULT_CEP_URI);
		try {

			startUri = new URI(cepBaseUri + "/" + CepConstants.CEP_START_METHOD);
			endUri = new URI(cepBaseUri + "/" + CepConstants.CEP_END_METHOD);
		} catch (Exception e) {
			logger.error("", e);
		}
		expireLimit = env.getProperty(CepConstants.CEP_EXPIRE_LIMIT, Integer.class,
				CepConstants.CEP_DEFAULT_EXPIRE_LIMIT);
		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		restTemplate = new RestTemplate(new SimpleClientHttpRequestFactory());
	}

	/**
	 * CEP監視開始
	 * 
	 * @param key キー
	 * @param date キー日時
	 * @throws URISyntaxException
	 */
	public void startMonitor(String key, LocalDateTime dateTime) {
		if (mock) {
			return;
		}
		try {
			RizaCepEventStart eventStart = new RizaCepEventStart();
			eventStart.setEntryKeyId(key);
			ZonedDateTime zdt = dateTime.atZone(ZoneId.systemDefault());
			Date date = Date.from(zdt.toInstant());
			eventStart.setEventDate(date);
			eventStart.setExpireLimit(expireLimit);
			RequestEntity<RizaCepEventStart> req = new RequestEntity<>(eventStart, headers, HttpMethod.POST, startUri);
			ResponseEntity<RizaCepEventResponse> rsp = restTemplate.exchange(req, RizaCepEventResponse.class);
			if (rsp == null || rsp.getBody().getRc() != RizaCepEventResponse.RC.NORMAL) {
				logger.error(MessageFormat.get(EventMessageId.CEP_ERROR), "start", key, dateTime, rsp);
			}
		} catch (Exception ex) {
			logger.error(MessageFormat.get(EventMessageId.CEP_ERROR), "start", key, dateTime, ex.getMessage());
			logger.error(MessageFormat.get(EventMessageId.EVENT_EXCEPTION),ex);
		}
	}

	/**
	 * CEP監視終了
	 * 
	 * @param key キー
	 * @param date キー日時
	 * @throws URISyntaxException
	 */
	public void endMonitor(String key, LocalDateTime dateTime) {
		if (mock) {
			return;
		}
		try {
			RizaCepEventFinish eventEnd = new RizaCepEventFinish();
			eventEnd.setEntryKeyId(key);
			RequestEntity<RizaCepEventFinish> req = new RequestEntity<>(eventEnd, headers, HttpMethod.POST, endUri);
			ResponseEntity<RizaCepEventResponse> rsp = restTemplate.exchange(req, RizaCepEventResponse.class);
			if (rsp == null || rsp.getBody().getRc() != RizaCepEventResponse.RC.NORMAL) {
				logger.error(MessageFormat.get(EventMessageId.CEP_ERROR), "end", key, dateTime, rsp);
			}
		} catch (Exception ex) {
			logger.error(MessageFormat.get(EventMessageId.CEP_ERROR), "end", key, dateTime, ex.getMessage());
			logger.error(MessageFormat.get(EventMessageId.EVENT_EXCEPTION),ex);
		}
	}
}
