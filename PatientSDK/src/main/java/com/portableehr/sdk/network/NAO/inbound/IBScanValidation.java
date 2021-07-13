package com.portableehr.sdk.network.NAO.inbound;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.portableehr.sdk.network.gson.GSONexcludeOutbound;
import com.portableehr.sdk.network.gson.GsonFactory;

import static com.portableehr.sdk.EHRLibRuntime.kModulePrefix;

/**
 * Created by : yvesleborg
 * Date       : 2017-12-31
 * <p>
 * Copyright Portable Ehr Inc, 2019
 */

public class IBScanValidation {


    private String status;
    private IBDeviceInfo deviceInfo;
    private IBUser userInfo;

    IBScanValidation(){
        onNew();
    }

    // region Getters/Setters

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public IBDeviceInfo getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(IBDeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public IBUser getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(IBUser userInfo) {
        this.userInfo = userInfo;
    }


    // endregion


    //region Countable

    private final static String  CLASSTAG       = kModulePrefix + "." + IBScanValidation.class.getSimpleName();
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

    public static  void setClassCountable( boolean isIt) {
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

    public static IBScanValidation fromJson(String json) {
        GsonBuilder      builder          = GsonFactory.standardBuilder();
        Gson             jsonDeserializer = builder.create();
        IBScanValidation theObject        = jsonDeserializer.fromJson(json, IBScanValidation.class);
        return theObject;
    }

    //endregion
}
