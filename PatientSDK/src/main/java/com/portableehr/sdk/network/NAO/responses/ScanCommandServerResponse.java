package com.portableehr.sdk.network.NAO.responses;

import android.util.Log;

import com.portableehr.sdk.EHRLibRuntime;
import com.portableehr.sdk.network.NAO.inbound.IBScanCommandResponse;
import com.portableehr.sdk.network.ehrApi.EHRServerResponse;
import com.portableehr.sdk.network.gson.GSONexcludeOutbound;

/**
 * Created by : yvesleborg
 * Date       : 2018-02-12
 * <p>
 * Copyright Portable Ehr Inc, 2019
 */

public class ScanCommandServerResponse extends EHRServerResponse {

    private IBScanCommandResponse scanCommandResponse;

    public ScanCommandServerResponse() {
        super();
        onNew();
    }

    //region Getters/Setters
    public IBScanCommandResponse getScanCommandResponse() {
        return scanCommandResponse;
    }

    public void setScanCommandResponse(IBScanCommandResponse scanCommandResponse) {
        this.scanCommandResponse = scanCommandResponse;
    }
    //endregion


    //region Countable

    private final static String  CLASSTAG       = EHRLibRuntime.kModulePrefix + "." + ScanCommandServerResponse.class.getSimpleName();
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

}
