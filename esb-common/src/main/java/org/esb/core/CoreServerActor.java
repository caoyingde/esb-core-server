package org.esb.core;

import java.util.HashSet;
import java.util.Set;

import org.esb.akka.AkkaSeed;
import org.esb.common.Role;
import org.esb.monitor.MonitorCoreActor;
import org.esb.monitor.MonitorEvent;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;
import akka.cluster.ClusterEvent.MemberEvent;
import akka.cluster.ClusterEvent.MemberRemoved;
import akka.cluster.ClusterEvent.MemberUp;
import akka.cluster.ClusterEvent.UnreachableMember;
import akka.cluster.Member;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class CoreServerActor extends UntypedActor {

	public static final String ActorName = "esbCore";

	private LoggingAdapter log = Logging.getLogger(getContext().system(), this);

	private Cluster cluster = Cluster.get(getContext().system());
	private ActorRef monitorActor;

	private Set<AkkaSeed> seeds = new HashSet<AkkaSeed>();

	public CoreServerActor(ActorRef monitorActor) {
		this.monitorActor = monitorActor;
	}

	@Override
	public void preStart() throws Exception {
		cluster.subscribe(getSelf(), ClusterEvent.initialStateAsEvents(),
				MemberEvent.class, UnreachableMember.class);
	}

	@Override
	public void postStop() throws Exception {
		cluster.unsubscribe(getSelf());
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof MemberUp) {
			MemberUp mUp = (MemberUp) message;
			Member member = mUp.member();
			log.info("Member is Up: {}", member);
			AkkaSeed akkaSeed = new AkkaSeed(member);
			seeds.add(akkaSeed);
			monitorActor.tell(new MonitorEvent.NodeSeedJionEvent(akkaSeed),
					getSelf());
			if (member.hasRole(Role.MONITOR.text())) {
				getContext().actorSelection(
						member.address() + "/user/"
								+ MonitorCoreActor.ActorName).tell(
						new MonitorEvent.NodeSeedAllEvent(seeds), getSelf());
			}
		} else if (message instanceof UnreachableMember) {
			UnreachableMember mUnreachable = (UnreachableMember) message;
			log.info("Member detected as unreachable: {}",
					mUnreachable.member());
			AkkaSeed akkaSeed = new AkkaSeed(mUnreachable.member());
			seeds.remove(akkaSeed);
			monitorActor.tell(new MonitorEvent.NodeSeedLeaveEvent(akkaSeed),
					getSelf());
		} else if (message instanceof MemberRemoved) {
			MemberRemoved mRemoved = (MemberRemoved) message;
			log.info("Member is Removed: {}", mRemoved.member());
			AkkaSeed akkaSeed = new AkkaSeed(mRemoved.member());
			seeds.remove(akkaSeed);
			monitorActor.tell(new MonitorEvent.NodeSeedLeaveEvent(akkaSeed),
					getSelf());
		} else if (message instanceof MemberEvent) {

		} else {
			unhandled(message);
		}
	}

}
