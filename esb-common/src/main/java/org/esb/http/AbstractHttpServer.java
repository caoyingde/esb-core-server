package org.esb.http;

import org.esb.common.URL;

/**
 * @author Andy.Cao
 * @date 2018-11-21
 * @deprecated
 */
public abstract class AbstractHttpServer implements HttpServer {
    private final URL url;

    private final Handler handler;

    public AbstractHttpServer(URL url, Handler handler) {
        if (url == null) {
            throw new IllegalArgumentException("url == null");
        }
        if (handler == null) {
            throw new IllegalArgumentException("handler == null");
        }
        this.url = url;
        this.handler = handler;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public URL getUrl() {
        return url;
    }

    @Override
    public Handler getHandler() {
        return handler;
    }


}
