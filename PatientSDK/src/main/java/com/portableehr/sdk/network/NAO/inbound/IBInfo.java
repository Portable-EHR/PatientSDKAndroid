package com.portableehr.sdk.network.NAO.inbound;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.portableehr.sdk.network.gson.GSONexcludeOutbound;
import com.portableehr.sdk.network.gson.GsonFactory;

import static com.portableehr.sdk.EHRLibRuntime.kModulePrefix;

/**
 * Created by : yvesleborg
 * Date       : 2017-12-20
 * <p>
 * Copyright Portable Ehr Inc, 2019
 */

public class IBInfo {

    public IBAppInfo  appInfo;
    public IBUserInfo userInfo;

    public IBInfo() {
        onNew();
    }

    public IBAppInfo getAppInfo() {
        return appInfo;
    }

    public void setAppInfo(IBAppInfo appInfo) {
        this.appInfo = appInfo;
    }

    public IBUserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(IBUserInfo userInfo) {
        this.userInfo = userInfo;
    }


    //region GSON helpers


    public String asJson() {
        GsonBuilder builder        = GsonFactory.standardBuilder();
        Gson        jsonSerializer = builder.create();
        String      theJson        = jsonSerializer.toJson(this, this.getClass());
        return theJson;
    }

    public static IBInfo fromJson(String json) {
        GsonBuilder builder          = GsonFactory.standardBuilder();
        Gson        jsonDeserializer = builder.create();
        IBInfo      theObject        = jsonDeserializer.fromJson(json, IBInfo.class);
        return theObject;
    }

    //endregion


    //region Countable

    private final static String  CLASSTAG       = kModulePrefix + "." + IBInfo.class.getSimpleName();
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


}
