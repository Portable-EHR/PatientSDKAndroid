package com.portableehr.sdk.network.NAO.inbound;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.portableehr.sdk.network.gson.GSONexcludeOutbound;
import com.portableehr.sdk.network.gson.GsonFactory;

import static com.portableehr.sdk.EHRLibRuntime.kModulePrefix;

/**
 * Created by : yvesleborg
 * Date       : 2018-08-27
 * <p>
 * Copyright Portable Ehr Inc, 2019
 */
public class FactorsRequirement {
    private boolean email;
    private boolean emailVerification;
    private boolean mobile;
    private boolean mobileVerification;
    private boolean identityVerification;
    private boolean password;
    private boolean alias;

    public boolean isEmail() {
        return email;
    }

    public void setEmail(boolean email) {
        this.email = email;
    }

    public boolean isEmailVerification() {
        return emailVerification;
    }

    public void setEmailVerification(boolean emailVerification) {
        this.emailVerification = emailVerification;
    }

    public boolean isMobile() {
        return mobile;
    }

    public void setMobile(boolean mobile) {
        this.mobile = mobile;
    }

    public boolean isMobileVerification() {
        return mobileVerification;
    }

    public void setMobileVerification(boolean mobileVerification) {
        this.mobileVerification = mobileVerification;
    }

    public boolean isIdentityVerification() {
        return identityVerification;
    }

    public void setIdentityVerification(boolean identityVerification) {
        this.identityVerification = identityVerification;
    }

    public boolean isPassword() {
        return password;
    }

    public void setPassword(boolean password) {
        this.password = password;
    }

    public boolean isAlias() {
        return alias;
    }

    public void setAlias(boolean alias) {
        this.alias = alias;
    }

    public FactorsRequirement() {
        onNew();
    }

    //region GSON helpers


    public String asJson() {
        GsonBuilder builder        = GsonFactory.standardBuilder();
        Gson        jsonSerializer = builder.create();
        String      theJson        = jsonSerializer.toJson(this, this.getClass());
        return theJson;
    }

    public static FactorsRequirement fromJson(String json) {
        GsonBuilder        builder          = GsonFactory.standardBuilder();
        Gson               jsonDeserializer = builder.create();
        FactorsRequirement theObject        = jsonDeserializer.fromJson(json, FactorsRequirement.class);
        return theObject;
    }

    //endregion


    //region Countable

    private final static String  CLASSTAG       = kModulePrefix + "." + FactorsRequirement.class.getSimpleName();
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
