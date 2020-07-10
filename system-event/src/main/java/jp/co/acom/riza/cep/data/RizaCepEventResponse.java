package jp.co.acom.riza.cep.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RizaCepEventResponse {
	static public enum RC {
        NORMAL, WARNING, ERROR
    }

    private RC rc;
    private Object addInfo;
}