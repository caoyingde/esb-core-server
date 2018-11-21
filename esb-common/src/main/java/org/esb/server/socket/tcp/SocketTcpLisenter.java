package org.esb.server.socket.tcp;

import org.esb.server.socket.SocketLisenter;

import akka.actor.ActorRef;
import akka.io.Tcp.Connected;
import akka.io.TcpMessage;
import akka.util.ByteString;

public abstract class SocketTcpLisenter implements SocketLisenter {

	private ActorRef handleActor;
	
	private Connected connected;
	
	private boolean ready;

	public void setHandler(ActorRef handler) {
		this.handleActor = handler;
	}

	public abstract void receive(ByteString data, ActorRef sender);

	public void write(ByteString data, ActorRef sender) {
		sender.tell(TcpMessage.write(data), handleActor);
	}

	public void write(ByteString data) {
		handleActor.tell(TcpMessage.write(data), handleActor);
	}

	@Override
	public void setConnected(Connected conn) {
		System.out.println("New Client:"
				+ conn.remoteAddress().getAddress().toString());
		ready = true;
	}

	public boolean isReady() {
		return ready;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}

	public Connected getConnected() {
		return connected;
	}
	
}
