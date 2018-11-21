package org.esb.http;

import org.esb.common.URL;

public interface Binder {

	HttpServer bind(URL url, Handler handler);
}
