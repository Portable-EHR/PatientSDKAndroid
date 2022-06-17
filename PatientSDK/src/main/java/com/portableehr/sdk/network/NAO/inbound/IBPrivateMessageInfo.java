package com.portableehr.sdk.network.NAO.inbound;

import java.util.Date;

/**
 * Created by : yvesleborg
 * Date       : 2017-04-03
 * <p>
 * Copyright Portable Ehr Inc, 2019
 */

public class IBPrivateMessageInfo {
    private String guid;
    private String source;
    private Date   createdOn;
    private Date   seenOn;
    private Date   acknowledgedOn;
    private String author;

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getSeenOn() {
        return seenOn;
    }

    public void setSeenOn(Date seenOn) {
        this.seenOn = seenOn;
    }

    public Date getAcknowledgedOn() {
        return acknowledgedOn;
    }

    public void setAcknowledgedOn(Date acknowledgedOn) {
        this.acknowledgedOn = acknowledgedOn;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void updateWith(IBPrivateMessageInfo fresh) {
        guid = fresh.guid;
        source = fresh.source;
        createdOn = fresh.createdOn;
        seenOn = fresh.seenOn;
        acknowledgedOn = fresh.acknowledgedOn;
        author = fresh.author;
    }

}
