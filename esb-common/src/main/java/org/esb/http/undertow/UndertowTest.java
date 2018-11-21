package org.esb.http.undertow;

import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.api.ServletInfo;
import io.undertow.servlet.spec.HttpServletRequestImpl;
import io.undertow.servlet.spec.HttpServletResponseImpl;
import io.undertow.servlet.spec.ServletContextImpl;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.cxf.bus.extension.ExtensionManagerBus;
import org.apache.cxf.transport.http.DestinationRegistry;
import org.apache.cxf.transport.http.HTTPTransportFactory;
import org.apache.cxf.transport.http.HttpDestinationFactory;
import org.apache.cxf.transport.servlet.ServletController;
import org.apache.cxf.transport.servlet.ServletDestinationFactory;
import org.esb.http.servlet.DispatcherServlet;

public class UndertowTest {

	public static final String MYAPP = "/myapp";

	public static void main(final String[] args) {

		ExtensionManagerBus bus = new ExtensionManagerBus();
		bus.setExtension(new ServletDestinationFactory(),
				HttpDestinationFactory.class);
		final HTTPTransportFactory transportFactory = new HTTPTransportFactory(
				(DestinationRegistry) bus);

		ServletInfo servletInfo = Servlets.servlet("MessageServlet",
				DispatcherServlet.class).addMapping("/*");
		
		//servletInfo.

		DeploymentInfo servletBuilder = Servlets.deployment()
				.setClassLoader(UndertowTest.class.getClassLoader())
				.setContextPath(MYAPP).addServlet(servletInfo);

		final DeploymentManager manager = Servlets.defaultContainer()
				.addDeployment(servletBuilder);
		manager.deploy();

		final ServletContext servletContext = manager.getDeployment()
				.getServletContext();

		Undertow server = Undertow.builder().addHttpListener(8080, "localhost")
				.setHandler(new HttpHandler() {

					private volatile ServletController servletController;

					@Override
					public void handleRequest(final HttpServerExchange exchange)
							throws Exception {
						HttpServletRequest request = new HttpServletRequestImpl(
								exchange, (ServletContextImpl) servletContext);
						HttpServletResponse response = new HttpServletResponseImpl(
								exchange, (ServletContextImpl) servletContext);
//						if (servletController == null) {
//							synchronized (this) {
//								if (servletController == null) {
//									servletController = new ServletController(
//											transportFactory.getRegistry(),
//											servletContext, request);
//								}
//							}
//						}
						servletController.invoke(request, response);
					}
				}).build();

	}
}
