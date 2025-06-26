package com.portableehr.sdk.network.NAO.inbound;

import java.io.Serializable;

/**
 * Created by : yvesleborg
 * Date       : 2017-04-01
 * <p>
 * Copyright Portable Ehr Inc, 2019
 */

public class IBUserInfo implements Serializable {
    private String    guid;
    private IBContact contact;
    private String    username;
    private String    language;
    private String    mobilePhone; // added to support clinic app

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }


}
