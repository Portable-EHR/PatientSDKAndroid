package com.portableehr.sdk.network.NAO.inbound.clinic;

import androidx.annotation.Nullable;
import android.util.Log;

import com.portableehr.sdk.EHRLibRuntime;
import com.portableehr.sdk.network.NAO.inbound.IBCapability;
import com.portableehr.sdk.network.NAO.inbound.IBVersion;
import com.portableehr.sdk.network.gson.GSONexcludeOutbound;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by : yvesleborg
 * Date       : 2018-07-05
 * <p>
 * Copyright Portable Ehr Inc, 2019
 */
public class IBAppInfo {
    String    name;
    String    guid;
    String    alias;
    String    description;
    String    agentGuid;
    IBVersion version;
    IBVersion requiredVersion;
    @Nullable
    IBVersion availableVersion;
    Date                          createdOn;
    Date                          lastUpdated;
    String                        url;
    String                        oampFaqUrl;
    HashMap<String, IBCapability> capabilities;
    HashMap<String, String>       dispensaries;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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


    public IBVersion getVersion() {
        return version;
    }

    public void setVersion(IBVersion version) {
        this.version = version;
    }

    public IBVersion getRequiredVersion() {
        return requiredVersion;
    }

    public void setRequiredVersion(IBVersion requiredVersion) {
        this.requiredVersion = requiredVersion;
    }

    @Nullable
    public IBVersion getAvailableVersion() {
        return availableVersion;
    }

    public void setAvailableVersion(@Nullable IBVersion availableVersion) {
        this.availableVersion = availableVersion;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public HashMap<String, IBCapability> getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(HashMap<String, IBCapability> capabilities) {
        this.capabilities = capabilities;
    }

    public HashMap<String, String> getDispensaries() {
        return dispensaries;
    }

    public void setDispensaries(HashMap<String, String> dispensaries) {
        this.dispensaries = dispensaries;
    }


    //region Countable

    private final static String  CLASSTAG       = EHRLibRuntime.kModulePrefix + "." + IBAppInfo.class.getSimpleName();
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
