package com.portableehr.sdk.network.NAO.calls.clinic.dispensary;

import androidx.annotation.Nullable;
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
public class PrivateMessageEmailSettings {

    {
        setClassCountable(false);
    }

    @Nullable
    String email;
    boolean isVerified;

    public PrivateMessageEmailSettings() {
        onNew();
    }

    public PrivateMessageEmailSettings(PrivateMessageEmailSettings other) {
        onNew();
        this.email = other.email;
        this.isVerified = other.isVerified;
    }

    public PrivateMessageEmailSettings(@Nullable String email, boolean isVerified) {
        this();
        this.email = email;
        this.isVerified = isVerified;
    }

    @Nullable
    public String getEmail() {
        return email;
    }

    public void setEmail(@Nullable String email) {
        this.email = email;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

//region Countable

    private final static String  CLASSTAG       = kModulePrefix + "." + PrivateMessageEmailSettings.class.getSimpleName();
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
