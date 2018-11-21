package org.esb.config.spring;

import org.apache.cxf.frontend.ServerFactoryBean;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.topteam.example.SimpleServiceImpl;

public class WebServiceTest {

	public static void main(String[] args) {
		SimpleServiceImpl serviceImpl = new SimpleServiceImpl();
		ServerFactoryBean svrFactory = new ServerFactoryBean();
		svrFactory.setServiceClass(SimpleServiceImpl.class);   //可省，但不建议，因为可能会有些小问题
		svrFactory.setAddress("http://192.168.1.121:9001/helloWorld");
		svrFactory.setServiceBean(serviceImpl);
		svrFactory.getInInterceptors().add(new LoggingInInterceptor());
		svrFactory.getOutInterceptors().add(new LoggingOutInterceptor());
		svrFactory.create();
	}
}
