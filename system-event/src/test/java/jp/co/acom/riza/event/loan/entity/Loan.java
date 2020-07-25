package jp.co.acom.riza.event.loan.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Version;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.envers.Audited;

import jp.co.acom.riza.system.object.annotation.ObjectAnnotation.AuditMessage;
import jp.co.acom.riza.system.object.annotation.ObjectAnnotation.NoEvent;

@Data
@Entity
@Audited
@AuditMessage
@EqualsAndHashCode(of = "id")
public class Loan {
  @Id @GeneratedValue private Long id;
  private String name;
  private Integer rank;

  @Version private long version;
}
