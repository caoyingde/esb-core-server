package org.esb.rpc.webservice;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.cxf.bus.extension.ExtensionManagerBus;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.frontend.ClientProxyFactoryBean;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transport.http.HttpDestinationFactory;
import org.apache.cxf.transport.servlet.ServletDestinationFactory;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.apache.log4j.Logger;
import org.esb.akka.EsbContext;
import org.esb.common.URL;
import org.esb.consumer.ConsumerBean;
import org.esb.core.Constants;
import org.esb.http.HttpServer;
import org.esb.protocol.AbstractProtocol;
import org.esb.provider.ProviderBean;

/**
 * @author Andy.Cao
 * @date 2018-11-21
 * @deprecated
 */
public class WebServiceProtocol3 extends AbstractProtocol {

    private static final long serialVersionUID = -1378005990604591964L;
    public static final String Protocol_Name = "webservice";

    private Logger logger = Logger.getLogger(WebServiceProtocol3.class);

    private final Map<String, HttpServer> serverMap = new ConcurrentHashMap<String, HttpServer>();

    private final ExtensionManagerBus bus = new ExtensionManagerBus();


    public WebServiceProtocol3() {
        bus.setExtension(new ServletDestinationFactory(), HttpDestinationFactory.class);
    }

    @Override
    public int getDefaultPort() {
        return 80;
    }

    @Override
    public String getProtocolName() {
        return Protocol_Name;
    }

    @Override
    protected void doProvide(ProviderBean provider, URL url) {
        logger.info("Provide WebService Server Successed: " + url.getFullPath()
                + provider.getName() + "?wsdl");
        String addr = url.getHost() + ":" + url.getPort();
        HttpServer httpServer = serverMap.get(addr);
        if (httpServer == null) {
            //  httpServer = binder.bind(url, new WebServiceHandler());
            serverMap.put(addr, httpServer);
        }
    }

    @Override
    protected <T> T doConsume(ConsumerBean consumer, final URL url) {
        ClientProxyFactoryBean proxyFactoryBean = new ClientProxyFactoryBean();
        proxyFactoryBean.setAddress(url.getFullPath() + consumer.getName());
        proxyFactoryBean.setServiceClass(consumer.getInterfaze());
        @SuppressWarnings("unchecked")
        T ref = (T) proxyFactoryBean.create();
        Client proxy = ClientProxy.getClient(ref);
        HTTPConduit conduit = (HTTPConduit) proxy.getConduit();
        HTTPClientPolicy policy = new HTTPClientPolicy();
        policy.setConnectionTimeout(Constants.DEFAULT_CONNECT_TIMEOUT);
        policy.setReceiveTimeout(Constants.DEFAULT_TIMEOUT);
        conduit.setClient(policy);
        return ref;
    }

    @Override
    public String getProtocol() {
        return "http";
    }

    @Override
    protected URL getDefaultUrl() {
        URL url = new URL();
        url.setHost(EsbContext.getAkkaEsbSystem().getHost());
        url.setPort(getDefaultPort());
        url.setProtocol(getProtocol());
        url.setPath("ws");
        return url;
    }

    @Override
    public void start() {
        logger.info("WebService Protocol started!");
    }

}
