package org.esb.http.undertow;

import javax.servlet.ServletException;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.handlers.PathHandler;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;

import org.esb.common.URL;
import org.esb.http.AbstractHttpServer;
import org.esb.http.Handler;

/**
 * @author Andy.Cao
 * @date 2018-11-21
 * @deprecated
 */
public class UndertowServer extends AbstractHttpServer {

    private Undertow server;

    public UndertowServer(URL url, Handler handler) throws ServletException {
        super(url, handler);

        DeploymentInfo servletBuilder = Servlets.deployment()
                .setClassLoader(UndertowServer.class.getClassLoader())
                .setContextPath("/myapp").setDeploymentName("test.war");

        DeploymentManager manager = Servlets.defaultContainer().addDeployment(
                servletBuilder);
        manager.deploy();

        PathHandler path = Handlers.path(Handlers.redirect("/myapp"))
                .addPrefixPath("/myapp", manager.start());

        server = Undertow.builder()
                .addHttpListener(url.getPort(), url.getHost()).setHandler(path)
                .build();
    }

}
