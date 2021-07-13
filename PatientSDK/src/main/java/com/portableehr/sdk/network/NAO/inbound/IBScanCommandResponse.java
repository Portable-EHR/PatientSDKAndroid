package com.portableehr.sdk.network.NAO.inbound;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.portableehr.sdk.network.gson.GSONexcludeOutbound;
import com.portableehr.sdk.network.gson.GsonFactory;

import java.util.ArrayList;

import static com.portableehr.sdk.EHRLibRuntime.kModulePrefix;

/**
 * Created by : yvesleborg
 * Date       : 2018-02-12
 * <p>
 * Copyright Portable Ehr Inc, 2019
 */

public class IBScanCommandResponse {
    private IBDeviceInfo         deviceInfo;
    private IBAppInfo            appInfo;
    private IBUser               userInfo;
    private ArrayList<IBService> services;
    private String               status;

    //region getters/setters

    public IBDeviceInfo getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(IBDeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public IBAppInfo getAppInfo() {
        return appInfo;
    }

    public void setAppInfo(IBAppInfo appInfo) {
        this.appInfo = appInfo;
    }

    public IBUser getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(IBUser userInfo) {
        this.userInfo = userInfo;
    }

    public ArrayList<IBService> getServices() {
        return services;
    }

    public void setServices(ArrayList<IBService> services) {
        this.services = services;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    //endregion

    public IBScanCommandResponse(){ onNew();}

    //region Countable

    private final static String  CLASSTAG       = kModulePrefix + "." + IBScanCommandResponse.class.getSimpleName();
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

    public static IBScanCommandResponse fromJson(String json) {
        GsonBuilder           builder          = GsonFactory.standardBuilder();
        Gson                  jsonDeserializer = builder.create();
        IBScanCommandResponse theObject        = jsonDeserializer.fromJson(json, IBScanCommandResponse.class);
        return theObject;
    }

    //endregion
}
