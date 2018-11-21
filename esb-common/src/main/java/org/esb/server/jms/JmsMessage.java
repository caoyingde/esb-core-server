package org.esb.server.jms;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.log4j.Logger;
import org.esb.exception.AkkaEsbException;

public class JmsMessage {

	private Logger logger = Logger.getLogger(JmsMessage.class);

	private Object message;

	private JmsMessageType messageType = JmsMessageType.OBJECT;

	private Map<String, String> properties = new HashMap<String, String>();

	public Message createMessage(Session session) {
		Message msg = null;
		if (messageType == JmsMessageType.OBJECT) {
			try {
				msg = session.createObjectMessage((Serializable) message);
			} catch (JMSException e) {
				e.printStackTrace();
				logger.error("创建Jms Object消息失败", e);
			}
		} else if (messageType == JmsMessageType.TEXT) {
			try {
				msg = session.createTextMessage(message.toString());
			} catch (JMSException e) {
				e.printStackTrace();
				logger.error("创建Jms Text消息失败", e);
			}
		}
		if (msg != null) {
			for (Iterator<String> iterator = properties.keySet().iterator(); iterator
					.hasNext();) {
				String key = iterator.next();
				try {
					msg.setStringProperty(key, properties.get(key).toString());
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		}

		return msg;
	}

	public void setMessage(Object message) {
		if (message == null) {
			throw new AkkaEsbException("Jms 消息不能为null");
		}
		this.message = message;
	}

	public void setProperty(String string, String string2) {
		// TODO Auto-generated method stub

	}

}
