package org.esb.http;

import org.esb.common.URL;

/**
 * @author Andy.Cao
 * @date 2018-11-21
 * @deprecated
 */
public interface HttpServer {

    void start();

    void stop();

    URL getUrl();

    Handler getHandler();
}
