package com.portableehr.sdk.network.ehrApi;

import androidx.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.portableehr.sdk.EHRLibRuntime;
import com.portableehr.sdk.PehrSDKConfiguration;
import com.portableehr.sdk.network.EHRGuid;
import com.portableehr.sdk.network.NAO.inbound.IBDeviceInfo;
import com.portableehr.sdk.network.NAO.inbound.IBUser;
import com.portableehr.sdk.network.NAO.inbound.IBVersion;
import com.portableehr.sdk.network.gson.GSONexcludeOutbound;

import java.util.HashMap;

import static com.portableehr.sdk.EHRLibRuntime.kModulePrefix;
import static com.portableehr.sdk.network.gson.GsonFactory.standardBuilder;

/**
 * Created by : yvesleborg
 * Date       : 2017-03-31
 * <p>
 * Copyright Portable Ehr Inc, 2019
 */

public class EHRServerRequest {

    @GSONexcludeOutbound
    private EHRApiServer server;

    private String                  language;
    private String                  route;
    private String                  apiKey;
    private String                  command;
    private String                  trackingId;
    private String                  deviceGuid;
    private String                  appGuid;
    private String                  appAlias;
    private IBVersion               appVersion;
    private HashMap<String, Object> parameters;


    public EHRServerRequest(
            EHRApiServer server,
            IBUser user,
            IBDeviceInfo deviceInfo,
            String deviceLanguage,
            String route,
            String command) {
        onNew();
        this.setServer(server);
        this.setApiKey(user.getApiKey());
        this.setRoute(route);
        this.setCommand(command);
        this.setLanguage(deviceLanguage);
        this.setDeviceGuid(deviceInfo.getDeviceGuid());
        this.trackingId = EHRGuid.guid();
        this.parameters = new HashMap<>();

        PehrSDKConfiguration config = PehrSDKConfiguration.getInstance();
        this.appGuid = config.getAppGuid();
        this.appAlias = config.getAppAlias();
        this.appVersion = config.getAppVersion();
    }

    public EHRServerRequest(String route, String command) {
        this(
                EHRLibRuntime.getInstance().getServer(),
                EHRLibRuntime.getInstance().getUser(),
                EHRLibRuntime.getInstance().getDeviceInfo(),
                EHRLibRuntime.getInstance().getDeviceLanguage(),
                route,
                command);
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }

    public String getDeviceGuid() {
        return deviceGuid;
    }

    public void setDeviceGuid(String deviceGuid) {
        this.deviceGuid = deviceGuid;
    }

    public EHRApiServer getServer() {
        return server;
    }

    public void setServer(EHRApiServer server) {
        this.server = server;
    }

    public void setParameter(String key, Object parameter) {

        if (null == this.parameters) {
            this.parameters = new HashMap<>();
        }
        this.parameters.put(key, parameter);
    }

    public void setParameters(@Nullable HashMap<String, Object> parameters) {
        if (null != parameters) {
            this.parameters = parameters;
        } else {
            this.parameters = new HashMap<>();
        }
    }

    public void setAppGuid(String appGuid) {
        this.appGuid = appGuid;
    }

    public String getAppGuid() {
        return appGuid;
    }

    public void setAppAlias(String appAlias) {
        this.appAlias = appAlias;
    }

    public String getAppAlias() {
        return this.appAlias;
    }

    public void setAppVersion(IBVersion version) {
        this.appVersion = version;
    }

    public IBVersion getAppVersion() {
        return this.appVersion;
    }

    //region GSON helpers


    public String asJson() {
        GsonBuilder builder        = standardBuilder();
        Gson        jsonSerializer = builder.create();
        String      theJson        = jsonSerializer.toJson(this, this.getClass());
        return theJson;
    }

    public static EHRServerRequest fromJson(String json) {
        GsonBuilder      builder          = standardBuilder();
        Gson             jsonDeserializer = builder.create();
        EHRServerRequest theObject        = jsonDeserializer.fromJson(json, EHRServerRequest.class);
        return theObject;
    }

    //endregion
    //region Countable

    private final static String  CLASSTAG       = kModulePrefix + "." + EHRServerRequest.class.getSimpleName();
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
