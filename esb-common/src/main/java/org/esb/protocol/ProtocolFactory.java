package org.esb.protocol;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.esb.akka.AkkaEsbSystem;
import org.esb.akka.EsbContext;
import org.esb.consumer.ConsumerBean;
import org.esb.provider.ProviderBean;
import org.esb.rpc.DefaultProtocol;
import org.esb.rpc.rest.RestServiceProtocol;
import org.esb.rpc.webservice.WebServiceProtocol;

/**
 * @author Andy.Cao
 * @date 2018-11-21
 * @deprecated
 */
public class ProtocolFactory {

    private AkkaEsbSystem akkaEsbSystem;

    public ProtocolFactory() {
        akkaEsbSystem = EsbContext.getAkkaEsbSystem();
        initialise();
    }

    private final Map<String, Protocol> protocols = new HashMap<String, Protocol>();

    public Protocol getProtocol(String name) {
        return protocols.get(name);
    }

    public void initialise() {
        protocols.put(WebServiceProtocol.Protocol_Name,
                new WebServiceProtocol());
        protocols.put(RestServiceProtocol.Protocol_Name,
                new RestServiceProtocol());

    }

    final public void provide(ProviderBean provider) {

        getDefaultProtocol().provide(provider);

        for (String protocolName : provider.getProtocols()) {
            Protocol protocol = akkaEsbSystem.currentProtocols().get(
                    protocolName);
            if (protocol == null) {
                protocol = getProtocol(protocolName);
                if (protocol == null) {
                    throw new ProtocolException("不存在协议" + protocolName);
                }
                akkaEsbSystem.currentProtocols().put(
                        protocol.getProtocolName(), protocol);
            }

            protocol.provide(provider);
        }
    }

    final public void consume(ConsumerBean consumer) {
        getDefaultProtocol().consume(consumer);
    }

    private Protocol getDefaultProtocol() {
        Protocol protocol = akkaEsbSystem.currentProtocols().get(
                DefaultProtocol.Protocol_Name);
        if (protocol == null) {
            protocol = new DefaultProtocol();
            akkaEsbSystem.currentProtocols().put(protocol.getProtocolName(),
                    protocol);
        }
        return protocol;
    }

    /**
     * 启动所有Protocol
     */
    public void start() {
        for (Iterator<String> iterator = protocols.keySet().iterator(); iterator.hasNext(); ) {
            String key = iterator.next();
            Protocol protocol = protocols.get(key);
            if (protocol != null) {
                protocol.start();
            }
        }
    }

}
