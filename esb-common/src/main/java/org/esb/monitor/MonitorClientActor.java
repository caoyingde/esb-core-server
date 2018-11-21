package org.esb.monitor;

import org.apache.log4j.Logger;
import org.esb.akka.AkkaEsbSystem;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.contrib.pattern.DistributedPubSubExtension;
import akka.contrib.pattern.DistributedPubSubMediator;

/**
 * @description: 监控客户端角色
 * @author: Andy.Cao
 * @create: 2018-11-21
 **/
public class MonitorClientActor extends UntypedActor {
    private Logger logger = Logger.getLogger(MonitorClientActor.class);

    private ActorRef mediator = DistributedPubSubExtension.get(
            getContext().system()).mediator();

    public MonitorClientActor(AkkaEsbSystem akkaEsbSystem) {
        mediator.tell(new DistributedPubSubMediator.Subscribe(
                MonitorEvent.MONITOR_PASSIVE_EVENT_TOPIC, getSelf()), getSelf());
        logger.info("Start monitoring the client service successfully!");
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof MonitorEvent.ConsumerCallFailureEvent) {
            MonitorEvent.ConsumerCallFailureEvent event = (MonitorEvent.ConsumerCallFailureEvent) message;
            mediator.tell(new DistributedPubSubMediator.Publish(
                    MonitorEvent.MONITOR_EVENT_TOPIC, event), getSelf());
        } else if (message instanceof MonitorEvent.ProviderBeCalledCount) {
            MonitorEvent.ProviderBeCalledCount event = (MonitorEvent.ProviderBeCalledCount) message;
            mediator.tell(new DistributedPubSubMediator.Publish(
                    MonitorEvent.MONITOR_EVENT_TOPIC, event), getSelf());
        } else if (message instanceof MonitorEvent.NodeSeedJionEvent) {
            MonitorEvent.NodeSeedJionEvent event = (MonitorEvent.NodeSeedJionEvent) message;
            mediator.tell(new DistributedPubSubMediator.Publish(
                    MonitorEvent.MONITOR_EVENT_TOPIC, event), getSelf());
        } else if (message instanceof MonitorEvent.NodeSeedLeaveEvent) {
            MonitorEvent.NodeSeedLeaveEvent event = (MonitorEvent.NodeSeedLeaveEvent) message;
            mediator.tell(new DistributedPubSubMediator.Publish(
                    MonitorEvent.MONITOR_EVENT_TOPIC, event), getSelf());
        } else if (message instanceof MonitorEvent.NodeSeedAllEvent) {
            MonitorEvent.NodeSeedAllEvent event = (MonitorEvent.NodeSeedAllEvent) message;
            mediator.tell(new DistributedPubSubMediator.Publish(
                    MonitorEvent.MONITOR_EVENT_TOPIC, event), getSelf());
        } else {
            unhandled(message);
        }
    }

}
