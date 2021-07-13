package com.portableehr.patientsdk.models;

import com.portableehr.sdk.network.NAO.inbound.IBMessageDistribution;

import java.util.Date;

public class MessageDistributionProgressChange {
    Date date;
    String progress;
    IBMessageDistribution messageDistribution;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public IBMessageDistribution getMessageDistribution() {
        return messageDistribution;
    }

    public void setMessageDistribution(IBMessageDistribution messageDistribution) {
        this.messageDistribution = messageDistribution;
    }
}

