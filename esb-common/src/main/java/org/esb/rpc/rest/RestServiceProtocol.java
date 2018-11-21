package org.esb.rpc.rest;

import java.io.Serializable;

import org.apache.cxf.binding.BindingFactoryManager;
import org.apache.cxf.jaxrs.JAXRSBindingFactory;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;
import org.apache.cxf.jaxrs.provider.JAXBElementProvider;
import org.apache.log4j.Logger;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.esb.akka.EsbContext;
import org.esb.common.URL;
import org.esb.consumer.ConsumerBean;
import org.esb.protocol.AbstractProtocol;
import org.esb.provider.ProviderBean;

/**
 * @author Andy.Cao
 * @date 2018-11-21
 * @deprecated
 */
public class RestServiceProtocol extends AbstractProtocol {

    private static final long serialVersionUID = -4961906481351558943L;

    public static final String Protocol_Name = "restful";

    private Logger logger = Logger.getLogger(RestServiceProtocol.class);

    private JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();

    public RestServiceProtocol() {
        BindingFactoryManager manager = sf.getBus().getExtension(
                BindingFactoryManager.class);
        JAXRSBindingFactory factory = new JAXRSBindingFactory();
        factory.setBus(sf.getBus());
        manager.registerBindingFactory(JAXRSBindingFactory.JAXRS_BINDING_ID,
                factory);
    }

    @Override
    public int getDefaultPort() {
        return 9900;
    }

    @Override
    public String getProtocol() {
        return "http";
    }

    @Override
    public String getProtocolName() {
        return Protocol_Name;
    }

    @Override
    protected void doProvide(ProviderBean provider, URL url) {
        sf.setResourceClasses(provider.getImplementor().getClass());
        sf.setResourceProvider(provider.getImplementor().getClass(),
                new SingletonResourceProvider(provider.getImplementor()));
        sf.setAddress(url.getFullPath());
        logger.info("Provide Restful Server Successed: " + provider.getName());
    }

    @Override
    protected <T> T doConsume(ConsumerBean consumer, final URL url) {
        return null;
    }

    @Override
    protected URL getDefaultUrl() {
        URL url = new URL();
        url.setHost(EsbContext.getAkkaEsbSystem().getHost());
        url.setPort(getDefaultPort());
        url.setProtocol(getProtocol());
        url.setPath("rest");
        return url;
    }

    @Override
    public void start() {
        if (!sf.getResourceClasses().isEmpty()) {
            sf.setProvider(new JacksonJsonProvider());
            sf.setProvider(new JAXBElementProvider<Serializable>());
            sf.create();
            logger.info("Restful Service Protocol started!");
        } else {
            logger.info("Restful Service Resource is Empty! Restful not start...");
        }

    }

}
