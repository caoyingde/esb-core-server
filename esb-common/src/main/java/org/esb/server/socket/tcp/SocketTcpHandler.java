package org.esb.server.socket.tcp;

import org.esb.server.socket.SocketLisenter;

import akka.actor.UntypedActor;
import akka.io.Tcp.Connected;
import akka.io.Tcp.ConnectionClosed;
import akka.io.Tcp.Received;
import akka.util.ByteString;

public class SocketTcpHandler extends UntypedActor {

	private SocketLisenter socketLisenter;

	public SocketTcpHandler(SocketLisenter socketLisenter) {
		this.socketLisenter = socketLisenter;
		this.socketLisenter.setHandler(getSelf());
	}

	@Override
	public void onReceive(Object msg) throws Exception {
		if (msg instanceof Received) {
			final ByteString data = ((Received) msg).data();
			socketLisenter.receive(data, getSender());
		} else if (msg instanceof ConnectionClosed) {
			getContext().stop(getSelf());
		} else if (msg instanceof Connected) {
			socketLisenter.setHandler(getSender());
		}
	}

}
