package com.portableehr.sdk.network.NAO.inbound;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.portableehr.sdk.network.gson.GSONexcludeOutbound;
import com.portableehr.sdk.network.gson.GsonFactory;

import java.util.Date;
import java.util.HashMap;

import static com.portableehr.sdk.EHRLibRuntime.kModulePrefix;

/**
 * Created by : yvesleborg
 * Date       : 2018-12-25
 * <p>
 * Copyright Portable Ehr Inc, 2019
 */
public class IBAppSummary {
    public static class Jurisdictions {
        public HashMap<String, String> healthCareJurisdictions;
    }


    public  String                     guid;
    public  String                     name;
    public  String                     alias;
    public  String                     modelCode;
    public  String                     target;
    public  Date                       createdOn;
    public  Date                       lastUpdated;
    public  IBVersion                  requiredVersion;
    public  IBVersion                  version;
    public  String                     info;
    public  String                     agentGuid;
    public  String                     oampFaqUrl;
    public  String                     url;
    public  String                     releaseNotes;
    public  IBAppSummary.Jurisdictions jurisdictions;
    private FactorsRequirement         factorsRequired;
    public  HashMap<String, String>    dispensaries;

    private Date lastRefreshed;

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getModelCode() {
        return modelCode;
    }

    public void setModelCode(String modelCode) {
        this.modelCode = modelCode;
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

    public IBVersion getRequiredVersion() {
        return requiredVersion;
    }

    public void setRequiredVersion(IBVersion requiredVersion) {
        this.requiredVersion = requiredVersion;
    }

    public IBVersion getVersion() {
        return version;
    }

    public void setVersion(IBVersion version) {
        this.version = version;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getAgentGuid() {
        return agentGuid;
    }

    public void setAgentGuid(String agentGuid) {
        this.agentGuid = agentGuid;
    }

    public String getOampFaqUrl() {
        return oampFaqUrl;
    }

    public void setOampFaqUrl(String oampFaqUrl) {
        this.oampFaqUrl = oampFaqUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public IBAppSummary.Jurisdictions getJurisdictions() {
        return jurisdictions;
    }

    public void setJurisdictions(IBAppSummary.Jurisdictions jurisdictions) {
        this.jurisdictions = jurisdictions;
    }

    public HashMap<String, String> getDispensaries() {
        return dispensaries;
    }

    public void setDispensaries(HashMap<String, String> dispensaries) {
        this.dispensaries = dispensaries;
    }

    public Date getLastRefreshed() {
        return lastRefreshed;
    }

    public void setLastRefreshed(Date lastRefreshed) {
        this.lastRefreshed = lastRefreshed;
    }

    public FactorsRequirement getFactorsRequired() {
        return factorsRequired;
    }

    public void setFactorsRequired(FactorsRequirement factorsRequired) {
        this.factorsRequired = factorsRequired;
    }

    public String getReleaseNotes() {
        return releaseNotes;
    }

    public void setReleaseNotes(String releaseNotes) {
        this.releaseNotes = releaseNotes;
    }

    IBAppSummary() {
        onNew();
    }


    //region Countable

    private final static String  CLASSTAG       = kModulePrefix + "." + IBAppSummary.class.getSimpleName();
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

    public static IBAppSummary fromJson(String json) {
        GsonBuilder  builder          = GsonFactory.standardBuilder();
        Gson         jsonDeserializer = builder.create();
        IBAppSummary theObject        = jsonDeserializer.fromJson(json, IBAppSummary.class);
        return theObject;
    }

    //endregion

}
