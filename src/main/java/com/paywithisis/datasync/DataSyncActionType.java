package com.paywithisis.datasync;

public enum DataSyncActionType {
    ADD("ADD"),
    UPDATE("UPDATE"),
    DELETE("DELETE");
    private final String actionName;

    private DataSyncActionType(String actionName) {
        this.actionName = actionName;
    }

    public String getActionName() {
        return actionName;
    }
    
}
