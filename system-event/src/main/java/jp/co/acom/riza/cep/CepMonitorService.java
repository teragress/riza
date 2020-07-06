package jp.co.acom.riza.cep;

import java.net.URI;
import java.net.URISyntaxException;
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

import jp.co.acom.riza.cep.event.RizaCepEventFinish;
import jp.co.acom.riza.cep.event.RizaCepEventResponse;
import jp.co.acom.riza.cep.event.RizaCepEventStart;
import jp.co.acom.riza.system.utils.log.Logger;

/**
 * CEP監視サービス
 *
 * @author developer
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
	 * デフォルトのCEP URI
	 */
	private static final String MONITOR_DEFAULT_CEP_URI = "http://localhost:8080/rest/sep";
	
	@PostConstruct
	public void initialize() {
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
	 * @param key
	 * @param date
	 * @throws URISyntaxException
	 */
	public void startMonitor(String key, Date date) {
		RizaCepEventStart eventStart = new RizaCepEventStart();
		eventStart.setEntryKeyId(key);
		eventStart.setEventDate(date);
		eventStart.setExpireLimit(expireLimit);
		RequestEntity<RizaCepEventStart> req = new RequestEntity<>(eventStart, headers, HttpMethod.POST, startUri);
		ResponseEntity<RizaCepEventResponse> rsp = restTemplate.exchange(req, RizaCepEventResponse.class);
		if (rsp == null || rsp.getBody().getRc() != RizaCepEventResponse.RC.NORMAL) {
			logger.error(rsp.getBody().toString());
		}
	}

	/**
	 * CEP監視終了
	 * @param key
	 * @throws URISyntaxException
	 */
	public void endMonitor(String key) {
		RizaCepEventFinish eventEnd = new RizaCepEventFinish();
		eventEnd.setEntryKeyId(key);
		RequestEntity<RizaCepEventFinish> req = new RequestEntity<>(eventEnd, headers, HttpMethod.POST, endUri);
		ResponseEntity<RizaCepEventResponse> rsp = restTemplate.exchange(req, RizaCepEventResponse.class);
		if (rsp == null || rsp.getBody().getRc() != RizaCepEventResponse.RC.NORMAL) {
			logger.error(rsp.getBody().toString());
		}
	}
}
