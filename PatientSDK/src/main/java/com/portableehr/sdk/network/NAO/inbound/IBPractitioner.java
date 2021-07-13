package com.portableehr.sdk.network.NAO.inbound;

import com.portableehr.sdk.network.NAO.Jurisdictions.IBJuristiction;

/**
 * Created by : yvesleborg
 * Date       : 2017-03-31
 *
 * Copyright Portable Ehr Inc, 2019
 */

public class IBPractitioner {
    private IBContact      contact;
    private IBContact      userContact;
    private String         guid;
    private String         practiceNumber;
    private String         practiceType;
    private IBJuristiction jurisdiction;

    public IBContact getContact() {
        return contact;
    }

    public void setContact(IBContact contact) {
        this.contact = contact;
    }

    public IBContact getUserContact() {
        return userContact;
    }

    public void setUserContact(IBContact userContact) {
        this.userContact = userContact;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getPracticeNumber() {
        return practiceNumber;
    }

    public void setPracticeNumber(String practiceNumber) {
        this.practiceNumber = practiceNumber;
    }

    public String getPracticeType() {
        return practiceType;
    }

    public void setPracticeType(String practiceType) {
        this.practiceType = practiceType;
    }

    public IBJuristiction getJurisdiction() {
        return jurisdiction;
    }

    public void setJurisdiction(IBJuristiction jurisdiction) {
        this.jurisdiction = jurisdiction;
    }


}


