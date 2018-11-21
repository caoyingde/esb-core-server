package org.esb.server.socket;

import java.net.InetSocketAddress;

import org.esb.akka.AkkaEsbSystem;
import org.esb.server.BaseServer;
import org.esb.server.socket.tcp.SocketTcpActor;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.io.Tcp;
import akka.io.Tcp.Connected;
import akka.io.TcpMessage;

/**
 * @author Andy.Cao
 * @date 2018-11-21
 * @deprecated
 */
public abstract class SocketServer extends BaseServer {

    public abstract String getHost();

    public abstract int getPort();

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

    public SocketServer(AkkaEsbSystem akkaEsbSystem) throws SocketException {
        this.akkaEsbSystem = akkaEsbSystem;
    }

    @Override
    public void init() {
        String host = getHost();
        int port = getPort();
        InetSocketAddress socketAddress = new InetSocketAddress(host, port);
        if (getSocketType() == SocketType.TCP) {
            final ActorRef tcp = Tcp.get(akkaEsbSystem.getSystem()).manager();
            tcp.tell(TcpMessage.bind(getActor(), socketAddress, 100),
                    getActor());
        } else if (getSocketType() == SocketType.UDP) {
            throw new SocketException("Unsupport UDP yet.");
        }
    }

    @Override
    public void run() throws Exception {

    }

}
