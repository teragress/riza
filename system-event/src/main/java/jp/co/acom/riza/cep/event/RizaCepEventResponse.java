package jp.co.acom.riza.cep.event;

import lombok.Data;

@Data
public class RizaCepEventResponse {
	public enum RC {
		NORMAL, WARNING ,ERROR
	}

	/**
	* リターンコード
	*/
	private RC rc;

	/**
	* 付加情報
	*/
	private String addInfo;

}
