package com.portableehr.sdk.network.NAO.inbound.clinic;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.portableehr.sdk.EHRLibRuntime;
import com.portableehr.sdk.network.NAO.inbound.IBDeviceInfo;
import com.portableehr.sdk.network.NAO.inbound.user.IBUserVerification;
import com.portableehr.sdk.network.gson.GSONexcludeOutbound;
import com.portableehr.sdk.network.gson.GsonFactory;

import java.util.HashMap;

/**
 * Created by : yvesleborg
 * Date       : 2018-07-15
 * <p>
 * Copyright Portable Ehr Inc, 2019
 */
@SuppressWarnings("unused")
public class IBUserActivationState {

    private String                        email;
    private String                        username;
    private boolean                       emailVerified;
    private boolean                       mobileVerified;
    private String                        mobile;
    private String                        status;
    private IBUserActivationOffer         activationOffer;
    private HashMap<String, IBDeviceInfo> devices;
    private IBUserVerification            userVerification;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }


    public void setMobileVerified(boolean mobileVerified) {
        this.mobileVerified = mobileVerified;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public IBUserActivationOffer getActivationOffer() {
        return activationOffer;
    }

    public void setActivationOffer(IBUserActivationOffer activationOffer) {
        this.activationOffer = activationOffer;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public boolean isMobileVerified() {
        return mobileVerified;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public HashMap<String, IBDeviceInfo> getDevices() {
        return devices;
    }

    public void setDevices(HashMap<String, IBDeviceInfo> devices) {
        this.devices = devices;
    }

    public IBUserVerification getUserVerification() {
        return userVerification;
    }

    public void setUserVerification(IBUserVerification userVerification) {
        this.userVerification = userVerification;
    }

    public IBUserActivationState() {
        super();
        onNew();
    }

    //region Countable

    private final static String  CLASSTAG       = EHRLibRuntime.kModulePrefix + "." + IBUserActivationState.class.getSimpleName();
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

    public static IBUserActivationState fromJson(String json) {
        GsonBuilder           builder          = GsonFactory.standardBuilder();
        Gson                  jsonDeserializer = builder.create();
        IBUserActivationState theObject        = jsonDeserializer.fromJson(json, IBUserActivationState.class);
        return theObject;
    }

    //endregion
}
