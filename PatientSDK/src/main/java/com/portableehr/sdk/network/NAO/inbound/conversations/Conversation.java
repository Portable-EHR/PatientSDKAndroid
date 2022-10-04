package com.portableehr.sdk.network.NAO.inbound.conversations;

import java.util.List;

public class Conversation {
    private String id;
    private String status;
    private String location;
    private String staffTittle;
    private String clientTittle;
    private List<ConversationParticipant> participants;
    private List<ConversationEntry> entries;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public void setClientTittle(String clientTittle) {
        this.clientTittle = clientTittle;
    }

    public List<ConversationParticipant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<ConversationParticipant> participants) {
        this.participants = participants;
    }

    public List<ConversationEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<ConversationEntry> entries) {
        this.entries = entries;
    }
}
