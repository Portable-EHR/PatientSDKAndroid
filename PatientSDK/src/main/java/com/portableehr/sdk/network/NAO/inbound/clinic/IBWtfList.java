package com.portableehr.sdk.network.NAO.inbound.clinic;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.portableehr.sdk.EHRLibRuntime;
import com.portableehr.sdk.network.gson.GSONexcludeOutbound;
import com.portableehr.sdk.network.gson.GsonFactory;

/**
 * Created by : yvesleborg
 * Date       : 2019-04-03
 * <p>
 * Copyright Portable Ehr Inc, 2019
 */
public class IBWtfList {
    int             firstIndex;
    Boolean         hasMore;
    IBWtfListItem[] results;

    public int getFirstIndex() {
        return firstIndex;
    }

    public void setFirstIndex(int firstIndex) {
        this.firstIndex = firstIndex;
    }

    public Boolean getHasMore() {
        return hasMore;
    }

    public void setHasMore(Boolean hasMore) {
        this.hasMore = hasMore;
    }

    public IBWtfListItem[] getResults() {
        return results;
    }

    public void setResults(IBWtfListItem[] results) {
        this.results = results;
    }

    public IBWtfList() {
        super();
        onNew();
        this.results = new IBWtfListItem[0];

    }
    //region Countable

    private final static String  CLASSTAG       = EHRLibRuntime.kModulePrefix + "." + IBWtfList.class.getSimpleName();
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

    //region GSON helpers


    public String asJson() {
        GsonBuilder builder        = GsonFactory.standardBuilder();
        Gson        jsonSerializer = builder.create();
        String      theJson        = jsonSerializer.toJson(this, this.getClass());
        return theJson;
    }

    public static IBWtfList fromJson(String json) {
        GsonBuilder builder          = GsonFactory.standardBuilder();
        Gson        jsonDeserializer = builder.create();
        IBWtfList   theObject        = jsonDeserializer.fromJson(json, IBWtfList.class);
        return theObject;
    }

    //endregion
}
