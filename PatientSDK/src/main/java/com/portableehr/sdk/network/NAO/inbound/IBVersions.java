package com.portableehr.sdk.network.NAO.inbound;

import android.util.Log;

import com.portableehr.sdk.network.gson.GSONexcludeOutbound;

import static com.portableehr.sdk.EHRLibRuntime.kModulePrefix;

/**
 * Created by : yvesleborg
 * Date       : 2017-04-12
 * <p>
 * Copyright Portable Ehr Inc, 2019
 */

public class IBVersions {

    private IBVersion api;
    private IBVersion db;
    private IBVersion appAvailable;
    private IBVersion appRequired;

    public IBVersion getApi() {
        return api;
    }

    public void setApi(IBVersion api) {
        this.api = api;
    }

    public IBVersion getDb() {
        return db;
    }

    public void setDb(IBVersion db) {
        this.db = db;
    }

    public IBVersion getAppAvailable() {
        return appAvailable;
    }

    public void setAppAvailable(IBVersion appAvailable) {
        this.appAvailable = appAvailable;
    }

    public IBVersion getAppRequired() {
        return appRequired;
    }

    public void setAppRequired(IBVersion appRequired) {
        this.appRequired = appRequired;
    }

    public IBVersions(){ onNew();}

    //region Countable

    private final static String  CLASSTAG       = kModulePrefix + "." + IBVersions.class.getSimpleName();
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

    //endregion**************************************************************************/


}
