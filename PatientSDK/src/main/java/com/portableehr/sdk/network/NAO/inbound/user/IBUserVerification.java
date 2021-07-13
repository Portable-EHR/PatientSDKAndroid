package com.portableehr.sdk.network.NAO.inbound.user;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.portableehr.sdk.network.gson.GSONexcludeOutbound;
import com.portableehr.sdk.network.gson.GsonFactory;

import static com.portableehr.sdk.EHRLibRuntime.kModulePrefix;

/**
 * Created by : yvesleborg
 * Date       : 2020-12-28
 * <p>
 * Copyright Portable Ehr Inc, 2020
 */
@SuppressWarnings("unused")
public class IBUserVerification {

    private boolean emailVerified;
    private boolean mobileVerified;
    private boolean identityVerified;

    public IBUserVerification() {
        super();
        onNew();
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public boolean isMobileVerified() {
        return mobileVerified;
    }

    public void setMobileVerified(boolean mobileVerified) {
        this.mobileVerified = mobileVerified;
    }

    public boolean isIdentityVerified() {
        return identityVerified;
    }

    public void setIdentityVerified(boolean identityVerified) {
        this.identityVerified = identityVerified;
    }

    public boolean is2FAcompleted() {
        return mobileVerified && emailVerified;
    }

    public boolean is3FAcompleted() {
        return is2FAcompleted() & isIdentityVerified();

    }

    //region GSON helpers

    public String asJson() {
        GsonBuilder builder        = GsonFactory.standardBuilder();
        Gson        jsonSerializer = builder.create();
        String      theJson        = jsonSerializer.toJson(this, this.getClass());
        return theJson;
    }

    public static IBUserVerification fromJson(String json) {
        GsonBuilder        builder          = GsonFactory.standardBuilder();
        Gson               jsonDeserializer = builder.create();
        IBUserVerification theObject        = jsonDeserializer.fromJson(json, IBUserVerification.class);
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

    private final static String  CLASSTAG = kModulePrefix + "." + IBUserVerification.class.getSimpleName();
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
