package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.esb.akka.AkkaEsbSystem;
import org.esb.server.Server;
import org.esb.server.socket.SocketException;
import org.esb.server.socket.SocketLisenter;
import org.esb.server.socket.SocketServer;
import org.esb.server.socket.SocketType;
import org.esb.server.socket.tcp.SocketTcpLisenter;

import akka.actor.ActorRef;
import akka.io.Tcp.Connected;
import akka.util.ByteString;

@Server
@Component
public class SocketServerTest extends SocketServer {

	@Autowired
	public SocketServerTest(AkkaEsbSystem akkaEsbSystem) throws SocketException {
		super(akkaEsbSystem);
	}

	@Override
	public String getHost() {
		return "0.0.0.0";
	}

	@Override
	public int getPort() {
		return 5002;
	}

	@Override
	public SocketLisenter getSocketLisenter() {
		return new SocketTcpLisenter() {
			@Override
			public void receive(ByteString data, ActorRef sender) {
				System.out.println(data.utf8String());
				this.write(data, sender);
			}
		};
	}

	@Override
	public SocketType getSocketType() {
		return SocketType.TCP;
	}

	@Override
	public void run() throws Exception {
		System.out.println("Sockect!!!");
	}

}
