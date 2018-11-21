package org.esb.rpc;

import org.esb.akka.EsbContext;
import org.esb.common.URL;
import org.esb.consumer.ConsumerBean;
import org.esb.protocol.AbstractProtocol;
import org.esb.provider.ProviderBean;

import akka.actor.ActorRef;
import akka.actor.Props;

/**
 * @author Andy.Cao
 * @date 2018-11-21
 * @deprecated
 */
public class DefaultProtocol extends AbstractProtocol {

    private static final long serialVersionUID = 3142448158778286530L;
    public static final String Protocol_Name = "default";

    private ActorRef rpcServerActor;

    public DefaultProtocol() {
        rpcServerActor = EsbContext.getAkkaEsbSystem().getSystem().actorOf(Props.create(RpcServerActor.class));
    }

    @Override
    public int getDefaultPort() {
        return -1;
    }

    @Override
    public String getProtocolName() {
        return Protocol_Name;
    }

    @Override
    protected void doProvide(ProviderBean provider, URL url) {
        rpcServerActor.tell(provider, rpcServerActor);
    }

    @Override
    protected <T> T doConsume(ConsumerBean consumer, final URL url) {
        return null;
    }

    @Override
    public String getProtocol() {
        return null;
    }

    @Override
    protected URL getDefaultUrl() {
        return null;
    }

    @Override
    public void start() {

    }

}
