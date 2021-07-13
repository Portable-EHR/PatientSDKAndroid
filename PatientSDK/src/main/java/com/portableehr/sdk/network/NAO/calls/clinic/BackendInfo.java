package com.portableehr.sdk.network.NAO.calls.clinic;

import android.util.Log;

import com.portableehr.sdk.network.gson.GSONexcludeOutbound;

import static com.portableehr.sdk.EHRLibRuntime.kModulePrefix;


/**
 * Created by : yvesleborg
 * Date       : 2019-04-30
 * <p>
 * Copyright Portable Ehr Inc, 2019
 */
@SuppressWarnings("unused")
public class BackendInfo {

    {
        setClassCountable(false);
    }

    int totalPatients;
    int connectedPatients;
    int connectedClinics;
    int connectedFeeds;
    int totalFeeds;
    int clinicAppUsers;


    public BackendInfo() {
        onNew();
    }

    public int getTotalPatients() {
        return totalPatients;
    }

    public void setTotalPatients(int totalPatients) {
        this.totalPatients = totalPatients;
    }

    public int getConnectedPatients() {
        return connectedPatients;
    }

    public void setConnectedPatients(int connectedPatients) {
        this.connectedPatients = connectedPatients;
    }

    public int getConnectedClinics() {
        return connectedClinics;
    }

    public void setConnectedClinics(int connectedClinics) {
        this.connectedClinics = connectedClinics;
    }

    public int getConnectedFeeds() {
        return connectedFeeds;
    }

    public void setConnectedFeeds(int connectedFeeds) {
        this.connectedFeeds = connectedFeeds;
    }

    public int getTotalFeeds() {
        return totalFeeds;
    }

    public void setTotalFeeds(int totalFeeds) {
        this.totalFeeds = totalFeeds;
    }

    public int getClinicAppUsers() {
        return clinicAppUsers;
    }

    public void setClinicAppUsers(int clinicAppUsers) {
        this.clinicAppUsers = clinicAppUsers;
    }

    //region Countable

    private final static String  CLASSTAG       = kModulePrefix + "." + BackendInfo.class.getSimpleName();
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
