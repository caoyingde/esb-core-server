package org.esb.config.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.esb.consumer.ConsumerFactoryBean;
import org.topteam.example.SimpleService;
import org.topteam.example.TestPojo;

public class ConsumerTest {

	public static void main(String[] args) {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"classpath:spring-esb-consumer.xml");

		while (true) {
			long t1 = System.nanoTime();
			SimpleService simpleService = applicationContext
					.getBean(SimpleService.class);
			TestPojo result = simpleService.getPojo("测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测");
			long t2 = System.nanoTime();
			System.out.println(result.getLen() + "  " + (t2 - t1));
		}
	}
}
