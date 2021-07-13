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
public class AccessOffer {

    String guid;
    @Nullable
    String requestGuid;
    IBUserInfo offeredBy;
    @Nullable
    IBUserInfo offeredTo;
    FulfillmentStateEnum  fulfillmentState;
    FulfillmentMethodEnum fulfillmentMethod;
    boolean               persist;
    Date                  offeredOn;
    Date                  expiresOn;
    @Nullable
    Date seenOn;
    @Nullable
    Date fulfilledOn;
    @Nullable
    Date until;
    String           resourceGuid;
    ResourceKindEnum resourceKind;
    @Nullable
    String qrCode;


    public AccessOffer() {
        super();
        onNew();
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    @Nullable
    public String getRequestGuid() {
        return requestGuid;
    }

    public void setRequestGuid(@Nullable String requestGuid) {
        this.requestGuid = requestGuid;
    }

    public IBUserInfo getOfferedBy() {
        return offeredBy;
    }

    public void setOfferedBy(IBUserInfo offeredBy) {
        this.offeredBy = offeredBy;
    }

    @Nullable
    public IBUserInfo getOfferedTo() {
        return offeredTo;
    }

    public void setOfferedTo(@Nullable IBUserInfo offeredTo) {
        this.offeredTo = offeredTo;
    }

    public FulfillmentStateEnum getFulfillmentState() {
        return fulfillmentState;
    }

    public void setFulfillmentState(FulfillmentStateEnum fulfillmentState) {
        this.fulfillmentState = fulfillmentState;
    }

    public FulfillmentMethodEnum getFulfillmentMethod() {
        return fulfillmentMethod;
    }

    public void setFulfillmentMethod(FulfillmentMethodEnum fulfillmentMethod) {
        this.fulfillmentMethod = fulfillmentMethod;
    }

    public boolean isPersist() {
        return persist;
    }

    public void setPersist(boolean persist) {
        this.persist = persist;
    }

    public Date getOfferedOn() {
        return offeredOn;
    }

    public void setOfferedOn(Date offeredOn) {
        this.offeredOn = offeredOn;
    }

    public Date getExpiresOn() {
        return expiresOn;
    }

    public void setExpiresOn(Date expiresOn) {
        this.expiresOn = expiresOn;
    }

    @Nullable
    public Date getSeenOn() {
        return seenOn;
    }

    public void setSeenOn(@Nullable Date seenOn) {
        this.seenOn = seenOn;
    }

    @Nullable
    public Date getFulfilledOn() {
        return fulfilledOn;
    }

    public void setFulfilledOn(@Nullable Date fulfilledOn) {
        this.fulfilledOn = fulfilledOn;
    }

    @Nullable
    public Date getUntil() {
        return until;
    }

    public void setUntil(@Nullable Date until) {
        this.until = until;
    }

    public String getResourceGuid() {
        return resourceGuid;
    }

    public void setResourceGuid(String resourceGuid) {
        this.resourceGuid = resourceGuid;
    }

    public ResourceKindEnum getResourceKind() {
        return resourceKind;
    }

    public void setResourceKind(ResourceKindEnum resourceKind) {
        this.resourceKind = resourceKind;
    }

    @Nullable
    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(@Nullable String qrCode) {
        this.qrCode = qrCode;
    }

    //region Countable
    static {
        setClassCountable(false);
    }

    private final static String  CLASSTAG = EHRLibRuntime.kModulePrefix + "." + AccessOffer.class.getSimpleName();
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
