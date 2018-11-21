package org.esb.protocol;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.esb.common.URL;
import org.esb.consumer.ConsumerBean;
import org.esb.provider.ProviderBean;

public abstract class AbstractProtocol implements Protocol, Serializable {

	private static final long serialVersionUID = 1087532749177338091L;

	private final Set<String> providers = new HashSet<String>();
	private final Set<String> consumers = new HashSet<String>();
	
	private URL url;

	@Override
	public void provide(ProviderBean provider) {
		providers.add(provider.getName());
		doProvide(provider, getUrl());
	}

	@Override
	public <T> T consume(ConsumerBean consumer) {
		consumers.add(consumer.getName());
		return doConsume(consumer,url);
	}

	protected abstract void doProvide(ProviderBean provider, URL url);

	protected abstract <T> T doConsume(ConsumerBean consumer, final URL url);

	public URL getUrl() {
		if(url == null){
			url = getDefaultUrl();
		}
		return url;
	}

	protected abstract URL getDefaultUrl();

	public void setUrl(URL url) {
		this.url = url;
	}
	
}
