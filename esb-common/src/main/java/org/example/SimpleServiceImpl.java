package org.example;

import org.springframework.stereotype.Component;
import org.esb.common.Provider;

@Component
@Provider(protocol = { "webservice", "restful" })
public class SimpleServiceImpl implements SimpleService {

	@Override
	public String sayHello(String name) {
		System.out.println("Be Called");
		return "Hello " + name;
	}

	@Override
	public TestPojo getPojo(String name) {
		return new TestPojo(name);
	}

	@Override
	public TestPojo[] getPojo2(String name) {
		return new TestPojo[] { new TestPojo(name) };
	}

}
