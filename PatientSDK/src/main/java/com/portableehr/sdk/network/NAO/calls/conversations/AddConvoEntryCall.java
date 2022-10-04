package com.portableehr.sdk.network.NAO.calls.conversations;

import static com.portableehr.sdk.EHRLibRuntime.kModulePrefix;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.portableehr.sdk.network.NAO.inbound.conversations.ConversationEntry;
import com.portableehr.sdk.network.NAO.responses.conversations.AddConvoEntryServerResponse;
import com.portableehr.sdk.network.ehrApi.AbstractEHRCall;
import com.portableehr.sdk.network.ehrApi.EHRRequestStatus;
import com.portableehr.sdk.network.ehrApi.EHRServerRequest;
import com.portableehr.sdk.network.gson.GSONexcludeOutbound;
import com.portableehr.sdk.network.gson.GsonFactory;
import com.portableehr.sdk.network.protocols.ICompletionHandler;


public class AddConvoEntryCall extends AbstractEHRCall {

    private ConversationEntry responseContent;

    public AddConvoEntryCall(EHRServerRequest serverRequest, ICompletionHandler completionHandler) {
        super(serverRequest, completionHandler);
        onNew();
    }

    public ConversationEntry getResponseContent() {
        return responseContent;
    }

    public void setResponseContent(ConversationEntry responseContent) {
        this.responseContent = responseContent;
    }

    @Override
    public EHRRequestStatus parse(String json) {

        GsonBuilder builder = GsonFactory.standardBuilder();
        Gson parser = builder.create();

        AddConvoEntryServerResponse response = parser.fromJson(json, AddConvoEntryServerResponse.class);

        if (response != null && response.getRequestStatus() != null) {
            this.setRequestStatus(response.getRequestStatus());
        }

        if (null != response) {
            this.responseContent = response.getResponseContent();
            return response.getRequestStatus();
        }

        return null;
    }


    //region Countable

    private final static String CLASSTAG = kModulePrefix + "." + AddConvoEntryCall.class.getSimpleName();
    @GSONexcludeOutbound
    private String TAG;
    private static int lifeTimeInstances;
    private static int numberOfInstances;
    @GSONexcludeOutbound
    private int instanceNumber;
    @GSONexcludeOutbound
    private static boolean classCountable = false;

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
