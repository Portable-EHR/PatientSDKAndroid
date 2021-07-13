package com.portableehr.sdk.network.NAO.inbound.patient.notification;

import androidx.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.portableehr.sdk.network.gson.GSONexcludeOutbound;
import com.portableehr.sdk.network.gson.GsonFactory;

import java.util.Date;

import static com.portableehr.sdk.EHRLibRuntime.kModulePrefix;

/**
 * Created by : yvesleborg
 * Date       : 2019-08-01
 * <p>
 * Copyright Portable Ehr Inc, 2019
 */
public class ReplyEnvelope {

    String state;
    String subject;
    String detail;
    String from;
    String type;
    Date createdOn;
    Date lastUpdated;
    Date answerBy;
    Date answeredOn;
    Date remindOn;
    Date remindedOn;
    Date fallbackOn;
    Date fellbackOn;
    @Nullable
    AlertReply alertReply;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Date getAnswerBy() {
        return answerBy;
    }

    public void setAnswerBy(Date answerBy) {
        this.answerBy = answerBy;
    }

    public Date getAnsweredOn() {
        return answeredOn;
    }

    public void setAnsweredOn(Date answeredOn) {
        this.answeredOn = answeredOn;
    }

    public Date getRemindOn() {
        return remindOn;
    }

    public void setRemindOn(Date remindOn) {
        this.remindOn = remindOn;
    }

    public Date getRemindedOn() {
        return remindedOn;
    }

    public void setRemindedOn(Date remindedOn) {
        this.remindedOn = remindedOn;
    }

    public Date getFallbackOn() {
        return fallbackOn;
    }

    public void setFallbackOn(Date fallbackOn) {
        this.fallbackOn = fallbackOn;
    }

    public Date getFellbackOn() {
        return fellbackOn;
    }

    public void setFellbackOn(Date fellbackOn) {
        this.fellbackOn = fellbackOn;
    }

    @Nullable
    public AlertReply getAlertReply() {
        return alertReply;
    }

    public void setAlertReply(@Nullable AlertReply alertReply) {
        this.alertReply = alertReply;
    }

//region Countable

    private final static String  CLASSTAG       = kModulePrefix + "." + ReplyEnvelope.class.getSimpleName();
    @GSONexcludeOutbound
    private              String  TAG;
    private static       int     lifeTimeInstances;
    private static       int     numberOfInstances;
    @GSONexcludeOutbound
    private              int     instanceNumber;
    @GSONexcludeOutbound
    private static       boolean classCountable = false;

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        numberOfInstances--;
        if (numberOfInstances < 0) {
            Log.e(getLogTAG(), "*** You did not call onNew in your ctor(s)");
        }
        if (classCountable) {
            Log.d(getLogTAG(), "finalize  ");
        }
    }

    protected void onNew() {
        TAG = CLASSTAG;
        numberOfInstances++;
        lifeTimeInstances++;
        instanceNumber = lifeTimeInstances;
        if (classCountable) {
            Log.d(getLogTAG(), "onNew ");
        }
    }

    private String getLogLabel() {
        return Integer.toHexString(instanceNumber) + "/" + Integer.toHexString(numberOfInstances);
    }

    public static void setClassCountable(boolean isIt) {
        classCountable = isIt;
    }

    private String getLogTAG() {
        TAG = CLASSTAG + " [" + getLogLabel() + "] ";
        return TAG;
    }

    //endregion

    //region GSON helpers


    public String asJson() {
        GsonBuilder builder        = GsonFactory.standardBuilder();
        Gson        jsonSerializer = builder.create();
        String      theJson        = jsonSerializer.toJson(this, this.getClass());
        return theJson;
    }

    public static ReplyEnvelope fromJson(String json) {
        GsonBuilder   builder          = GsonFactory.standardBuilder();
        Gson          jsonDeserializer = builder.create();
        ReplyEnvelope theObject        = jsonDeserializer.fromJson(json, ReplyEnvelope.class);
        return theObject;
    }

    //endregion
}
