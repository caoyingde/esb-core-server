package org.esb.server.socket;

import java.net.InetSocketAddress;

import org.esb.akka.AkkaEsbSystem;
import org.esb.server.BaseServer;
import org.esb.server.socket.tcp.SocketTcpActor;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.io.Tcp;
import akka.io.TcpMessage;
import akka.io.Tcp.Connected;

public abstract class SocketClient extends BaseServer {

	private String host;

	private int port;

	public abstract SocketLisenter getSocketLisenter();

	private AkkaEsbSystem akkaEsbSystem;

	@Override
	public Class<? extends UntypedActor> getActorClass() throws SocketException {
		if (getSocketType() == SocketType.TCP) {
			return SocketTcpActor.class;
		} else {
			throw new SocketException("Unsupport UDP yet.");
		}
	}

	public abstract SocketType getSocketType();

	public SocketClient(AkkaEsbSystem akkaEsbSystem) throws SocketException {
		this.akkaEsbSystem = akkaEsbSystem;
	}

	@Override
	public void init() {
		String host = getHost();
		int port = getPort();
		InetSocketAddress remote = new InetSocketAddress(host, port);
		if (getSocketType() == SocketType.TCP) {
			final ActorRef tcp = Tcp.get(akkaEsbSystem.getSystem()).manager();
			tcp.tell(TcpMessage.connect(remote), getActor());
		} else if (getSocketType() == SocketType.UDP) {
			throw new SocketException("Unsupport UDP yet.");
		}
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

}
