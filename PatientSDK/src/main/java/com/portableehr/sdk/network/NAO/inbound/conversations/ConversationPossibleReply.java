package com.portableehr.sdk.network.NAO.inbound.conversations;

public class ConversationPossibleReply {
    private String id;
    private boolean disablesReminder;
    private String label;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isDisablesReminder() {
        return disablesReminder;
    }

    public void setDisablesReminder(boolean disablesReminder) {
        this.disablesReminder = disablesReminder;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
