package com.paywithisis.datasync;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import com.isis.wps.vo.JsonConverter;
import com.paywithisis.datasync.utils.DataSyncConstant;
import com.paywithisis.datasync.vo.DataSyncObject;


public class DataSyncTopicSender
{
    private JmsTemplate jmsTemplate;
    private String dataSyncTopicName;
    private final JsonConverter jsonBuilder = new JsonConverter();
    
    public void setJmsTemplate(final JmsTemplate jmsTemplate)
    {
        this.jmsTemplate = jmsTemplate;
    }

    public void setDataSyncTopicName(String dataSyncTopicName) {
        this.dataSyncTopicName = dataSyncTopicName;
    }


    public void sendMesage(final DataSyncObject syncObject) throws JsonGenerationException, JsonMappingException, IOException
    {
        final String syncMessageString = stringfy(syncObject);
        final MessageCreator messageCreator = new MessageCreator()
        {
            public Message createMessage(final Session session) throws JMSException
            {
                TextMessage tm = session.createTextMessage(syncMessageString);
                setMessageProperties(tm,createMessagePropertiesBaseSyncType(syncObject.getSyncType().getSubscriberName()));
                return tm;
            }
        };

        jmsTemplate.send(dataSyncTopicName, messageCreator);
    }

    private void setMessageProperties (javax.jms.Message msg, final HashMap <String, String> properties) throws JMSException {
        if (properties == null) {
            return;
        }
        for (Entry<String, String> entry : properties.entrySet()) {
            String propertyName = entry.getKey ();
            String value = entry.getValue ();
             msg.setObjectProperty(propertyName, value);
        }
    }
    
    private HashMap<String, String> createMessagePropertiesBaseSyncType(final String syncType){
        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put(DataSyncConstant.SYNC_TYPE_PROPERTY_NAME, syncType);
        return properties;
    }

    private String stringfy(final DataSyncObject param) throws JsonGenerationException, JsonMappingException, IOException
    {
        String jsonString = null;
        jsonString = jsonBuilder.stringfy(param);
        return jsonString;
    }
    
    
}

