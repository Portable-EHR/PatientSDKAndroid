package com.portableehr.sdk.network.NAO.calls.clinic.dispensary;

import android.util.Log;

import com.portableehr.sdk.network.gson.GSONexcludeOutbound;

import static com.portableehr.sdk.EHRLibRuntime.kModulePrefix;

/**
 * Created by : yvesleborg
 * Date       : 2019-05-07
 * <p>
 * Copyright Portable Ehr Inc, 2018
 */
@SuppressWarnings("unused")
public class PrivateMessageSettings {

    {
        setClassCountable(false);
    }

    String                        reminderDelay;
    String                        fallbackDelay;
    boolean                       failOnFallback;
    String[]                      notifyOnFallback;
    PrivateMessageEmailSettings[] emailOnFallback;

    PrivateMessageSettings() {
        onNew();
    }

    PrivateMessageSettings(PrivateMessageSettings other) {
        onNew();
        this.reminderDelay = other.reminderDelay;
        this.failOnFallback = other.failOnFallback;
        this.fallbackDelay = other.fallbackDelay;
        this.notifyOnFallback = other.notifyOnFallback;
        this.emailOnFallback = other.emailOnFallback;
    }

    public String getReminderDelay() {
        return reminderDelay;
    }

    public void setReminderDelay(String reminderDelay) {
        this.reminderDelay = reminderDelay;
    }

    public String getFallbackDelay() {
        return fallbackDelay;
    }

    public void setFallbackDelay(String fallbackDelay) {
        this.fallbackDelay = fallbackDelay;
    }

    public boolean isFailOnFallback() {
        return failOnFallback;
    }

    public void setFailOnFallback(boolean failOnFallback) {
        this.failOnFallback = failOnFallback;
    }

    public String[] getNotifyOnFallback() {
        return notifyOnFallback;
    }

    public void setNotifyOnFallback(String[] notifyOnFallback) {
        this.notifyOnFallback = notifyOnFallback;
    }

    public PrivateMessageEmailSettings[] getEmailOnFallback() {
        return emailOnFallback;
    }

    public void setEmailOnFallback(PrivateMessageEmailSettings[] emailOnFallback) {
        this.emailOnFallback = emailOnFallback;
    }

//region Countable

    private final static String  CLASSTAG       = kModulePrefix + "." + PrivateMessageSettings.class.getSimpleName();
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
