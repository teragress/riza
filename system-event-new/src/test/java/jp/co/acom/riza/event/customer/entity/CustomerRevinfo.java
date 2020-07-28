package jp.co.acom.riza.event.customer.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

@Entity(name = "CUSTOMER_REVINFO")
@RevisionEntity
public class CustomerRevinfo  {
	@Id
	@GeneratedValue
	@RevisionNumber
	private long id;

	@RevisionTimestamp
	private long timestamp;

	public long getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Transient
	public Date getRevisionDate() {
		return new Date( timestamp );
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

}
