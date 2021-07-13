package com.portableehr.sdk.rest;

import com.portableehr.sdk.RestCallOptions;
import com.portableehr.sdk.EHRLibRuntime;
import com.portableehr.sdk.network.NAO.calls.AppInfoCall;
import com.portableehr.sdk.network.NAO.calls.FeedCommandCall;
import com.portableehr.sdk.network.NAO.calls.PingServerCall;
import com.portableehr.sdk.network.NAO.calls.clinic.UserLoginCall;
import com.portableehr.sdk.network.NAO.calls.clinic.dispensary.DispensaryDetailCall;
import com.portableehr.sdk.network.NAO.inbound.IBUser;
import com.portableehr.sdk.network.ehrApi.EHRServerRequest;
import com.portableehr.sdk.network.protocols.ICompletionHandler;

/**
 * Created by : yvesleborg
 * Date       : 2020-07-22
 * <p>
 * Copyright Portable Ehr Inc, 2020
 */
public class app {

    @SuppressWarnings("unused")
    public static class ClinicApi {
        public static class Patient{

        }
        public ClinicApi() {
            this.patient = new Patient();
        }

        public Patient patient;

        public void getAppInfo(ICompletionHandler handler, RestCallOptions options) {
            app         clinic  = new app();
            AppInfoCall theCall = clinic.getAppInfoCall(handler, options);
            theCall.call();
        }

        @SuppressWarnings("unused")
        public void getAppInfo(ICompletionHandler handler) {
            app         clinic  = new app();
            AppInfoCall theCall = clinic.getAppInfoCall(handler, RestCallOptions.defaults());
            theCall.call();
        }

        public void getDispensaryDetail(String guid, ICompletionHandler handler) {
            getDispensaryDetail(guid, handler, RestCallOptions.defaults());
        }

        public void getDispensaryDetail(String guid, ICompletionHandler handler, RestCallOptions options) {
            app                  clinic  = new app();
            DispensaryDetailCall theCall = clinic.getDispensaryDetailCall(handler, options);
            theCall.getServerRequest().setParameter("guid", guid);
            theCall.call();
        }

        public void pingBackend(ICompletionHandler handler, RestCallOptions options) {
            app            clinic         = new app();
            PingServerCall pingServerCall = clinic.getPingBackendCall(handler, options);
            pingServerCall.call();
        }

        @SuppressWarnings("unused")
        public void pingBackend(ICompletionHandler handler) {
            pingBackend(handler, RestCallOptions.defaults());
        }
    }

    @SuppressWarnings("unused")
    public static class UserApi {
        @SuppressWarnings("unused")
        public static class AccountApi {
            //region login
            public UserLoginCall getLoginCall(ICompletionHandler handler) {
                return getLoginCall(handler, RestCallOptions.defaults());
            }

            public UserLoginCall getLoginCall(ICompletionHandler handler, RestCallOptions options) {
                EHRServerRequest request = EHRLibRuntime.getInstance().getRequest(IBUser.guest(), "/app/user/account", "login");
                UserLoginCall    theCall = new UserLoginCall(request, handler);
                theCall.applyOptions(options);
                return theCall;
            }

            public void login(String guid, String secret, ICompletionHandler handler, RestCallOptions options) {
                UserLoginCall theCall = getLoginCall(handler, options);
                theCall.getServerRequest().setParameter("guid", guid);
                theCall.getServerRequest().setParameter("method", "password");
                theCall.getServerRequest().setParameter("secret", secret);
                theCall.applyOptions(options);
                theCall.call();
            }

            public void login(String guid, String password, ICompletionHandler handler) {
                this.login(guid, password, handler, RestCallOptions.defaults());
            }

            public void accessWithPIN(String guid, String secret, ICompletionHandler handler, RestCallOptions options) {
                UserLoginCall theCall = getLoginCall(handler, options);
                theCall.getServerRequest().setParameter("guid", guid);
                theCall.getServerRequest().setParameter("method", "accessPIN");
                theCall.getServerRequest().setParameter("secret", secret);
                theCall.applyOptions(options);
                theCall.call();
            }

            public void accessWithPIN(String guid, String secret, ICompletionHandler handler) {
                this.accessWithPIN(guid, secret, handler, RestCallOptions.defaults());
            }

            //endregion
        }

        public AccountApi account = new AccountApi();


    }

    public ClinicApi       clinic;
    public NotificationApi notification;
    public DispensaryApi   dispensary;
    public FeedApi         feed;
    public UserApi         user;

    public app() {
        clinic = new ClinicApi();
        notification = new NotificationApi();
        dispensary = new DispensaryApi();
        feed = new FeedApi();
        user = new UserApi();
    }

    public AppInfoCall getAppInfoCall(ICompletionHandler handler, RestCallOptions callOptions) {

        EHRServerRequest request = EHRLibRuntime.getInstance().getRequest(IBUser.guest(), "/app/clinic/commands", "appinfo");
        AppInfoCall      theCall = new AppInfoCall(request, handler);
        theCall.applyOptions(callOptions);
        return theCall;
    }

    public AppInfoCall getAppInfoCall(ICompletionHandler handler) {

        EHRServerRequest request = EHRLibRuntime.getInstance().getRequest(IBUser.guest(), "/app/clinic/commands", "appinfo");
        AppInfoCall      theCall = new AppInfoCall(request, handler);
        theCall.applyOptions(RestCallOptions.defaults());
        return theCall;
    }

    public DispensaryDetailCall getDispensaryDetailCall(ICompletionHandler handler, RestCallOptions callOptions) {

        EHRServerRequest     request = EHRLibRuntime.getInstance().getRequest(IBUser.guest(), "/app/clinic/dispensary", "detail");
        DispensaryDetailCall theCall = new DispensaryDetailCall(request, handler);
        theCall.applyOptions(callOptions);
        return theCall;
    }

    public FeedCommandCall getFeedCommandCall(String command, ICompletionHandler handler, RestCallOptions callOptions) {

        EHRServerRequest request = EHRLibRuntime.getInstance().getRequest(
                EHRLibRuntime.getInstance().getUser(),
                "/app/feed",
                command);
        FeedCommandCall theCall = new FeedCommandCall(request, handler);
        theCall.applyOptions(callOptions);
        return theCall;
    }

    public PingServerCall getPingBackendCall(ICompletionHandler handler, RestCallOptions callOptions) {

        EHRServerRequest request = EHRLibRuntime.getInstance().getRequest(IBUser.guest(), "/app/commands", "pingServer");
        PingServerCall   theCall = new PingServerCall(request, handler);
        theCall.applyOptions(callOptions);
        return theCall;
    }

    public AppInfoCall getAppCommandCall(String command, String level, String message, ICompletionHandler handler) {

        EHRServerRequest request = EHRLibRuntime.getInstance().getRequest( "/app/commands", command);
        AppInfoCall   theCall = new AppInfoCall(request, handler);
        theCall.getServerRequest().setParameter("level", level);
        theCall.getServerRequest().setParameter("message", message);
        theCall.applyOptions(RestCallOptions.defaults());
        return theCall;
    }

}
