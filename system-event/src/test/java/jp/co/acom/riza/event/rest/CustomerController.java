package jp.co.acom.riza.event.rest;

import jp.co.acom.riza.even.service.CustomerService;
import jp.co.acom.riza.event.customer.entity.Customer;
import jp.co.acom.riza.utils.log.Logger;
import jp.co.acom.riza.utils.log.MessageFormat;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/** 処理を開始するための REST を受け付ける. */
//@RestController
//@RequestMapping(path = "rest/customers", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
public class CustomerController {
	/**
	 * ロガー
	 */
	private static Logger logger = Logger.getLogger(CustomerController.class);

  @Autowired
  MessageFormat msg;
  
  private CustomerService customerService;

  public CustomerController(CustomerService customerService) {
    this.customerService = customerService;
  }

  /**
   * 新規登録.
   *
   * @param customer 登録する情報
   * @return
   */
  @RequestMapping("/insert")
  public Customer save(@RequestBody Customer customer) {

	logger.info(MessageFormat.get("RIZA0001"),"イベント","監視");
    customerService.save(customer);
    return customer;
  }

  /**
   * 登録されている情報をすべて取得する.
   * @return 情報のリスト
   */
  @RequestMapping("/list")
  public List<Customer> getAll() {
    return customerService.findAll();
  }

  /**
   * 指定した id の情報を削除する.
   * @param id 削除対象のid
   * @return
   */
  @RequestMapping("/delete")
  public String remove(@PathVariable long id) {
    customerService.delete(id);
    return "Deleted";
  }

  /**
   * 指定した情報で既存の情報を更新する.
   * @param customer 更新内容
   * @return
   */
  @RequestMapping("/update")
  public Customer modify(@RequestBody Customer customer) {
    customerService.modify(customer);
    return customer;
  }
}
