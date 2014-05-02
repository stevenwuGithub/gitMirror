package com.paywithisis.datasync.vo;

import com.paywithisis.datasync.DataSyncSubscriberType;

public class GeoSync extends DataSyncObject{

    private static final long serialVersionUID = -507586622593050265L;

    public GeoSync() {
        super(DataSyncSubscriberType.GEO_SYNC);
    }

}
