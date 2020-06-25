package jp.co.acom.riza.event.utils;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jp.co.acom.riza.utils.log.Logger;

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
