package com.portableehr.patient.sdk.network.NAO.inbound.conversations;

import com.portableehr.sdk.network.NAO.inbound.IBContact;

import java.io.Serializable;

public class ParticipantResponders implements Serializable {

    IBContact contact;
    String guid;

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public IBContact getContact() {
        return contact;
    }

    public void setContact(IBContact contact) {
        this.contact = contact;
    }
}
