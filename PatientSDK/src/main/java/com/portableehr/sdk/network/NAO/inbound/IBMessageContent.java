package com.portableehr.sdk.network.NAO.inbound;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by : yvesleborg
 * Date       : 2017-04-03
 * <p>
 * Copyright Portable Ehr Inc, 2019
 */

public class IBMessageContent {

    String guid;
    String subject;
    String text;
    IBContact from;
    String status;
    boolean mustAck;
    boolean confidential;
    Date lastUpdated;
    Date createdOn;
    IBPatientInfo patient;
    IBMedia[] attachments;
    IBMessageDistribution[] distribution;

    IBMessageContent(){
    }

    /*
     *  getters and setters
     */

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public IBContact getFrom() {
        return from;
    }

    public void setFrom(IBContact from) {
        this.from = from;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isMustAck() {
        return mustAck;
    }

    public void setMustAck(boolean mustAck) {
        this.mustAck = mustAck;
    }

    public boolean isConfidential() {
        return confidential;
    }

    public void setConfidential(boolean confidential) {
        this.confidential = confidential;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public IBPatientInfo getPatient() {
        return patient;
    }

    public void setPatient(IBPatientInfo patient) {
        this.patient = patient;
    }

    public IBMedia[] getAttachments() {
        return attachments;
    }

    public void setAttachments(IBMedia[] attachments) {
        this.attachments = attachments;
    }

    public IBMessageDistribution[] getDistribution() {
        return distribution;
    }

    public void setDistribution(IBMessageDistribution[] distribution) {
        this.distribution = distribution;
    }

    /*
     * public stuff here
     */

    public IBMessageDistribution distributionForUser(IBUser user) {
        IBMessageDistribution md = null;
        for (IBMessageDistribution elem : this.getDistribution()) {
            if (null != elem.getTo()) {
                IBUserInfo to = elem.getTo();
                if (to.getGuid().equals(user.getGuid())) {
                    md = elem;
                    break;
                }
            }
        }
        return md;
    }



    public IBMessageDistribution[] getTOs() {
        ArrayList<IBMessageDistribution> list = new ArrayList<>();
        for (IBMessageDistribution elem : this.getDistribution()) {
            if (elem.getRole().equals("to")) list.add(elem);
        }
        return (IBMessageDistribution[]) list.toArray();
    }

    public IBMessageDistribution[] getCCs() {
        ArrayList<IBMessageDistribution> list = new ArrayList<>();
        for (IBMessageDistribution elem : this.getDistribution()) {
            if (elem.getRole().equals("cc")) list.add(elem);
        }
        return (IBMessageDistribution[]) list.toArray();
    }

    public boolean shouldAck(IBUser user) {
        IBMessageDistribution md = this.distributionForUser(user);
        if ((null != md) && md.getRole().equals("to")) {
            if (md.isMustAck() && (null != md.getAckedOn())) return true;
        }
        return false;
    }

    public boolean shouldSee(IBUser user) {
        IBMessageDistribution md = this.distributionForUser(user);
        if (null == md) return false;
        if (md.getRole().equals("to")) {
            if (null != md.getSeenOn()) return false;
            if (null == md.getSeeBefore()) return false;
            Date now = new Date();
            if (now.compareTo(md.getSeeBefore()) > 0) return true;
        }
        return false;
    }

    // todo : dafuk, this is same as above, check the O-c code for use ????
    public boolean lateSeing(IBUser user) {
        IBMessageDistribution md = this.distributionForUser(user);
        if (null == md) return false;
        if (md.getRole().equals("to")) {
            if (null != md.getSeenOn()) return false;
            if (null == md.getSeeBefore()) return false;
            Date now = new Date();
            if (now.compareTo(md.getSeeBefore()) <= 0) return false;
            return true;
        }
        return false;
    }

}
