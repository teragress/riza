package jp.co.acom.riza.event.customer.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Version;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.envers.Audited;

import jp.co.acom.riza.system.object.annotation.ObjectAnnotation.AuditMessage;

@Data
@Entity
@Audited
@AuditMessage
//@NoEvent
@EqualsAndHashCode(of = "id")
public class Customer {
  @Id @GeneratedValue private Long id;
  private String name;
  private Integer rank;

  @Version private long version;
}
