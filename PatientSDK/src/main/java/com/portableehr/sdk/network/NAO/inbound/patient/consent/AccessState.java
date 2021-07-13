package com.portableehr.sdk.network.NAO.inbound.patient.consent;

import androidx.annotation.Nullable;
import android.util.Log;

import com.portableehr.sdk.EHRLibRuntime;
import com.portableehr.sdk.network.gson.GSONexcludeOutbound;

/**
 * Created by : yvesleborg
 * Date       : 2020-01-15
 * <p>
 * Copyright Portable Ehr Inc, 2020
 */
public class AccessState {

    @Nullable AccessOffer offer;
    @Nullable AccessRequest request;

    public AccessState(){
        super();
        onNew();
    }

    @Nullable
    public AccessOffer getOffer() {
        return offer;
    }

    public void setOffer(@Nullable AccessOffer offer) {
        this.offer = offer;
    }

    @Nullable
    public AccessRequest getRequest() {
        return request;
    }

    public void setRequest(@Nullable AccessRequest request) {
        this.request = request;
    }

    //region Countable
    static {
        setClassCountable(false);
    }

    private final static String  CLASSTAG = EHRLibRuntime.kModulePrefix + "." + AccessState.class.getSimpleName();
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

    public static  void setClassCountable( boolean isIt) {
        classCountable = isIt;
    }

    private String getLogTAG() {
        TAG = CLASSTAG + " [" + getLogLabel() + "] ";
        return TAG;
    }

    /* endregion */
}
