package com.portableehr.sdk.network.NAO.calls;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.portableehr.sdk.network.NAO.inbound.IBPrivateMessage;
import com.portableehr.sdk.network.NAO.responses.PrivateMessageResponse;
import com.portableehr.sdk.network.ehrApi.AbstractEHRCall;
import com.portableehr.sdk.network.ehrApi.EHRRequestStatus;
import com.portableehr.sdk.network.ehrApi.EHRServerRequest;
import com.portableehr.sdk.network.gson.GSONexcludeOutbound;
import com.portableehr.sdk.network.gson.GsonFactory;
import com.portableehr.sdk.network.protocols.ICompletionHandler;

import static com.portableehr.sdk.EHRLibRuntime.kModulePrefix;

/**
 * Created by : yvesleborg
 * Date       : 2017-06-14
 * <p>
 * Copyright Portable Ehr Inc, 2019
 */

public class PrivateMessageAcknowledgeCall extends AbstractEHRCall {

    IBPrivateMessage responseContent;

    public PrivateMessageAcknowledgeCall(EHRServerRequest serverRequest, ICompletionHandler completionHandler) {
        super(serverRequest, completionHandler);
        onNew();
    }

    public IBPrivateMessage getResponseContent() {
        return responseContent;
    }

    public void setResponseContent(IBPrivateMessage responseContent) {
        this.responseContent = responseContent;
    }

    @Override
    public EHRRequestStatus parse(String json) {

        GsonBuilder            builder  = GsonFactory.standardBuilder();
        Gson                   parser   = builder.create();
        PrivateMessageResponse response = parser.fromJson(json, PrivateMessageResponse.class);

        this.responseContent = response.getPrivateMessage();
        return response.getRequestStatus();

    }


    //region Countable

    private final static String  CLASSTAG       = kModulePrefix + "." + PrivateMessageAcknowledgeCall.class.getSimpleName();
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
