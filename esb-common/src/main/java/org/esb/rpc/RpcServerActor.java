package org.esb.rpc;

import org.apache.log4j.Logger;
import org.esb.provider.ProviderBean;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.routing.RoundRobinPool;

/**
 * @author Andy.Cao
 * @date 2018-11-21
 * @deprecated
 */
public class RpcServerActor extends UntypedActor {

    private Logger logger = Logger.getLogger(RpcServerActor.class);

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof ProviderBean) {
            ProviderBean provider = (ProviderBean) message;
            ActorRef providerActor = getContext().actorOf(
                    new RoundRobinPool(2).props(Props.create(
                            RpcProviderActor.class, provider)),
                    provider.getName());
            getContext().watch(providerActor);
            logger.info("Provide default rpc server successed: " + providerActor.toString());
        }
    }

}
