package com.portableehr.sdk.network.NAO.inbound;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by : yvesleborg
 * Date       : 2017-03-31
 *
 * Copyright Portable Ehr Inc, 2019
 */

public class IBPatient implements Serializable {
//    private static final long serialVersionUID = 1L;

    private String     guid;
    private String     name;
    private String     firstName;
    private Date       dateOfBirth;
    private Date       dateOfDeath;
    private String     gender;
    private Date       dateRegistered;
    private Date       lastUpdated;
    private IBContact  contact;
    private IBAddress  address;
    private IBUserInfo userInfo;
    private Integer    unreadNotifications;

    public Integer getUnreadNotifications() {
        return unreadNotifications;
    }

    public void setUnreadNotifications(Integer unreadNotifications) {
        this.unreadNotifications = unreadNotifications;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
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

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Date getDateOfDeath() {
        return dateOfDeath;
    }

    public void setDateOfDeath(Date dateOfDeath) {
        this.dateOfDeath = dateOfDeath;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(Date dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public IBContact getContact() {
        return contact;
    }

    public void setContact(IBContact contact) {
        this.contact = contact;
    }

    public IBAddress getAddress() {
        return address;
    }

    public void setAddress(IBAddress address) {
        this.address = address;
    }

    public IBUserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(IBUserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
