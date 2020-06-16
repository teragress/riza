package jp.co.acom.example.eventnotify.trade.entity;

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
public class Trade {
  @Id @GeneratedValue private Long id;

  private Long customerId;
  private Integer total;

  @Version private long version;
}
