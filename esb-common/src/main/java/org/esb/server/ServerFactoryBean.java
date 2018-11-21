package org.esb.server;

import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.esb.akka.AkkaEsbSystem;

import akka.actor.ActorRef;
import akka.actor.Props;

public class ServerFactoryBean implements ApplicationContextAware {

	private Logger logger = Logger.getLogger(ServerFactoryBean.class);

	private AkkaEsbSystem akkaEsbSystem;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		Map<String, Object> beans = applicationContext
				.getBeansWithAnnotation(Server.class);
		for (Iterator<String> iterator = beans.keySet().iterator(); iterator
				.hasNext();) {
			String key = iterator.next();
			Object bean = beans.get(key);
			if (bean instanceof BaseServer) {
				BaseServer server = (BaseServer) bean;
				ActorRef actorRef = akkaEsbSystem.getSystem()
						.actorOf(
								Props.create(
										server.getActorClass(),
										bean), bean.getClass().getName());
				server.setActor(actorRef);
				server.init();
				logger.info(bean.getClass().getName());
			}
		}
	}

	public AkkaEsbSystem getAkkaEsbSystem() {
		return akkaEsbSystem;
	}

	public void setAkkaEsbSystem(AkkaEsbSystem akkaEsbSystem) {
		this.akkaEsbSystem = akkaEsbSystem;
	}

}
