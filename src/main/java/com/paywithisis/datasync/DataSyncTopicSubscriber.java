package com.paywithisis.datasync;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.isis.exception.BusinessException;
import com.isis.exception.resources.ContractExceptionResource;
import com.paywithisis.datasync.utils.DataSyncConstant;
import com.paywithisis.datasync.utils.ErrorHeaderCreator;
import com.paywithisis.datasync.utils.QueueSender;

public class DataSyncTopicSubscriber implements MessageListener{
    private static final Logger logger = LoggerFactory.getLogger(DataSyncTopicSubscriber.class);
    private QueueSender deadletterQueueSender;
    
    @Override
    public void onMessage(final Message message) {
        
        try {
            final String syncType = getSyncType(message);
            if (logger.isInfoEnabled())
            {
                logger.info("JMSMessageID= "+ message.getJMSMessageID()+" message selector: " + syncType +" enter on message");
                logMsg(message);
            }
        }
        catch (JMSException e) {
            handleUnknowException(e,message);
        }
        
    }
    
    private static String getSyncType(Message message) throws JMSException{
        String syncType = null;
        syncType = message.getStringProperty(DataSyncConstant.SYNC_TYPE_PROPERTY_NAME);
        return syncType;
    }
    private static void logMsg(Message message){
        if (message instanceof TextMessage) {
            TextMessage tm = (TextMessage) message;
            if(logger.isDebugEnabled()){
                try {
                    logger.debug("Incoming Message " +tm.getText());
                }
                catch (JMSException e) {
                    logger.error("Error during log incoming message ",e);
                }
            }
        }
    }

    private void handleUnknowException(Exception t, Message message) {
        logger.error("Error during processing data sync message", t);
        deadletterQueueSender.sendMesage(message,ErrorHeaderCreator.createJMSErrorHeader(t));
    }

    private void process(final Message message, final String syncType){
        switch (DataSyncSubscriberType.valueOf(syncType))
        {
            case CAMPAIGN_SYNC :
                break;

            case GEO_SYNC :
                break;

            case MERCHANT_SYNC :
                break;

            case OFFER_SYNC :
                break;

            default :
                throw new BusinessException(ContractExceptionResource.FAILED_TO_LOCATE_SYNC_TYPE, syncType);
        }
    }

}
