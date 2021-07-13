package com.portableehr.patientsdk.nao.backend.appointment;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.portableehr.sdk.network.NAO.inbound.IBNotification;
import com.portableehr.sdk.network.ehrApi.AbstractEHRCall;
import com.portableehr.sdk.network.ehrApi.EHRRequestStatus;
import com.portableehr.sdk.network.ehrApi.EHRServerRequest;
import com.portableehr.sdk.network.gson.GSONexcludeOutbound;
import com.portableehr.sdk.network.gson.GsonFactory;
import com.portableehr.sdk.network.protocols.ICompletionHandler;

import static com.portableehr.sdk.EHRLibRuntime.kModulePrefix;

/**
 * Created by : yvesleborg
 * Date       : 2020-03-25
 * <p>
 * Copyright Portable Ehr Inc, 2020
 */
public class GetConfirmationStateCall extends AbstractEHRCall {
    private IBNotification                  responseContent;
    private ConfirmationStateServerResponse serverResponse;

    public GetConfirmationStateCall(EHRServerRequest serverRequest, ICompletionHandler completionHandler) {
        super(serverRequest, completionHandler);
        onNew();
    }

    public IBNotification getResponseContent() {
        return responseContent;
    }

    public void setResponseContent(IBNotification responseContent) {
        this.responseContent = responseContent;
    }

    @Override
    public EHRRequestStatus parse(String json) {

        GsonBuilder builder = GsonFactory.standardBuilder();
        Gson        parser  = builder.create();

        if (getVerbose()) {
            Log.d(getLogTAG(), "Received json \n" + json);
        }
        ConfirmationStateServerResponse response = parser.fromJson(json, ConfirmationStateServerResponse.class);

        if (response != null && response.getRequestStatus() != null) {
            this.setRequestStatus(response.getRequestStatus());
        }

        if (null != response) {
            this.serverResponse = response;
            this.responseContent = response.getResponseContent();
            return response.getRequestStatus();
        }

        return null;

    }

    public ConfirmationStateServerResponse getServerResponse() {
        return serverResponse;
    }


    //region Countable
    static {
        setClassCountable(false);
    }

    protected boolean getVerbose() {
        return classCountable;
    }

    private final static String  CLASSTAG = kModulePrefix + "." + GetConfirmationStateCall.class.getSimpleName();
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
