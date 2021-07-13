package com.portableehr.sdk.network.NAO.inbound.patient.consent;

import androidx.annotation.Nullable;
import android.util.Log;

import com.portableehr.sdk.EHRLibRuntime;
import com.portableehr.sdk.network.NAO.inbound.IBUserInfo;
import com.portableehr.sdk.network.gson.GSONexcludeOutbound;

import java.util.Date;

/**
 * Created by : yvesleborg
 * Date       : 2020-01-14
 * <p>
 * Copyright Portable Ehr Inc, 2020
 */
public class AccessRequest {
    String                guid;
    IBUserInfo            requester;
    IBUserInfo            requestee;
    Date                  requestedOn;
    FulfillmentMethodEnum fulfillmentMethod;
    FulfillmentStateEnum  fulfillmentState;
    @Nullable
    Date expiresOn;
    @Nullable
    Date fulfilledOn;
    @Nullable
    Date seenOn;


    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public IBUserInfo getRequester() {
        return requester;
    }

    public void setRequester(IBUserInfo requester) {
        this.requester = requester;
    }

    public IBUserInfo getRequestee() {
        return requestee;
    }

    public void setRequestee(IBUserInfo requestee) {
        this.requestee = requestee;
    }

    public Date getRequestedOn() {
        return requestedOn;
    }

    public void setRequestedOn(Date requestedOn) {
        this.requestedOn = requestedOn;
    }

    public FulfillmentMethodEnum getFulfillmentMethod() {
        return fulfillmentMethod;
    }

    public void setFulfillmentMethod(FulfillmentMethodEnum fulfillmentMethod) {
        this.fulfillmentMethod = fulfillmentMethod;
    }

    public FulfillmentStateEnum getFulfillmentState() {
        return fulfillmentState;
    }

    public void setFulfillmentState(FulfillmentStateEnum fulfillmentState) {
        this.fulfillmentState = fulfillmentState;
    }

    @Nullable
    public Date getExpiresOn() {
        return expiresOn;
    }

    public void setExpiresOn(@Nullable Date expiresOn) {
        this.expiresOn = expiresOn;
    }

    @Nullable
    public Date getFulfilledOn() {
        return fulfilledOn;
    }

    public void setFulfilledOn(@Nullable Date fulfilledOn) {
        this.fulfilledOn = fulfilledOn;
    }

    @Nullable
    public Date getSeenOn() {
        return seenOn;
    }

    public void setSeenOn(@Nullable Date seenOn) {
        this.seenOn = seenOn;
    }

    public AccessRequest() {
        super();
        onNew();
    }

    //region Countable
    static {
        setClassCountable(false);
    }

    private final static String  CLASSTAG = EHRLibRuntime.kModulePrefix + "." + AccessRequest.class.getSimpleName();
    @GSONexcludeOutbound
    private              String  TAG;
    private static       int     lifeTimeInstances;
    private static       int     numberOfInstances;
    @GSONexcludeOutbound
    private              int     instanceNumber;
    @GSONexcludeOutbound
    private static       boolean classCountable;

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
}
