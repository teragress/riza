package jp.co.acom.example.eventnotify.rest;

import jp.co.acom.example.eventnotify.customer.entity.Customer;
import jp.co.acom.example.eventnotify.service.CustomerService;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/** 処理を開始するための REST を受け付ける. */
@RestController
@RequestMapping("rest/customers")
public class CustomerController {
  private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);

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
  @RequestMapping(
      method = RequestMethod.PUT,
      consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public Customer save(@RequestBody Customer customer) {
    customerService.save(customer);
    return customer;
  }

  /**
   * 登録されている情報をすべて取得する.
   * @return 情報のリスト
   */
  @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public List<Customer> getAll() {
    return customerService.findAll();
  }

  /**
   * 指定した id の情報を削除する.
   * @param id 削除対象のid
   * @return
   */
  @RequestMapping(
      path = "/{id}",
      method = RequestMethod.DELETE,
      produces = MediaType.TEXT_PLAIN_VALUE)
  public String remove(@PathVariable long id) {
    customerService.delete(id);
    return "Deleted";
  }

  /**
   * 指定した情報で既存の情報を更新する.
   * @param customer 更新内容
   * @return
   */
  @RequestMapping(
      method = RequestMethod.POST,
      consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public Customer modify(@RequestBody Customer customer) {
    customerService.modify(customer);
    return customer;
  }
}
