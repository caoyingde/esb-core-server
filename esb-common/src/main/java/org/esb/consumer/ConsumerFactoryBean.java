package org.esb.consumer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.esb.akka.AkkaEsbSystem;
import org.esb.common.Role;
import org.esb.rpc.DefaultTimeOutHandler;
import org.esb.rpc.RpcBeanProxy;
import org.esb.rpc.TimeOutHandler;

import akka.actor.ActorRef;
import akka.cluster.routing.AdaptiveLoadBalancingGroup;
import akka.cluster.routing.ClusterRouterGroup;
import akka.cluster.routing.ClusterRouterGroupSettings;
import akka.cluster.routing.HeapMetricsSelector;

public class ConsumerFactoryBean implements BeanFactoryPostProcessor {

	private Logger log = Logger.getLogger(ConsumerFactoryBean.class);

	private static final String RESOURCE_PATTERN = "/**/*.class";
	private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

	private AkkaEsbSystem akkaEsbSystem;

	private String[] packagesToScan;

	private ConfigurableListableBeanFactory configurableListableBeanFactory;

	private int lookUpWaitTime = 1;

	private TimeOutHandler timeOutHandler;

	public AkkaEsbSystem getAkkaEsbSystem() {
		return akkaEsbSystem;
	}

	public void setAkkaEsbSystem(AkkaEsbSystem akkaEsbSystem) {

		this.akkaEsbSystem = akkaEsbSystem;
	}

	private ActorRef getRemoteActor(Class<?> clazz) {
		int totalInstances = 100;
		Iterable<String> routeesPaths = Arrays.asList("/user/"
				+ clazz.getName());
		boolean allowLocalRoutees = false;
		ClusterRouterGroup clusterRouterGroup = new ClusterRouterGroup(
				new AdaptiveLoadBalancingGroup(
						HeapMetricsSelector.getInstance(),
						Collections.<String> emptyList()),
				new ClusterRouterGroupSettings(totalInstances, routeesPaths,
						allowLocalRoutees, Role.PROVIDER.text()));
		ActorRef remoteActor = akkaEsbSystem.getSystem().actorOf(
				clusterRouterGroup.props());
		return remoteActor;
	}

	public String[] getPackagesToScan() {
		return packagesToScan;
	}

	public void setPackagesToScan(String... packagesToScan) {
		this.packagesToScan = packagesToScan;
	}

	public int getLookUpWaitTime() {
		return lookUpWaitTime;
	}

	public void setLookUpWaitTime(int lookUpWaitTime) {
		log.info("Change ClusterRouterGroup Lookup Wait Time to "
				+ lookUpWaitTime + "s");
		this.lookUpWaitTime = lookUpWaitTime;
	}

	@Override
	public void postProcessBeanFactory(
			ConfigurableListableBeanFactory beanFactory) throws BeansException {
		this.configurableListableBeanFactory = beanFactory;

		if (configurableListableBeanFactory
				.containsBean(TimeOutHandler.DEFAULT_BEAN_NAME)) {
			timeOutHandler = configurableListableBeanFactory
					.getBean(TimeOutHandler.class);
		} else {
			timeOutHandler = new DefaultTimeOutHandler();
		}

		List<Class<?>> interfaceClasses = scanPackages(packagesToScan);
		try {
			Thread.sleep(lookUpWaitTime * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		for (Class<?> interfac : interfaceClasses) {
			log.info("Consumer [" + interfac.getName()+"]");
			Object o = new RpcBeanProxy().proxy(getRemoteActor(interfac),
					interfac, timeOutHandler, akkaEsbSystem);
			configurableListableBeanFactory.registerSingleton(
					interfac.getName(), o);

			ConsumerBean consumer = new ConsumerBean(interfac);

			akkaEsbSystem.getProtocolFactory().consume(consumer);
		}

	}

	/**
	 * 扫描package下面的所有interface
	 * 
	 * @param packagesToScan
	 * @return
	 */
	private List<Class<?>> scanPackages(String... packagesToScan) {
		List<Class<?>> interfaceClasses = new ArrayList<Class<?>>();
		for (String pkg : packagesToScan) {
			String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
					+ ClassUtils.convertClassNameToResourcePath(pkg)
					+ RESOURCE_PATTERN;
			try {
				Resource[] resources = resourcePatternResolver
						.getResources(pattern);
				MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(
						this.resourcePatternResolver);
				for (Resource resource : resources) {
					if (resource.isReadable()) {
						MetadataReader reader = readerFactory
								.getMetadataReader(resource);
						String className = reader.getClassMetadata()
								.getClassName();
						Class<?> clz = Class.forName(className);
						if (clz.isInterface()) {
							interfaceClasses.add(clz);
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return interfaceClasses;
	}

}
