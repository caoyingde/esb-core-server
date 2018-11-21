package org.esb.monitor;

import java.util.Iterator;

import org.apache.log4j.Logger;
import org.esb.akka.AkkaSeed;
import org.esb.monitor.MonitorEvent.ConsumerCallFailureEvent;
import org.esb.monitor.MonitorEvent.NodeSeedAllEvent;
import org.esb.monitor.MonitorEvent.NodeSeedJionEvent;
import org.esb.monitor.MonitorEvent.NodeSeedLeaveEvent;
import org.esb.monitor.MonitorEvent.ProviderBeCalledCount;

import akka.actor.ActorRef;
import akka.contrib.pattern.DistributedPubSubExtension;
import akka.contrib.pattern.DistributedPubSubMediator;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.persistence.UntypedProcessor;

/**
 * @author Andy.Cao
 * @date 2018-11-21
 * @deprecated
 */
public class MonitorCoreActor extends UntypedProcessor {

    private Logger logger = Logger.getLogger(MonitorClientActor.class);

    public static final String ActorName = "monitorCore";

    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    public MonitorCoreActor() {
        ActorRef mediator = DistributedPubSubExtension.get(
                getContext().system()).mediator();
        mediator.tell(new DistributedPubSubMediator.Subscribe(
                MonitorEvent.MONITOR_EVENT_TOPIC, getSelf()), getSelf());

        logger.info("启动监控中心服务成功。。。");
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof ConsumerCallFailureEvent) {
            ConsumerCallFailureEvent event = (ConsumerCallFailureEvent) message;
            System.out.println(event);
        } else if (message instanceof ProviderBeCalledCount) {
            ProviderBeCalledCount event = (ProviderBeCalledCount) message;
            for (Iterator<String> iterator = event.getCountData().keySet()
                    .iterator(); iterator.hasNext(); ) {
                String key = iterator.next();
                log.info(event.getServerName() + " Be Called Count in 10 Seconds : {}={}", key, event
                        .getCountData().get(key));
            }
        } else if (message instanceof NodeSeedJionEvent) {
            NodeSeedJionEvent event = (NodeSeedJionEvent) message;
            System.out.println(event);
        } else if (message instanceof NodeSeedLeaveEvent) {
            NodeSeedLeaveEvent event = (NodeSeedLeaveEvent) message;
            System.out.println(event);
        } else if (message instanceof NodeSeedAllEvent) {
            NodeSeedAllEvent event = (NodeSeedAllEvent) message;
            for (Iterator<AkkaSeed> iterator = event.getAkkaSeeds().iterator(); iterator.hasNext(); ) {
                AkkaSeed seed = iterator.next();
                System.out.println(seed.toString());
            }
        } else {
            unhandled(message);
        }
    }

}
