package jp.co.acom.example.eventnotify.customer.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Version;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.envers.Audited;

@Data
@Entity
@Audited
//@EqualsAndHashCode(of = "md")
public class MultiKeyEntity {
  @EmbeddedId private MultiKey multiKey;
  private String name;
  private Integer rank;

  @Version private long version;
}
