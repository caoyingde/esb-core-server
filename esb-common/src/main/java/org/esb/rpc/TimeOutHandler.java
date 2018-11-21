package org.esb.rpc;

import java.lang.reflect.Method;

import akka.actor.ActorRef;

public interface TimeOutHandler {

	public static final String DEFAULT_BEAN_NAME = "timeOutHandler";

	public Object invoke(Class<?> clz, Method method, Object[] args, ActorRef remoteActor);

	public int tryTimes();
}
