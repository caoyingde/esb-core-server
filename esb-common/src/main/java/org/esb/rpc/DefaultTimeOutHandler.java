package org.esb.rpc;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;

import akka.actor.ActorRef;

/**
 * @author Andy.Cao
 * @date 2018-11-21
 * @deprecated
 */
public class DefaultTimeOutHandler implements TimeOutHandler {

    private Logger logger = Logger.getLogger(DefaultTimeOutHandler.class);

    @Override
    public Object invoke(Class<?> clz, Method method, Object[] args,
                         ActorRef remoteActor) {
        logger.debug("Defualt Timeout Handler return null");
        return null;
    }

    @Override
    public int tryTimes() {
        return 1;
    }

}
