package com.paywithisis.datasync.vo;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.paywithisis.datasync.DataSyncActionType;
import com.paywithisis.datasync.DataSyncSubscriberType;

@JsonTypeInfo(use = Id.NAME, include = As.WRAPPER_OBJECT)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
@JsonSubTypes({
        @JsonSubTypes.Type(value = GeoSync.class, name = "geoSync"),

})

public abstract class DataSyncObject implements Serializable{
    private static final long serialVersionUID = 8980458149818594856L;
    private DataSyncSubscriberType syncType;
    private DataSyncActionType action;
    private Long version;
    
    public DataSyncObject(DataSyncSubscriberType syncType) {
        super();
        this.syncType = syncType;
    }

    public DataSyncActionType getAction() {
        return action;
    }

    public void setAction(DataSyncActionType action) {
        this.action = action;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public DataSyncSubscriberType getSyncType() {
        return syncType;
    }    
    
}
