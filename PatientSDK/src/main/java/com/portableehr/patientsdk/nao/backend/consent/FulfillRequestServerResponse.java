package com.portableehr.patientsdk.nao.backend.consent;

import android.util.Log;

import com.portableehr.sdk.network.NAO.inbound.patient.consent.AccessState;
import com.portableehr.sdk.network.NAO.responses.ScanValidationServerResponse;
import com.portableehr.sdk.network.ehrApi.EHRServerResponse;
import com.portableehr.sdk.network.gson.GSONexcludeOutbound;

import static com.portableehr.patientsdk.utils.PatientRuntimeConstants.kDefaultClassCountable;
import static com.portableehr.sdk.EHRLibRuntime.kModulePrefix;


/**
 * Created by : yvesleborg
 * Date       : 2020-01-15
 * <p>
 * Copyright Portable Ehr Inc, 2020
 */
public class FulfillRequestServerResponse extends EHRServerResponse {

    AccessState responseContent;

    public AccessState getResponseContent() {
        return responseContent;
    }

    public void setResponseContent(AccessState responseContent) {
        this.responseContent = responseContent;
    }

    public FulfillRequestServerResponse() {
        super();
        onNew();
    }


    //region Countable

    private final static String  CLASSTAG       = kModulePrefix + "." + ScanValidationServerResponse.class.getSimpleName();
    @GSONexcludeOutbound
    private              String  TAG;
    private static       int     lifeTimeInstances;
    private static       int     numberOfInstances;
    @GSONexcludeOutbound
    private              int     instanceNumber;
    @GSONexcludeOutbound
    private static       boolean classCountable = kDefaultClassCountable;

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
