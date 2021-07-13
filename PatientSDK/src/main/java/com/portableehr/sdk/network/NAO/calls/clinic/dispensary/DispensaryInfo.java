package com.portableehr.sdk.network.NAO.calls.clinic.dispensary;

import android.util.Log;

import com.portableehr.sdk.network.gson.GSONexcludeOutbound;

import static com.portableehr.sdk.EHRLibRuntime.kModulePrefix;


/**
 * Created by : yvesleborg
 * Date       : 2019-06-21
 * <p>
 * Copyright Portable Ehr Inc, 2019
 */
@SuppressWarnings("unused")
public class DispensaryInfo {
    {
        setClassCountable(false);
    }

    int totalPatients;
    int connectedPatients;
    int privateMessagesAcknowledged;
    int privateMessagesFailed;
    int privateMessagesTotal;
    int clinicAppUsers;

    public DispensaryInfo() {
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

    public int getPrivateMessagesAcknowledged() {
        return privateMessagesAcknowledged;
    }

    public void setPrivateMessagesAcknowledged(int privateMessagesAcknowledged) {
        this.privateMessagesAcknowledged = privateMessagesAcknowledged;
    }

    public int getPrivateMessagesFailed() {
        return privateMessagesFailed;
    }

    public void setPrivateMessagesFailed(int privateMessagesFailed) {
        this.privateMessagesFailed = privateMessagesFailed;
    }

    public int getPrivateMessagesTotal() {
        return privateMessagesTotal;
    }

    public void setPrivateMessagesTotal(int privateMessagesTotal) {
        this.privateMessagesTotal = privateMessagesTotal;
    }

    public int getClinicAppUsers() {
        return clinicAppUsers;
    }

    public void setClinicAppUsers(int clinicAppUsers) {
        this.clinicAppUsers = clinicAppUsers;
    }

    //region Countable

    private final static String  CLASSTAG       = kModulePrefix + "." + DispensaryInfo.class.getSimpleName();
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
