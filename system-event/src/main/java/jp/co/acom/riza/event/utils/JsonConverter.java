package jp.co.acom.riza.event.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jp.co.acom.riza.utils.log.Logger;

/**
 * @author mtera1003
 *
 */
public class JsonConverter {
	/**
	 * ロガー
	 */
	private static Logger logger = Logger.getLogger(JsonConverter.class);
	/**
	 * Objectからjson形式に変換
	 * @param o
	 * @return
	 */
	public static String objectToJsonString(Object o) {
		String str = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			str = mapper.writeValueAsString(o);
		} catch (JsonProcessingException e) {
			logger.error("obj to json convert error", e);
		}
		return str;
	}
}
