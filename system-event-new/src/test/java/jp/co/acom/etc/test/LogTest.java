package jp.co.acom.etc.test;


import org.springframework.boot.autoconfigure.SpringBootApplication;

import jp.co.acom.riza.system.utils.log.Logger;

/** A spring-boot application. */
@SpringBootApplication
//@ComponentScan(basePackages="jp.co.acom")
public class LogTest {
	private static final Logger logger = Logger.getLogger(LogTest.class);	
  // must have a main method spring-boot can run
  public static void main(String[] args) {
	  logger.info("aaaaa{}bbbbbb{}cccccc{}","1","2","3");

  }

}
