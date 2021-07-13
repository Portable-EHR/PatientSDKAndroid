package com.portableehr.sdk.network.NAO.inbound;

import java.util.Date;

/**
 * Created by : yvesleborg
 * Date       : 2017-04-04
 *
 * Copyright Portable Ehr Inc, 2019
 */

public class IBMessageDistribution {
    private String role;
    private IBUserInfo to;
    private String guid;
    private String status;
    private String progress;
    private boolean confidential;
    private boolean mustAck;
    private Date seeBefore;
    private Date ackedOn;
    private Date seenOn;
    private Date archivedOn;
    private Date lastUpdated;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public IBUserInfo getTo() {
        return to;
    }

    public void setTo(IBUserInfo to) {
        this.to = to;
    }

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

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public boolean isConfidential() {
        return confidential;
    }

    public void setConfidential(boolean confidential) {
        this.confidential = confidential;
    }

    public boolean isMustAck() {
        return mustAck;
    }

    public void setMustAck(boolean mustAck) {
        this.mustAck = mustAck;
    }

    public Date getSeeBefore() {
        return seeBefore;
    }

    public void setSeeBefore(Date seeBefore) {
        this.seeBefore = seeBefore;
    }

    public Date getAckedOn() {
        return ackedOn;
    }

    public void setAckedOn(Date ackedOn) {
        this.ackedOn = ackedOn;
    }

    public Date getSeenOn() {
        return seenOn;
    }

    public void setSeenOn(Date seenOn) {
        this.seenOn = seenOn;
    }

    public Date getArchivedOn() {
        return archivedOn;
    }

    public void setArchivedOn(Date archivedOn) {
        this.archivedOn = archivedOn;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public boolean isSeen() {
        return (null != seenOn);
    }

    public boolean isAcked() {
        return (null != ackedOn);
    }

    public boolean isLateSeing() {
        if (!hasDeadline()) return false;
        Date today = new Date();
        return (today.compareTo(seeBefore) > 0);
    }

    public boolean shouldAck() {
        return !isAcked() && mustAck;
    }

    public boolean hasDeadline() {
        return (null != this.seeBefore);
    }
}
