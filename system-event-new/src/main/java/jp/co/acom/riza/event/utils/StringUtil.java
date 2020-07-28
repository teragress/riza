package jp.co.acom.riza.event.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jp.co.acom.riza.event.msg.EntityEvent;
import jp.co.acom.riza.event.msg.TranEvent;
import jp.co.acom.riza.system.utils.log.Logger;

/**
 * ストリングユーティリティ
 * 
 * @author teratani
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
	 * エンティティイベントストリングからエンティティイベントオブジェクトへ変換する
	 * 
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static EntityEvent stringToEntityEventObject(String str)
			throws Exception {

		ObjectMapper objectMapper = new ObjectMapper();

		EntityEvent entityEvent = objectMapper.readValue(str, EntityEvent.class);

		return entityEvent;
	}

	/**
	 * トランザクションイベントオブジェクトから文字列に変換
	 * 
	 * @param str
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static TranEvent stringToTranEventEventObject(String str)
			throws JsonParseException, JsonMappingException, IOException {

		ObjectMapper objectMapper = new ObjectMapper();

		TranEvent tranEvent = objectMapper.readValue(str, TranEvent.class);

		return tranEvent;
	}

	/**
	 * 文字列を指定サイズで分割してリストを返す
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
