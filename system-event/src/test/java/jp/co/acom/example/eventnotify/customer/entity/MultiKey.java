package jp.co.acom.example.eventnotify.customer.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;
import lombok.Data;
import org.hibernate.envers.Audited;

@Data
@Audited
@Embeddable
public class MultiKey implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2121117402991040958L;
	private String key1;
	private Integer key2;

	public MultiKey(String key1, Integer key2) {
		this.key1 = key1;
		this.key2 = key2;
	}

	public MultiKey() {
		super();

	}

}
