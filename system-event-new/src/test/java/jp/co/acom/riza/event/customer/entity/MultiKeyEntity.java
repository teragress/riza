package jp.co.acom.riza.event.customer.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Version;
import lombok.Data;
import org.hibernate.envers.Audited;

import jp.co.acom.riza.system.object.annotation.ObjectAnnotation.AuditMessage;

@Data
@Entity
@Audited
@AuditMessage
//@EqualsAndHashCode(of = "md")
public class MultiKeyEntity {
  @EmbeddedId private MultiKey multiKey;
  private String name;
  private Integer rank;

  @Version private long version;
}
