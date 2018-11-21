package org.esb.server.socket;

import akka.actor.ActorRef;

public class SocketSession {

	private ActorRef connection;
	
	

	public ActorRef getConnection() {
		return connection;
	}

	public void setConnection(ActorRef connection) {
		this.connection = connection;
	}
	
}
