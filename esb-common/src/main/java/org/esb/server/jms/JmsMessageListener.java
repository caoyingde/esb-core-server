package org.esb.server.jms;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.esb.server.BaseServer;

/**
 * @author Andy.Cao
 * @date 2018-11-21
 * @deprecated Jms监听服务，通过spring的  {@link DefaultMessageListenerContainer} 来实现异步的消息监听
 */
public abstract class JmsMessageListener extends BaseServer implements MessageListener {


    private DefaultMessageListenerContainer defaultMessageListenerContainer = new DefaultMessageListenerContainer();

    @Override
    public void preRun() {
        defaultMessageListenerContainer.setConnectionFactory(getConnectionFactory());
        defaultMessageListenerContainer.setMessageListener(this);
        defaultMessageListenerContainer.setDestination(getDestination());
        defaultMessageListenerContainer.initialize();
        defaultMessageListenerContainer.start();
        System.out.println("jms listener");
    }


    @Override
    public void run() throws Exception {

    }

    @Override
    public void onMessage(Message message) {

    }

    protected abstract Destination getDestination();

    protected abstract ConnectionFactory getConnectionFactory();

    public DefaultMessageListenerContainer getDefaultMessageListenerContainer() {
        return defaultMessageListenerContainer;
    }

    @Override
    public void postStop() throws Exception {
        defaultMessageListenerContainer.stop();
    }


}
