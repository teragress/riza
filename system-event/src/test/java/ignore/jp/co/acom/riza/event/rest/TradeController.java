package ignore.jp.co.acom.riza.event.rest;

import jp.co.acom.riza.event.service.TradeService;
import jp.co.acom.riza.event.trade.entity.Trade;

import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** 処理を開始するための REST を受け付ける. */
//@RestController
//@RequestMapping("rest/trade")
public class TradeController {

  private TradeService tradeService;

  public TradeController(TradeService tradeService) {
    this.tradeService = tradeService;
  }

  /**
   * 新規登録.
   *
   * @param trade 登録する情報
   * @return
   */
  @PutMapping(
      consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public Trade save(@RequestBody Trade trade) {
    tradeService.save(trade);
    return trade;
  }

  /**
   * Trade の情報を取得する.
   *
   * @param customerId 取得する Customer の id. null の場合は全件取得.
   * @return 情報のリスト
   */
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public List<Trade> getByCustomer(
      @RequestParam(name = "customerId", required = false) Long customerId) {
    if (customerId == null) {
      return tradeService.getAll();
    } else {
      return tradeService.getByCustomer(customerId);
    }
  }
}
