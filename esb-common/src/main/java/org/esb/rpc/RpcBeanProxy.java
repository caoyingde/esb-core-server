package org.esb.rpc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;
import org.esb.akka.AkkaEsbSystem;
import org.esb.monitor.MonitorEvent;

import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import akka.actor.ActorRef;
import akka.pattern.Patterns;
import akka.util.Timeout;

/**
 * @author Andy.Cao
 * @date 2018-11-21
 * @deprecated
 */
public class RpcBeanProxy implements InvocationHandler {

    private Logger logger = Logger.getLogger(RpcBeanProxy.class);

    private ActorRef remoteActor;

    private Class<?> clz;

    private TimeOutHandler timeOutHandler;

    private AkkaEsbSystem akkaSystem;

    @SuppressWarnings("unchecked")
    public <T> T proxy(ActorRef remoteActor, Class<T> clz,
                       TimeOutHandler timeOutHandler, AkkaEsbSystem akkaSystem) {
        this.remoteActor = remoteActor;
        this.clz = clz;
        this.timeOutHandler = timeOutHandler;
        this.akkaSystem = akkaSystem;
        Class<?>[] clzz = new Class<?>[]{clz};
        return (T) Proxy.newProxyInstance(clz.getClassLoader(), clzz, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        logger.debug("Proxy invoke method " + method.getName() + " ["
                + clz.getName() + "]");
        RpcEvent.CallMethod callMethod = new RpcEvent.CallMethod(
                method.getName(), args, clz.getName());
        Future<Object> future = Patterns.ask(remoteActor, callMethod,
                new Timeout(Duration.create(5, TimeUnit.SECONDS)));
        Object o = null;
        try {
            o = Await.result(future, Duration.create(5, TimeUnit.SECONDS));
        } catch (TimeoutException e) {
            logger.debug("call provider timeout ", e);
            o = timeOutHandler.invoke(clz, method, args, remoteActor);
        }

        if (o == null) {
            akkaSystem.getMonitorActor().tell(
                    new MonitorEvent.ConsumerCallFailureEvent(
                            akkaSystem.getHost() + ":" + akkaSystem.getPort(),
                            clz.getName(), method.getName()), remoteActor);
        }
        return o;
    }
}
