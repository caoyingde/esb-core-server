package org.esb.server.socket;

import akka.actor.ActorRef;

/**
 * @author Andy.Cao
 * @date 2018-11-21
 * @deprecated
 */
public class SocketSession {

    private ActorRef connection;


    public ActorRef getConnection() {
        return connection;
    }

    public void setConnection(ActorRef connection) {
        this.connection = connection;
    }

}
