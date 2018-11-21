package org.esb.http;

import org.esb.common.URL;


public interface HttpServer {

	void start();
	
	void stop();
	
	URL getUrl();
	
	Handler getHandler();
}
