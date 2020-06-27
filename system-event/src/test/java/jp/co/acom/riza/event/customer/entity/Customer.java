package jp.co.acom.riza.event.customer.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Version;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.envers.Audited;

@Data
@Entity
@Audited
@EqualsAndHashCode(of = "id")
public class Customer {
  @Id @GeneratedValue private Long id;
  private String name;
  private Integer rank;

  @Version private long version;
}
