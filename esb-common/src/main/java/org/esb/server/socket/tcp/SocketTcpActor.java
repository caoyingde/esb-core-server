package org.esb.server.socket.tcp;

import org.apache.log4j.Logger;
import org.esb.server.ServerActor;
import org.esb.server.ServerEvent;
import org.esb.server.ServerEvent.RUN;
import org.esb.server.socket.SocketClient;
import org.esb.server.socket.SocketLisenter;
import org.esb.server.socket.SocketServer;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.io.Tcp.Bound;
import akka.io.Tcp.CommandFailed;
import akka.io.Tcp.Connected;
import akka.io.Tcp.ConnectionClosed;
import akka.io.Tcp.Received;
import akka.io.TcpMessage;
import akka.japi.Procedure;
import akka.util.ByteString;

public class SocketTcpActor extends ServerActor {

	private Logger logger = Logger.getLogger(SocketTcpActor.class);

	private SocketLisenter socketLisenter;

	private boolean isServer = false;

	public SocketTcpActor(ServerEvent serverEvent) {
		super(serverEvent);
		if (serverEvent instanceof SocketServer) {
			isServer = true;
			this.socketLisenter = ((SocketServer) serverEvent)
					.getSocketLisenter();
		} else if (serverEvent instanceof SocketClient) {
			this.socketLisenter = ((SocketClient) serverEvent)
					.getSocketLisenter();
		} else {
			logger.error("ServerEvent is not match SocketServer!!!");
		}
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof Bound) {
		} else if (message instanceof CommandFailed) {
			CommandFailed failed = (CommandFailed) message;
			System.out.println(failed.toString());
			getContext().stop(getSelf());
		} else if (message instanceof Connected) {
			final Connected conn = (Connected) message;
			socketLisenter.setConnected(conn);
			final ActorRef handler = getContext().actorOf(
					Props.create(SocketTcpHandler.class, socketLisenter));
			if (isServer) {
				getSender().tell(TcpMessage.register(handler), getSelf());
			} else {
				handler.tell(message, getSender());
				getSender().tell(TcpMessage.register(handler), getSelf());
				getContext().become(connected(getSender(), handler));
			}
		} else {
			super.onReceive(message);
		}
	}

	private Procedure<Object> connected(final ActorRef connection,
			final ActorRef handler) {
		return new Procedure<Object>() {
			@Override
			public void apply(Object msg) throws Exception {

				if (msg instanceof ByteString) {
					connection.tell(TcpMessage.write((ByteString) msg),
							getSelf());

				} else if (msg instanceof CommandFailed) {

				} else if (msg instanceof Received) {
					handler.tell(msg, getSelf());
				} else if (msg instanceof ConnectionClosed) {
					getContext().stop(getSelf());
				} else if (msg instanceof RUN){
					SocketTcpActor.this.onReceive(msg);
				}
			}
		};
	}

}
