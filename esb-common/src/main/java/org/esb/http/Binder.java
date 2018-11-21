package org.esb.http;

import org.esb.common.URL;

/**
 * @author Andy.Cao
 * @date 2018-11-21
 * @deprecated
 */
public interface Binder {

    HttpServer bind(URL url, Handler handler);
}
