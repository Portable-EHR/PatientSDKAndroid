package com.portableehr.sdk.rest;

import com.portableehr.sdk.RestCallOptions;
import com.portableehr.sdk.EHRLibRuntime;
import com.portableehr.sdk.network.NAO.calls.clinic.dispensary.DispensaryDetailCall;
import com.portableehr.sdk.network.NAO.calls.clinic.dispensary.DispensarySettings;
import com.portableehr.sdk.network.NAO.inbound.IBDispensary;
import com.portableehr.sdk.network.NAO.inbound.IBUser;
import com.portableehr.sdk.network.ehrApi.EHRServerRequest;
import com.portableehr.sdk.network.protocols.ICompletionHandler;

/**
 * Created by : yvesleborg
 * Date       : 2020-08-13
 * <p>
 * Copyright Portable Ehr Inc, 2020
 */
public class DispensaryApi {
    public DispensaryApi() {
    }

    //region saveSettings
    @SuppressWarnings("unused")
    public DispensaryDetailCall getSaveSettingsCall(ICompletionHandler handler) {
        return getSaveSettingsCall(handler, RestCallOptions.defaults());
    }

    public DispensaryDetailCall getSaveSettingsCall(ICompletionHandler handler, RestCallOptions options) {
        EHRServerRequest     request = EHRLibRuntime.getInstance().getRequest(IBUser.guest(), "/app/dispensary", "saveSettings");
        DispensaryDetailCall theCall = new DispensaryDetailCall(request, handler);
        theCall.applyOptions(options);
        return theCall;
    }

    @SuppressWarnings("unused")
    public void saveSettings(IBDispensary dispensary, DispensarySettings settings, ICompletionHandler handler) {
        saveSettings(dispensary, settings, handler, RestCallOptions.defaults());
    }

    public void saveSettings(IBDispensary dispensary, DispensarySettings settings, ICompletionHandler handler, RestCallOptions options) {
        DispensaryDetailCall theCall = getSaveSettingsCall(handler, options);
        theCall.getServerRequest().setParameter("guid", dispensary.getGuid());
        theCall.getServerRequest().setParameter("dispensarySettings", settings);
        theCall.call();
    }
    //endregion

    //region Dispensary detail
    @SuppressWarnings("unused")
    public DispensaryDetailCall getDetailsCall(ICompletionHandler handler) {
        return getDetailsCall(handler, RestCallOptions.defaults());
    }

    public DispensaryDetailCall getDetailsCall(ICompletionHandler handler, RestCallOptions options) {
        EHRServerRequest     request = EHRLibRuntime.getInstance().getRequest(IBUser.guest(), "/app/dispensary", "detail");
        DispensaryDetailCall theCall = new DispensaryDetailCall(request, handler);
        theCall.applyOptions(options);
        return theCall;
    }

    @SuppressWarnings("unused")
    public void getDispensaryDetails(IBDispensary dispensary, ICompletionHandler handler) {
        getDispensaryDetails(dispensary, handler, RestCallOptions.defaults());
    }

    public void getDispensaryDetails(IBDispensary dispensary, ICompletionHandler handler, RestCallOptions options) {
        DispensaryDetailCall theCall = getDetailsCall(handler, options);
        theCall.getServerRequest().setParameter("guid", dispensary.getGuid());
        theCall.call();
    }
    //endregion


}
