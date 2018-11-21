package org.esb.server.socket;

import akka.actor.ActorRef;
import akka.io.Tcp.Connected;
import akka.util.ByteString;

/**
 * @author Andy.Cao
 * @date 2018-11-21
 * @deprecated
 */
public interface SocketLisenter {

    void setHandler(ActorRef handler);

    void receive(ByteString data, ActorRef sender);

    void write(ByteString data, ActorRef sender);

    void setConnected(Connected conn);

}
