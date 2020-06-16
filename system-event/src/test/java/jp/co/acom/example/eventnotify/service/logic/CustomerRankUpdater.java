package jp.co.acom.example.eventnotify.service.logic;

import jp.co.acom.example.eventnotify.customer.entity.Customer;
import jp.co.acom.example.eventnotify.trade.entity.Trade;
import jp.co.acom.example.eventnotify.trade.repository.TradeRepository;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** Customer の Rank を判定する. */
@Component
public class CustomerRankUpdater {
  @Autowired protected TradeRepository tradeRepository;

  /**
   * Customer の Rank を判定し、更新する.
   *
   * @param customer 判定する Customer
   */
  public void updateRank(Customer customer) {
    List<Trade> tradeList = tradeRepository.findByCustomerId(customer.getId());
    int total = tradeList.stream().collect(Collectors.summingInt(Trade::getTotal));
    customer.setRank(total / 10000);
  }
}
