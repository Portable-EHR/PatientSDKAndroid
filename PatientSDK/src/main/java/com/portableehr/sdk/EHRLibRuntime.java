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

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;


@SuppressWarnings("unused")
public class EHRLibRuntime {

    {
        setClassCountable(true);
    }

    //region App constants
    public static String    kAppAlias     = "patient.android";
    public static String    kAppGuid      = "27991c67-79db-4a71-a69d-9ef8737940b7";
    public static IBVersion kAppVersion   = new IBVersion("1.1.0");
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

    private static       String                        _stackKey;
    private static final HashMap<String, EHRApiServer> _stackApiHosts;
    private              Context                       context;
    private              IBDeviceInfo                  deviceInfo;
    private              EHRApiServer                  server;
    private              String                        deviceLanguage;
    private              IBAppInfo                     appInfo;

    public RestAPI api;
    static Object  instance;

    public static EHRLibRuntime getInstance() {
        if (null == instance) {
            try {
                instance = new EHRLibRuntime();
                ((EHRLibRuntime) instance).mStartTime = new Date();
                ((EHRLibRuntime) instance).api = new RestAPI();
            } catch (Exception e) {
                Log.e(CLASSTAG, "You have not properly set the runtime context.");
                e.printStackTrace();
            }
        }
        return (EHRLibRuntime) instance;
    }

    public EHRLibRuntime() {
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


    public static void initialize(String appAlias, String appGuid, IBVersion appVersion) {
        initialize(kModulePrefix, appAlias, appGuid, appVersion);
    }

    public static void initialize(String module, String appAlias, String appGuid, IBVersion appVersion) {
        kModulePrefix = module;
        setAppAlias(appAlias);
        setAppVersion(appVersion);
        setAppGuid(appGuid);
    }

    private static void setAppAlias(String appAlias) {
        kAppAlias = appAlias;
    }

    private static void setAppGuid(String appGuid) {
        kAppGuid = appGuid;
    }

    private static void setAppVersion(IBVersion appVersion) {
        kAppVersion = appVersion;
    }


    static {

        Log.d(EHRLibRuntime.class.getName(), "In static");

        EHRApiServer.productionApiServer();
        _stackApiHosts = new HashMap<>();
        _stackApiHosts.put("CA.prod", EHRApiServer.productionApiServer());
        _stackApiHosts.put("CA.staging", EHRApiServer.stagingApiServer());
        _stackApiHosts.put("CA.devhome", EHRApiServer.create("http", "10.0.1.22", 8080));
        _stackApiHosts.put("CA.devoffice", EHRApiServer.create("http", "192.168.32.32", 8080));
        _stackApiHosts.put("CA.partner", EHRApiServer.partnerApiServer());
        //***************************************************************************************
        // *   CA.prod                                                                          *
        //***************************************************************************************

//        setStackKey("CA.devhome");
        setStackKey("CA.partner");
    }

    public static String getStackKey() {
        return _stackKey;
    }

    public static EHRApiServer getCurrentServer() {
        return _stackApiHosts.get(_stackKey);
    }

    public static EHRApiServer getServerForStackKey(String stackKey) {
        return _stackApiHosts.get(stackKey);
    }

    public static EHRApiServer getOAMPserver() {
        return EHRLibRuntime.getOAMPserver(_stackKey);
    }

    public static EHRApiServer getOAMPserver(String stackKey) {
        EHRApiServer oamp = new EHRApiServer();
        switch (stackKey) {
            case "CA.devhome":
                oamp.setServerDNSname("10.0.1.22");
                oamp.setScheme("http");
                oamp.setPort(80);
                break;
            case "CA.devoffice":
                oamp.setServerDNSname("192.168.32.32");
                oamp.setScheme("http");
                oamp.setPort(80);
                break;
            case "CA.staging":
                oamp.setServerDNSname("oamp.portableehr.net");
                oamp.setScheme("https");
                oamp.setPort(443);
                break;
            case "CA.prod":
                oamp.setServerDNSname("oamp.portableehr.ca");
                oamp.setScheme("https");
                oamp.setPort(443);
                break;
            case "CA.partner":
                oamp.setServerDNSname("api.portableehr.io");
                oamp.setScheme("https");
                oamp.setPort(443);
                break;
            default:
                Log.e(EHRLibRuntime.class.getName(), String.format("*** Unknown stack key [%s] when fetching OAMP server.", _stackKey));
        }

        return oamp;
    }

    public static void setStackKey(@SuppressWarnings("SameParameterValue") String stackKey) {
        EHRApiServer _host = _stackApiHosts.get(stackKey);
        if (_host != null) {
            Log.d(EHRLibRuntime.class.getName(), String.format("Set stack key to [%s]", stackKey));
            _stackKey = stackKey;
            getInstance().setServer(getCurrentServer());
        } else {
            Log.e(EHRLibRuntime.class.getName(), String.format("Unknown host when stating stack key to [%s]", stackKey));
        }

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

    public void setContext(Context context) {
        this.context = context;
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
        request.setAppAlias(EHRLibRuntime.kAppAlias);
        request.setAppVersion(EHRLibRuntime.kAppVersion);
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
