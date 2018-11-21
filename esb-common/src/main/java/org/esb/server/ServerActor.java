package org.esb.server;

import org.esb.akka.EsbContext;
import org.esb.server.socket.tcp.SocketTcpHandler;

import scala.Option;
import scala.concurrent.duration.FiniteDuration;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.io.Tcp.Connected;
import akka.io.TcpMessage;

public class ServerActor extends UntypedActor {

	private ActorRef monitor = EsbContext.getAkkaEsbSystem().getMonitorActor();

	private ServerEvent serverEvent;

	public ServerActor(ServerEvent serverEvent) {

		this.serverEvent = serverEvent;
		if (serverEvent instanceof Schedulable) {
			Schedulable schedulable = (Schedulable) serverEvent;
			ActorSystem system = getContext().system();
			FiniteDuration begin = schedulable.getDelay() == null ? FiniteDuration
					.Zero() : schedulable.getDelay();
			if (schedulable.getPeriod() == null) {
				system.scheduler().scheduleOnce(begin, getSelf(),
						new ServerEvent.RUN(), system.dispatcher(), getSelf());
			} else {
				system.scheduler().schedule(begin, schedulable.getPeriod(),
						getSelf(), new ServerEvent.RUN(), system.dispatcher(),
						getSelf());
			}

		} else {
			getSelf().tell(new ServerEvent.RUN(), getSelf());
		}
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof ServerEvent.RUN) {
			System.out.println("RUN!!!");
			serverEvent.preRun();
			serverEvent.run();
			serverEvent.postRun();
		}
	}

	@Override
	public void postRestart(Throwable reason) throws Exception {
		serverEvent.postRestart();
		super.postRestart(reason);
	}

	@Override
	public void postStop() throws Exception {
		serverEvent.postStop();
		super.postStop();
	}

	@Override
	public void preRestart(Throwable reason, Option<Object> message)
			throws Exception {
		serverEvent.preRestart();
		super.preRestart(reason, message);
	}

	@Override
	public void preStart() throws Exception {
		serverEvent.preStart();
		super.preStart();
	}

}
