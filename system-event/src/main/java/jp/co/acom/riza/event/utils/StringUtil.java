package jp.co.acom.riza.event.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jp.co.acom.riza.event.msg.EntityEvent;
import jp.co.acom.riza.system.utils.log.Logger;

/**
 * @author mtera1003
 *
 */
public class StringUtil {
	/**
	 * ロガー
	 */
	private static Logger logger = Logger.getLogger(StringUtil.class);

	/**
	 * Objectからjson形式に変換
	 * 
	 * @param o
	 * @return
	 */
	public static String objectToJsonString(Object o) {
		String str = "";
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			str = objectMapper.writeValueAsString(o);
		} catch (JsonProcessingException e) {
			logger.error("obj to json convert error", e);
		}
		return str;
	}

	/**
	 * @param str
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static EntityEvent stringToEntityEventObject(String str)
			throws JsonParseException, JsonMappingException, IOException {

		ObjectMapper objectMapper = new ObjectMapper();

		EntityEvent entityEvent = objectMapper.readValue(str, EntityEvent.class);

		return entityEvent;
	}

	/**
	 * @param s
	 * @param length
	 * @return
	 */
	public static List<String> splitByLength(String s, int length) {
		List<String> list = new ArrayList<String>();
		while (s != null && s.length() != 0) {
			int endIndex = Math.min(length, s.length());
			list.add(s.substring(0, endIndex));
			s = s.substring(endIndex);
		}
		return list;
	}
}
