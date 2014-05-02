package com.paywithisis.datasync.utils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

import javax.jms.JMSException;
import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.isis.exception.BaseIsisException;
import com.isis.exception.BusinessException;
import com.isis.exception.ContractException;
import com.isis.exception.SystemException;

public class ErrorHeaderCreator {
    private static final Logger LOG = LoggerFactory.getLogger(ErrorHeaderCreator.class);
    
    public static final String ERROR_NAME = "EVTSRV_ERROR_NAME";
    public static final String ERROR_CATEGORY = "EVTSRV_ERROR_CATEGORY";
    public static final String ERROR_DESCRIPTION = "EVTSRV_ERROR_DESCRIPTION";
    
    public static HashMap <String, Object> createJMSErrorHeader(Exception ex) {
        if (BaseIsisException.class.isAssignableFrom(ex.getClass())) {
            return createJMSPropertiesFromExStr((String.valueOf(((BaseIsisException)ex).getExceptionResource())), getIsisErrorCategory(((BaseIsisException)ex)), ex.getMessage());
        }else{
            return createJMSPropertiesFromExStr(ex.getClass().getName(), getErrorCategory(ex), ex.getMessage());
        }
    }
    
    private static HashMap <String, Object> createJMSPropertiesFromExStr(final String name, final String category, final String description){
        LOG.info("create Error Header" + ERROR_NAME + " = " + name + " "+ ERROR_CATEGORY + " = " + category + 
                " " + ERROR_DESCRIPTION + " = " + description);
        HashMap<String, Object> jmsProperties = new HashMap<String, Object>();
        jmsProperties.put(ERROR_NAME, name);
        jmsProperties.put(ERROR_CATEGORY, category);
        jmsProperties.put(ERROR_DESCRIPTION, description);
        return jmsProperties;
    }
    
    private static String getIsisErrorCategory(BaseIsisException ex){
        if(isKindOfExceptoin(ex,BusinessException.class)){
            return BusinessException.class.getSimpleName();
        }else if(isKindOfExceptoin(ex,ContractException.class)){
            return ContractException.class.getSimpleName();
        }else if(isKindOfExceptoin(ex,SecurityException.class)){
            return SecurityException.class.getSimpleName();
        }else if(isKindOfExceptoin(ex,SystemException.class)){
            return SystemException.class.getSimpleName();
        }else{
            return "Unknown ISIS Exception";
        }
    }
    
    private static <T> boolean isKindOfExceptoin(Exception ex, Class<T> klass){
        if (klass.isAssignableFrom(ex.getClass())){
            return true;
        }else{
            return false;
        } 
    }

    private static String getErrorCategory(Exception ex){
        if(isKindOfExceptoin(ex,RuntimeException.class)){
            return RuntimeException.class.getSimpleName();
        }else if(isKindOfExceptoin(ex,IOException.class)){
            return IOException.class.getSimpleName();
        }else if(isKindOfExceptoin(ex,SQLException.class)){
            return SQLException.class.getSimpleName();
        }else if(isKindOfExceptoin(ex,JMSException.class)){
            return JMSException.class.getSimpleName();
        }else if(isKindOfExceptoin(ex,JAXBException.class)){
            return JAXBException.class.getSimpleName();
        }else{
            return "Unknown Category Exception";
        }
    }

}
