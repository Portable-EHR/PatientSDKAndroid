package com.portableehr.sdk.network.NAO.inbound;

/*
 * Created by yvesleborg on 2017-12-20.
 *
 * Copyright Portable Ehr Inc, 2019
 */

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.portableehr.sdk.network.gson.GSONexcludeOutbound;
import com.portableehr.sdk.network.gson.GsonFactory;

import java.util.Date;

import static com.portableehr.sdk.EHRLibRuntime.kModulePrefix;

public class IBCapability {

    private String serviceGuid;
    private String guid;
    private String alias;
    private String activationMode;
    private String scope;
    private String capabilityDescription;
    private String eulaGuid;
    private String name;
    private Date   createdOn;
    private Date   lastUpdated;

    //region Getters/Setters

    public String getServiceGuid() {
        return serviceGuid;
    }

    public void setServiceGuid(String serviceGuid) {
        this.serviceGuid = serviceGuid;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getActivationMode() {
        return activationMode;
    }

    public void setActivationMode(String activationMode) {
        this.activationMode = activationMode;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getCapabilityDescription() {
        return capabilityDescription;
    }

    public void setCapabilityDescription(String capabilityDescription) {
        this.capabilityDescription = capabilityDescription;
    }

    public String getEulaGuid() {
        return eulaGuid;
    }

    public void setEulaGuid(String eulaGuid) {
        this.eulaGuid = eulaGuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
    //endregion

    public IBCapability() {
        onNew();
    }


    //region GSON helpers


    public String asJson() {
        GsonBuilder builder        = GsonFactory.standardBuilder();
        Gson        jsonSerializer = builder.create();
        String      theJson        = jsonSerializer.toJson(this, this.getClass());
        return theJson;
    }

    public static IBCapability fromJson(String json) {
        GsonBuilder  builder          = GsonFactory.standardBuilder();
        Gson         jsonDeserializer = builder.create();
        IBCapability theObject        = jsonDeserializer.fromJson(json, IBCapability.class);
        return theObject;
    }

    //endregion



    //region Countable

    private final static String  CLASSTAG       = kModulePrefix + "." + IBCapability.class.getSimpleName();
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

    //endregion
}
