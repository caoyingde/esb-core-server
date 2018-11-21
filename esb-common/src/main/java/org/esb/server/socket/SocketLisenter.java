package org.esb.server.socket;

import akka.actor.ActorRef;
import akka.io.Tcp.Connected;
import akka.util.ByteString;

public interface SocketLisenter {
	
	void setHandler(ActorRef handler);
	
	void receive(ByteString data, ActorRef sender);
	
	void write(ByteString data, ActorRef sender);

	void setConnected(Connected conn);
	
}
