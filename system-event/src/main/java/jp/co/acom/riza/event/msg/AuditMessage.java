package jp.co.acom.riza.event.msg;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 監査メッセージ情報
 * 
 * @author vagrant
 *
 */
@Getter
@Setter
@ToString
public class AuditMessage {
	
	/**
	 * ユーザーID
	 */
	private String user;

	/**
	 * 監査エンティティリスト
	 * 
	 */
	private List<AuditEntity> auditEntity = new ArrayList<AuditEntity>();

}
