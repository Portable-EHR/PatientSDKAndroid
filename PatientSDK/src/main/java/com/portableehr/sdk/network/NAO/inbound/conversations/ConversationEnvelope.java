package com.portableehr.sdk.network.NAO.inbound.conversations;


public class ConversationEnvelope {
    private String guid;
    private String status;
    private String location;
    private String staffTittle;
    private String clientTittle;
    private String teaser;
    private String lastUpdated;
    private int unread;

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStaffTittle() {
        return staffTittle;
    }

    public void setStaffTittle(String staffTittle) {
        this.staffTittle = staffTittle;
    }

    public String getClientTittle() {
        return clientTittle;
    }

    public void setClientTittle(String clientTitle) {
        this.clientTittle = clientTitle;
    }

    public String getTeaser() {
        return teaser;
    }

    public void setTeaser(String teaser) {
        this.teaser = teaser;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }
}
