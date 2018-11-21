package org.esb.akka;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.esb.common.Role;
import org.esb.core.CoreServerActor;
import org.esb.monitor.MonitorClientActor;
import org.esb.monitor.MonitorCoreActor;
import org.esb.protocol.Protocol;
import org.esb.protocol.ProtocolFactory;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.cluster.Cluster;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * 
 * @author Andy.Cao
 */
public class AkkaEsbSystem implements InitializingBean {
	
	private Logger logger = Logger.getLogger(AkkaEsbSystem.class);

	public static final String DEFAULT_PROTOCOL = "akka.tcp";

	public static final String DEFAULT_SYSTEM_NAME = "AkkaEsbSystem";

	private String systemName = DEFAULT_SYSTEM_NAME;

	private String roles;

	private String host;

	private int port;
	private List<String> seedNodes;

	private ActorSystem system;

	private ActorRef monitorActor;

	private Thread thread = new Thread();

	private final Map<String, Protocol> currentProtocols = new HashMap<String, Protocol>();

	private ProtocolFactory protocolFactory = null;

	public void start() {
		thread.start();
		System.out.println(">>>>> 启动Akka System 。。。");
		Config config = ConfigFactory
				.parseString(
						"akka.actor.provider=akka.cluster.ClusterActorRefProvider")
				.withFallback(
						ConfigFactory
								.parseString("akka.remote.netty.tcp.hostname="
										+ host))
				.withFallback(
						ConfigFactory.parseString("akka.remote.netty.tcp.port="
								+ port))
				.withFallback(
						ConfigFactory
								.parseString("akka.cluster.auto-down = on"));
		if (roles != null && !"".equals(roles)) {
			config = config.withFallback(ConfigFactory
					.parseString("akka.cluster.roles = [" + roles + "]"));
		}

		String nodes = "";
		for (int i = 0; i < seedNodes.size(); i++) {
			nodes += ",\"akka.tcp://" + systemName + "@" + seedNodes.get(i)
					+ "\"";
		}
		if (nodes.length() > 0) {
			nodes = nodes.substring(1);
			config = config.withFallback(ConfigFactory
					.parseString("akka.cluster.seed-nodes = [" + nodes + "]"));
		}

		system = ActorSystem.create(systemName, config);

		// List<Address> addresses = new ArrayList<Address>();
		//
		// for (int i = 0; i < seedNodes.size(); i++) {
		// String hostPort = seedNodes.get(i);
		// String[] hosts = hostPort.split(":");
		// Address address = new Address("akka.tcp", systemName, hosts[0],
		// Integer.valueOf(hosts[1]));
		// addresses.add(address);
		// }
		// Cluster.get(system).joinSeedNodes(
		// JavaConversions.asScalaBuffer(addresses).toList());
		startSpecialActor();

		Cluster.get(system).registerOnMemberUp(new Runnable() {
			@Override
			public void run() {
				synchronized (thread) {
					logger.info(">>>>> 加入集群成功！！！");
					thread.notify();
				}
			}
		});
		synchronized (thread) {
			try {
				logger.info(">>>>> 尝试加入Akka集群中...");
				thread.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void startSpecialActor() {

		monitorActor = system.actorOf(
				Props.create(MonitorClientActor.class, this), "monitorClient");

		if (hasRole(Role.CORE)) {
			system.actorOf(Props.create(CoreServerActor.class, monitorActor),
					CoreServerActor.ActorName);
		} else if (hasRole(Role.MONITOR)) {
			system.actorOf(Props.create(MonitorCoreActor.class),
					MonitorCoreActor.ActorName);
		}
	}

	public void close() {
		Cluster.get(system).shutdown();
		system.shutdown();
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	public boolean hasRole(Role role) {
		return roles.indexOf(role.text()) >= 0;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public List<String> getSeedNodes() {
		return seedNodes;
	}

	public void setSeedNodes(List<String> seedNodes) {
		this.seedNodes = seedNodes;
	}

	public ActorSystem getSystem() {
		return system;
	}

	public void setSystem(ActorSystem system) {
		this.system = system;
	}

	public ActorRef getMonitorActor() {
		return monitorActor;
	}

	public void setMonitorActor(ActorRef monitorActor) {
		this.monitorActor = monitorActor;
	}

	public Map<String, Protocol> currentProtocols() {
		return currentProtocols;
	}

	public ProtocolFactory getProtocolFactory() {
		return protocolFactory;
	}

	public void setProtocolFactory(ProtocolFactory protocolFactory) {
		this.protocolFactory = protocolFactory;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		EsbContext.setAkkaEsbSystem(this);
		if (protocolFactory == null) {
			protocolFactory = new ProtocolFactory();
		}
	}

}
