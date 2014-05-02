package com.paywithisis.datasync.utils;


import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public class QueueSender {
    private static final Logger logger = LoggerFactory.getLogger(QueueSender.class);
    
    private JmsTemplate jmsTemplate;
    private String queueName;

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public void sendMesage(final Message message) {
        MessageCreator messageCreator = new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                TextMessage tm = session.createTextMessage(((TextMessage) message).getText());
                logPublishMsg(tm);
                setMessageProperties(tm,getMessageProperties(message));
                return tm;
            }
        };

        jmsTemplate.send(queueName, messageCreator);
    }

    public void sendTextMesage(final String message) {
        MessageCreator messageCreator = new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(message);
            }
        };

        logPublishMsg(message);
        jmsTemplate.send(queueName, messageCreator);
    }

    public void sendMesage(final Message message,final HashMap <String, Object> properties) {
        MessageCreator messageCreator = new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                TextMessage tm = session.createTextMessage(((TextMessage) message).getText());
                logPublishMsg(tm);
                tm = (TextMessage)setMessageProperties(tm,getMessageProperties(message));
                tm = (TextMessage)setMessageProperties(tm, properties);
                return tm;
            }
        };
        
        jmsTemplate.send(queueName, messageCreator);
    }

    public void sendMesage(final String messageStr,final HashMap <String, Object> properties) {
        MessageCreator messageCreator = new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                TextMessage tm = session.createTextMessage(messageStr);
                setMessageProperties(tm, properties);
                return tm;
            }
        };
        logPublishMsg(messageStr);
        jmsTemplate.send(queueName, messageCreator);
    }
    
    public void sendMesageWithExpiration(final String messageStr,final HashMap <String, Object> properties, final long expirationInMiliSec) {
        MessageCreator messageCreator = new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                TextMessage tm = session.createTextMessage(messageStr);
                setMessageProperties(tm, properties);
                return tm;
            }
        };
        logPublishMsg(messageStr);
        jmsTemplate.setTimeToLive(expirationInMiliSec);
        jmsTemplate.send(queueName, messageCreator);
        jmsTemplate.setTimeToLive(0l);

    }
    private HashMap <String, Object> getMessageProperties (final Message msg) throws JMSException 
    {
       HashMap <String, Object> properties = new HashMap <String, Object> ();
       Enumeration<String> srcProperties = msg.getPropertyNames();
       while (srcProperties.hasMoreElements()) {
           String propertyName = (String) srcProperties.nextElement ();
           properties.put(propertyName, msg.getObjectProperty (propertyName));
       }
       return properties;
    }

    private Message setMessageProperties (Message msg, final HashMap <String, Object> properties) throws JMSException {
        if (properties == null) {
            return msg;
        }
        for (Entry<String, Object> entry : properties.entrySet()) {
            String propertyName = entry.getKey ();
             Object value = entry.getValue ();
             msg.setObjectProperty(propertyName, value);
        }
        return msg;
    }
    
    private static void logPublishMsg(TextMessage tm) throws JMSException{
        logPublishMsg(tm.getText());
    }
    private static void logPublishMsg(String msg){
        if(logger.isDebugEnabled())
        logger.debug("Message going to publish: " + msg);
    }

}
