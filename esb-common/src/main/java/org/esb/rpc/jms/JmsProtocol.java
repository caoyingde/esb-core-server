package org.esb.rpc.jms;

import org.esb.common.URL;
import org.esb.consumer.ConsumerBean;
import org.esb.protocol.AbstractProtocol;
import org.esb.provider.ProviderBean;

/**
 * @author Andy.Cao
 * @date 2018-11-21
 * @deprecated
 */
public class JmsProtocol extends AbstractProtocol {

    private static final long serialVersionUID = 3770768271950827913L;

    @Override
    public int getDefaultPort() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String getProtocol() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getProtocolName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void doProvide(ProviderBean provider, URL url) {
        // TODO Auto-generated method stub

    }

    @Override
    protected <T> T doConsume(ConsumerBean consumer, final URL url) {
        return null;
    }

    @Override
    protected URL getDefaultUrl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void start() {
        // TODO Auto-generated method stub

    }

}
