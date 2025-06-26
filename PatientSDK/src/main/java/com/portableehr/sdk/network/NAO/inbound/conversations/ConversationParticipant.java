package com.portableehr.sdk.network.NAO.inbound.conversations;

import com.portableehr.patient.sdk.network.NAO.inbound.conversations.ParticipantResponders;

import java.util.ArrayList;

public class ConversationParticipant {
    private String guid;
    private String participantId;
    private String type;
    private String role;
    private String addedOn;
    private String name;
    private String firstName;
    private String middleName;
    private boolean mySelf;
    private boolean active;
    ArrayList<ParticipantResponders> responders;

    public ArrayList<ParticipantResponders> getResponders() {
        return responders;
    }

    public void setResponders(ArrayList<ParticipantResponders> responders) {
        this.responders = responders;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String id) {
        this.guid = id;
    }

    public String getParticipantId() {
        return participantId;
    }

    public void setParticipantId(String participantId) {
        this.participantId = participantId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(String addedOn) {
        this.addedOn = addedOn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public boolean isMySelf() {
        return mySelf;
    }

    public void setMySelf(boolean mySelf) {
        this.mySelf = mySelf;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
