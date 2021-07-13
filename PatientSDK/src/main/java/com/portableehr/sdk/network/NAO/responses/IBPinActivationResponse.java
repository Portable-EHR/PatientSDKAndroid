package com.portableehr.sdk.network.NAO.responses;

import android.util.Log;

import com.portableehr.sdk.EHRLibRuntime;
import com.portableehr.sdk.network.NAO.inbound.IBDeviceInfo;
import com.portableehr.sdk.network.NAO.inbound.IBUser;
import com.portableehr.sdk.network.gson.GSONexcludeOutbound;

/**
 * Created by : yvesleborg
 * Date       : 2017-04-14
 * <p>
 * Copyright Portable Ehr Inc, 2019
 */

public class IBPinActivationResponse {
    {
        setClassCountable(false);
    }

    private String       status;
    private IBDeviceInfo deviceInfo;
    private IBUser       userInfo;

    IBPinActivationResponse() {
        onNew();
    }

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


    //region Countable

    private final static String  CLASSTAG       = EHRLibRuntime.kModulePrefix + "." + IBPinActivationResponse.class.getSimpleName();
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
