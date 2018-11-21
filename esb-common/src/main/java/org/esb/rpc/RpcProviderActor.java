package org.esb.rpc;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.esb.akka.EsbContext;
import org.esb.monitor.MonitorEvent;
import org.esb.provider.ProviderBean;
import org.esb.rpc.RpcEvent.CallMethod;

import scala.concurrent.duration.Duration;
import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import com.google.common.util.concurrent.AtomicLongMap;

/**
 * @author Andy.Cao
 * @date 2018-11-21
 * @deprecated
 */
public class RpcProviderActor extends UntypedActor {

    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    private Object target;
    final AtomicLongMap<String> counter = AtomicLongMap.create();
    private ActorRef monitorActor;
    private String interfacee;

    public RpcProviderActor(ProviderBean provider) {
        this.target = provider.getImplementor();
        this.interfacee = provider.getInterfaze().getName();
        this.monitorActor = EsbContext.getAkkaEsbSystem().getMonitorActor();
        getContext()
                .system()
                .scheduler()
                .schedule(Duration.create(1, TimeUnit.SECONDS),
                        Duration.create(10, TimeUnit.SECONDS), getSelf(),
                        new RpcEvent.CallCount(),
                        getContext().system().dispatcher(), null);
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof RpcEvent.CallMethod) {
            CallMethod event = (CallMethod) message;
            counter.getAndIncrement(event.getMethodName());
            Object[] params = event.getParams();
            List<Class<?>> paraTypes = new ArrayList<Class<?>>();
            Class<?>[] paramerTypes = new Class<?>[]{};
            if (params != null) {
                for (Object param : params) {
                    paraTypes.add(param.getClass());
                }
            }
            Method method = target.getClass().getMethod(event.getMethodName(),
                    paraTypes.toArray(paramerTypes));
            Object o = method.invoke(target, params);
            getSender().tell(o, getSelf());
        } else if (message instanceof RpcEvent.CallCount) {
            for (Iterator<String> iterator = counter.asMap().keySet()
                    .iterator(); iterator.hasNext(); ) {
                String key = iterator.next();
                log.info("Be Called Count in 10 Seconds : {}={}", key,
                        counter.get(key));
            }
            Map<String, Long> map = new HashMap<String, Long>();
            map.putAll(counter.asMap());
            counter.clear();
            monitorActor.tell(new MonitorEvent.ProviderBeCalledCount(
                    interfacee, map), getSelf());

        }
    }

}
