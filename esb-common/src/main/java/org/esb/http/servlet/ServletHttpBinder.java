package org.esb.http.servlet;

import org.esb.common.URL;
import org.esb.http.Binder;
import org.esb.http.Handler;
import org.esb.http.HttpServer;

/**
 * @author Andy.Cao
 * @date 2018-11-21
 * @deprecated
 */
public class ServletHttpBinder implements Binder {

    @Override
    public HttpServer bind(URL url, Handler handler) {
        return new ServletHttpServer(url, handler);
    }

}
