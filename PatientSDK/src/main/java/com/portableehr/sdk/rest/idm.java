package com.portableehr.sdk.rest;

import android.util.Log;

import com.portableehr.sdk.RestCallOptions;
import com.portableehr.sdk.EHRLibRuntime;
import com.portableehr.sdk.network.NAO.calls.clinic.dispensary.DispensaryListCall;
import com.portableehr.sdk.network.NAO.inbound.IBUser;
import com.portableehr.sdk.network.ehrApi.EHRServerRequest;
import com.portableehr.sdk.network.gson.GSONexcludeOutbound;
import com.portableehr.sdk.network.protocols.ICaller;
import com.portableehr.sdk.network.protocols.ICompletionHandler;

import static com.portableehr.sdk.EHRLibRuntime.kModulePrefix;

/**
 * Created by : yvesleborg
 * Date       : 2020-07-22
 * <p>
 * Copyright Portable Ehr Inc, 2020
 */
public class idm {

    public idm() {
    }


    @SuppressWarnings("unused")
    public void getDispensaries(boolean withStaff, final ICompletionHandler handler) {
        getDispensaries(withStaff, handler, RestCallOptions.defaults());
    }

    public void getDispensaries(boolean withStaff, final ICompletionHandler handler, RestCallOptions options) {
        EHRServerRequest request = EHRLibRuntime.getInstance().getRequest(IBUser.guest(), "/idm/dispensary", "list");
//        EHRServerRequest request;
//        request = new EHRServerRequest(
//                Runtime.getInstance().getServer(),
//                IBUser.guest(),
//                Runtime.getInstance().getDeviceInfo(),
//                Runtime.getInstance().getDeviceLanguage(),
//                "/idm/dispensary",
//                "list"
//        );

        request.setParameter("withStaff", withStaff);
        DispensaryListCall mDispensaryListCall = new DispensaryListCall(request, new ICompletionHandler() {
            @Override
            public void handleSuccess(ICaller theCall) {
                handler.handleSuccess(theCall);
            }


            @Override
            public void handleError(ICaller theCall) {
                handler.handleError(theCall);
            }

            @Override
            public void handleCancel(ICaller thCall) {
                handler.handleCancel(thCall);
            }
        });
        mDispensaryListCall.applyOptions(options);
        mDispensaryListCall.call();
    }


    //region Countable
    static {
        setClassCountable(false);
    }

    protected boolean getVerbose() {
        return classCountable;
    }

    private final static String  CLASSTAG = kModulePrefix + "." + idm.class.getSimpleName();
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
        if (getVerbose()) {
            Log.d(getLogTAG(), "finalize  ");
        }
    }

    protected void onNew() {
        TAG = CLASSTAG;
        numberOfInstances++;
        lifeTimeInstances++;
        instanceNumber = lifeTimeInstances;
        if (getVerbose()) {
            Log.d(getLogTAG(), "onNew ");
        }
    }

    private String getLogLabel() {
        return Integer.toHexString(instanceNumber) + "/" + Integer.toHexString(numberOfInstances);
    }

    public static void setClassCountable(boolean isIt) {
        classCountable = isIt;
    }

    protected String getLogTAG() {
        TAG = CLASSTAG + " [" + getLogLabel() + "] ";
        return TAG;
    }

    //endregion

}
