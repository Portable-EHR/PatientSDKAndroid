package com.portableehr.sdk.network.NAO.inbound;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.portableehr.sdk.network.gson.GSONexcludeOutbound;
import com.portableehr.sdk.network.gson.GsonFactory;

import static com.portableehr.sdk.EHRLibRuntime.kModulePrefix;

/**
 *
 * This faces OBUserApiCredentials in back end
 *
 * Created by : yvesleborg
 * Date       : 2018-06-23
 *
 * Copyright Portable Ehr Inc, 2019
 */
public class IBUserApiCredentials {


    private String username;
    private String guid;
    private String apiKey;

    public IBUserApiCredentials() {
        onNew();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }


    //region GSON helpers


    public String asJson() {
        GsonBuilder builder        = GsonFactory.standardBuilder();
        Gson        jsonSerializer = builder.create();
        String      theJson        = jsonSerializer.toJson(this, this.getClass());
        return theJson;
    }

    public static IBUserApiCredentials fromJson(String json) {
        GsonBuilder          builder          = GsonFactory.standardBuilder();
        Gson                 jsonDeserializer = builder.create();
        IBUserApiCredentials theObject        = jsonDeserializer.fromJson(json, IBUserApiCredentials.class);
        return theObject;
    }

    //endregion


    //region Countable

    private final static String  CLASSTAG       = kModulePrefix + "." + IBUserApiCredentials.class.getSimpleName();
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

    public static  void setClassCountable( boolean isIt) {
        classCountable = isIt;
    }

    private String getLogTAG() {
        TAG = CLASSTAG + " [" + getLogLabel() + "] ";
        return TAG;
    }

    //endregion
}
