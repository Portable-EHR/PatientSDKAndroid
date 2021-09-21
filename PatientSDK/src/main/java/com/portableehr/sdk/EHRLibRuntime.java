package com.portableehr.sdk;

/*
  Created by : yvesleborg
  Date       : 2017-12-28
  <p>
 Copyright Portable Ehr Inc, 2019
 */


import android.content.Context;
import android.util.Log;

import com.portableehr.sdk.models.UserModel;
import com.portableehr.sdk.models.notification.NotificationModel;
import com.portableehr.sdk.models.service.ServiceModel;
import com.portableehr.sdk.network.NAO.inbound.IBAppInfo;
import com.portableehr.sdk.network.NAO.inbound.IBDeviceInfo;
import com.portableehr.sdk.network.NAO.inbound.IBUser;
import com.portableehr.sdk.network.NAO.inbound.IBVersion;
import com.portableehr.sdk.network.ehrApi.EHRApiServer;
import com.portableehr.sdk.network.ehrApi.EHRServerRequest;
import com.portableehr.sdk.network.gson.GSONexcludeOutbound;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Properties;


@SuppressWarnings("unused")
public class EHRLibRuntime {

    {
        setClassCountable(true);
    }

    //region App constants
    public static String    kModulePrefix = "pehr.sdk.android";
    //endregion

    //region Precanned accounts : Guest
    public static final String kGuestApiKey   = "K7ICfFOwS3ELdHfAzWBhPt";
    public static final String kGuestUserGuid = "67b1c035-9d12-4bd6-9f94-df75182da183";
    //endregion

    private             Date   mStartTime;
    public static final String kEventUnauthorized         = "kEventUnauthorized";
    public static final String kEventMaintenance          = "kEventMaintenance";
    public static final String kEventNotificationsUpdated = "kEventNotificationsUpdated";
    @SuppressWarnings("unused")
    public static final String kEventUserModelUpdated     = "kEventUserModelUpdated";
    @SuppressWarnings("unused")
    public static final String kEventServiceModelUpdated  = "kEventServiceModelUpdated";
    @SuppressWarnings("deprecation")
    public static       Date   kEpochStart                = new Date("1 jan 1970");

    private              Context                       context;
    private              IBDeviceInfo                  deviceInfo;
    private              EHRApiServer                  server;
    private              String                        deviceLanguage;
    private              IBAppInfo                     appInfo;

    public RestAPI api;
    private static EHRLibRuntime  instance;

    public static EHRLibRuntime getInstance() {
        if (null == instance) {
            try {
                instance = new EHRLibRuntime();
                instance.mStartTime = new Date();
                instance.api = new RestAPI();
            } catch (Exception e) {
                Log.e(CLASSTAG, "You have not properly set the runtime context.");
                e.printStackTrace();
            }
        }
        return (EHRLibRuntime) instance;
    }

    private EHRLibRuntime() {
        String dl = Locale.getDefault().getISO3Language();
        if (dl.equals("eng")) {
            dl = "en";
        } else if (dl.startsWith("fr")) {
            dl = "fr";
        } else {
            dl = "en";
        }
        this.setDeviceLanguage(dl);
    }

    public void initialize(Context context, String appGuid, String appAlias, String appVersion, String stackKey) {
        this.context = context;
        PehrSDKConfiguration.getInstance(appGuid, appAlias, appVersion, stackKey);
        this.setServer(getCurrentServer());
    }

    public void initialize(Context context, Properties properties) {
        this.context = context;
        PehrSDKConfiguration.getInstance(properties);
        this.setServer(getCurrentServer());
    }

    public static EHRApiServer getCurrentServer() {
        return EHRLibRuntime.getServerForStackKey(PehrSDKConfiguration.getInstance().getAppStackKey());
    }

    public static EHRApiServer getServerForStackKey(String stackKey) {
        EHRApiServer api = new EHRApiServer();
        switch (stackKey) {
            case "CA.prod":
                api.setServerDNSname("api.portableehr.ca");
                api.setScheme("https");
                api.setPort(443);
                break;
            case "CA.partner":
                api.setServerDNSname("api.portableehr.io");
                api.setScheme("https");
                api.setPort(443);
                break;
            case "CA.staging":
                api.setServerDNSname("api.portableehr.net");
                api.setScheme("https");
                api.setPort(443);
                break;
            case "CA.dev":
                api.setServerDNSname("api.portableehr.dev");
                api.setScheme("https");
                api.setPort(443);
                break;
            case "CA.local":
                api.setServerDNSname("api.portableehr.local");
                api.setScheme("http");
                api.setPort(8080);
                break;
            default:
                Log.e(EHRLibRuntime.class.getName(), String.format("*** Unknown stack key [%s] when fetching API server.", stackKey));
        }

        return api;
    }

    public static EHRApiServer getOAMPserver() {
        return EHRLibRuntime.getOAMPserver(PehrSDKConfiguration.getInstance().getAppStackKey());
    }

    public static EHRApiServer getOAMPserver(String stackKey) {
        EHRApiServer oamp = new EHRApiServer();
        switch (stackKey) {
            case "CA.prod":
                oamp.setServerDNSname("oamp.portableehr.ca");
                oamp.setScheme("https");
                oamp.setPort(443);
                break;
            case "CA.partner":
                oamp.setServerDNSname("oamp.portableehr.io");
                oamp.setScheme("https");
                oamp.setPort(443);
                break;
            case "CA.staging":
                oamp.setServerDNSname("oamp.portableehr.net");
                oamp.setScheme("https");
                oamp.setPort(443);
                break;
            case "CA.dev":
                oamp.setServerDNSname("oamp.portableehr.dev");
                oamp.setScheme("https");
                oamp.setPort(443);
                break;
            case "CA.local":
                oamp.setServerDNSname("oamp.portableehr.local");
                oamp.setScheme("http");
                oamp.setPort(80);
                break;
            default:
                Log.e(EHRLibRuntime.class.getName(), String.format("*** Unknown stack key [%s] when fetching OAMP server.", stackKey));
        }

        return oamp;
    }

    public static String getCountableInstanceLogLabel(int instanceNumber, int numberOfInstances) {
        return Integer.toHexString(instanceNumber) + "/" + Integer.toHexString(numberOfInstances);
    }

    //region instance methods

    public Date getStartTime() {
        return mStartTime;
    }

    public Context getContext() {
        return context;
    }

    public UserModel getUserModel() {
        return UserModel.getInstance();
    }

    public IBUser getUser() {
        // lazy , less typing
        return UserModel.getInstance().getUser();
    }

    public IBAppInfo getAppInfo() {
        return appInfo;
    }

    public void setAppInfo(IBAppInfo appInfo) {
        this.appInfo = appInfo;
    }

    public NotificationModel getNotificationModel() {
        return NotificationModel.getInstance();
    }

    public ServiceModel getServiceModel() {
        return ServiceModel.getInstance();
    }


    public IBDeviceInfo getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(IBDeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public EHRApiServer getServer() {
        return server;
    }

    public void setServer(EHRApiServer server) {
        this.server = server;
    }

    public String getDeviceLanguage() {
        return deviceLanguage;
    }

    public void setDeviceLanguage(String deviceLanguage) {
        this.deviceLanguage = deviceLanguage;
    }

    public EHRServerRequest getRequest(String route, String command) {
        return getRequest(EHRLibRuntime.getInstance().getUser(), route, command);
    }

    public EHRServerRequest getRequest(IBUser user, String route, String command) {
        EHRApiServer server = EHRLibRuntime.getInstance().getServer();
        EHRServerRequest request = new EHRServerRequest(
                server,
                user,
                EHRLibRuntime.getInstance().getDeviceInfo(),
                EHRLibRuntime.getInstance().getDeviceLanguage(),
                route, command
        );
        return request;
    }


    //region Countable
    static {
        setClassCountable(false);
    }

    protected boolean getVerbose() {
        return classCountable;
    }

    private final static String  CLASSTAG = kModulePrefix + "." + EHRLibRuntime.class.getSimpleName();
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
