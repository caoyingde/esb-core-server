package org.esb.server;

import org.esb.server.socket.SocketException;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

/**
 * @author Andy.Cao
 * @date 2018-11-21
 * @deprecated
 */
public abstract class BaseServer implements ServerEvent {

    private ActorRef actor;

    public void init() {
    }

    @Override
    public void postRestart() throws Exception {
    }

    @Override
    public void postStop() throws Exception {

    }

    @Override
    public void preRestart() throws Exception {

    }

    @Override
    public void preStart() throws Exception {
    }

    @Override
    public void preRun() throws Exception {

    }

    @Override
    public void postRun() throws Exception {

    }

    public Class<? extends UntypedActor> getActorClass() throws SocketException {
        return ServerActor.class;
    }

    public ActorRef getActor() {
        return actor;
    }

    public void setActor(ActorRef actor) {
        this.actor = actor;
    }

}
