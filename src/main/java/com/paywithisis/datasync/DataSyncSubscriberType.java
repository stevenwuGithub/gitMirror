package com.paywithisis.datasync;


public enum DataSyncSubscriberType {
    MERCHANT_SYNC("MERCHANT_SYNC"),
    OFFER_SYNC("OFFER_SYNC"),
    CAMPAIGN_SYNC("CAMPAIGN_SYNC"),
    GEO_SYNC("GEO_SYNC");
    private final String subscriberName;

    private DataSyncSubscriberType(String subscriberName) {
        this.subscriberName = subscriberName;
    }

    public String getSubscriberName() {
        return subscriberName;
    }

}
