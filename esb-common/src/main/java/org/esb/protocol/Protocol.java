package org.esb.protocol;

import org.esb.consumer.ConsumerBean;
import org.esb.provider.ProviderBean;

public interface Protocol {

	int getDefaultPort();
	
	String getProtocol();

	String getProtocolName();

	void provide(ProviderBean provider);

	<T> T consume(ConsumerBean consumer);

	void start();
}
