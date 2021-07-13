package com.portableehr.sdk.network.NAO.calls.clinic;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.portableehr.sdk.network.NAO.inbound.clinic.IBWtfList;
import com.portableehr.sdk.network.NAO.responses.clinic.WtfListResponse;
import com.portableehr.sdk.network.ehrApi.AbstractEHRCall;
import com.portableehr.sdk.network.ehrApi.EHRRequestStatus;
import com.portableehr.sdk.network.ehrApi.EHRServerRequest;
import com.portableehr.sdk.network.gson.GSONexcludeOutbound;
import com.portableehr.sdk.network.gson.GsonFactory;
import com.portableehr.sdk.network.protocols.ICompletionHandler;

import static com.portableehr.sdk.EHRLibRuntime.kModulePrefix;

/**
 * Created by : yvesleborg
 * Date       : 2019-04-04
 * <p>
 * Copyright Portable Ehr Inc, 2019
 */
public class WtfListCall extends AbstractEHRCall {

    private IBWtfList responseContent;

    @Override
    public EHRRequestStatus parse(String json) {
        GsonBuilder builder = GsonFactory.standardBuilder();
        Gson        parser  = builder.create();

        WtfListResponse response = parser.fromJson(json, WtfListResponse.class);

        if (response != null && response.getRequestStatus() != null) {
            this.setRequestStatus(response.getRequestStatus());
        }

        if (null != response) {
            this.responseContent = response.getResponseContent();
            return response.getRequestStatus();
        }

        return null;
    }

    public WtfListCall(EHRServerRequest serverRequest, ICompletionHandler completionHandler) {
        super(serverRequest, completionHandler);
        onNew();
    }

    public IBWtfList getResponseContent() {
        return responseContent;
    }

    public void setResponseContent(IBWtfList responseContent) {
        this.responseContent = responseContent;
    }

    //region Countable

    private final static String  CLASSTAG       = kModulePrefix + "." + WtfListCall.class.getSimpleName();
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
