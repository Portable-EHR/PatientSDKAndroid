package com.portableehr.sdk.rest;

import com.portableehr.sdk.RestCallOptions;
import com.portableehr.sdk.EHRLibRuntime;
import com.portableehr.sdk.network.NAO.calls.FeedCommandCall;
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
@SuppressWarnings("unused")
public class FeedApi {
    public FeedApi() {
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
    public DispensaryDetailCall getStatus(ICompletionHandler handler) {
        return getStatus(handler, RestCallOptions.defaults());
    }

    public DispensaryDetailCall getStatus(ICompletionHandler handler, RestCallOptions options) {
        EHRServerRequest     request = EHRLibRuntime.getInstance().getRequest(IBUser.guest(), "/app/feed", "status");
        DispensaryDetailCall theCall = new DispensaryDetailCall(request, handler);
        theCall.applyOptions(options);
        return theCall;
    }

    //endregion

    public void restart(String feedAlias, ICompletionHandler handler) {
        restart(feedAlias, handler, RestCallOptions.defaults());
    }

    public void restart(String feedAlias, ICompletionHandler handler, RestCallOptions options) {
        FeedCommandCall theCall = EHRLibRuntime.getInstance().api.app.getFeedCommandCall("restart", handler, options);
        theCall.getServerRequest().setParameter("feedAlias", feedAlias);
        theCall.call();

    }

    public void ping(String feedAlias, ICompletionHandler handler) {
        ping(feedAlias, handler, RestCallOptions.defaults());
    }

    public void ping(String feedAlias, ICompletionHandler handler, RestCallOptions options) {
        FeedCommandCall theCall = EHRLibRuntime.getInstance().api.app.getFeedCommandCall("ping", handler, options);
        theCall.getServerRequest().setParameter("feedAlias", feedAlias);
        theCall.call();
    }

    public void getStatus(String feedAlias, ICompletionHandler handler) {
        getStatus(feedAlias, handler, RestCallOptions.defaults());
    }

    public void getStatus(String feedAlias, ICompletionHandler handler, RestCallOptions options) {
        FeedCommandCall theCall = EHRLibRuntime.getInstance().api.app.getFeedCommandCall("status", handler, options);
        theCall.getServerRequest().setParameter("feedAlias", feedAlias);
        theCall.call();
    }
}
