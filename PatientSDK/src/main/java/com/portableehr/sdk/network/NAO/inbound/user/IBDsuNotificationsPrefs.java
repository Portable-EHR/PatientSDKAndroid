package com.portableehr.sdk.network.NAO.inbound.user;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.portableehr.sdk.network.gson.GSONexcludeOutbound;
import com.portableehr.sdk.network.gson.GsonFactory;

import static com.portableehr.sdk.EHRLibRuntime.kModulePrefix;

/**
 * Created by : yvesleborg
 * Date       : 2020-12-26
 * <p>
 * Copyright Portable Ehr Inc, 2020
 */
public class IBDsuNotificationsPrefs {
    private boolean showBanner;
    private int     deleteAfterDays;

    public IBDsuNotificationsPrefs() {
        super();
        onNew();
    }

    public boolean isShowBanner() {
        return showBanner;
    }

    public void setShowBanner(boolean showBanner) {
        this.showBanner = showBanner;
    }

    public int getDeleteAfterDays() {
        return deleteAfterDays;
    }

    public void setDeleteAfterDays(int deleteAfterDays) {
        this.deleteAfterDays = deleteAfterDays;
    }

    //region GSON helpers


    public String asJson() {
        GsonBuilder builder        = GsonFactory.standardBuilder();
        Gson        jsonSerializer = builder.create();
        String      theJson        = jsonSerializer.toJson(this, this.getClass());
        return theJson;
    }

    public static IBDsuNotificationsPrefs fromJson(String json) {
        GsonBuilder             builder          = GsonFactory.standardBuilder();
        Gson                    jsonDeserializer = builder.create();
        IBDsuNotificationsPrefs theObject        = jsonDeserializer.fromJson(json, IBDsuNotificationsPrefs.class);
        return theObject;
    }

    //endregion


    //region Countable
    static {
        setClassCountable(false);
    }

    protected boolean getVerbose() {
        return classCountable;
    }

    private final static String  CLASSTAG = kModulePrefix + "." + IBDsuNotificationsPrefs.class.getSimpleName();
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
