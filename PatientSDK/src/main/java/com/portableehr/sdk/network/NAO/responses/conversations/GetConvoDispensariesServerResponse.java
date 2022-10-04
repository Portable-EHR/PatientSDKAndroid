package com.portableehr.sdk.network.NAO.responses.conversations;

import android.util.Log;

import com.portableehr.sdk.EHRLibRuntime;
import com.portableehr.sdk.network.NAO.inbound.IBDispensary;
import com.portableehr.sdk.network.ehrApi.EHRServerResponse;
import com.portableehr.sdk.network.gson.GSONexcludeOutbound;

import java.util.List;


public class GetConvoDispensariesServerResponse extends EHRServerResponse {

    List<IBDispensary> responseContent;

    public List<IBDispensary> getResponseContent() {
        return responseContent;
    }

    public void setResponseContent(List<IBDispensary> responseContent) {
        this.responseContent = responseContent;
    }

    public GetConvoDispensariesServerResponse() {
        super();
        onNew();
    }


    //region Countable

    private final static String CLASSTAG = EHRLibRuntime.kModulePrefix + "." + GetConvoDispensariesServerResponse.class.getSimpleName();
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
