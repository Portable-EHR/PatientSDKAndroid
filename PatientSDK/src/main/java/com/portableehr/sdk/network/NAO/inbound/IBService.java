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
 * Date       : 2017-12-18
 * <p>
 * Copyright Portable Ehr Inc, 2019
 */

public class IBService {

    private String                        guid;
    private String                        alias;
    private Date                          createdOn;
    private Date                          lastUpdated;
    private boolean                       active;
    private String                        name;
    private String                        serviceDescription;
    private String                        serviceDescriptionRenderer;
    private String                        summary;
    private String                        infoUrl;
    private String                        iconMediaGuid;
    private String                        eulaGuid;
    private IBVersion                     version;
    private Integer                       seq;
    private String                        creationOrder;
    private HashMap<String, IBCapability> capabilities;
    private IBAgentInfo                   agentInfo;
    private String                        parentServiceGuid;
    private String[]                      childServicesGuid;

    // computed fields (ie not obtained from mr server)
    private boolean wasSeen;


    public IBService() {
        onNew();
    }

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

    public String getParentServiceGuid() {
        return parentServiceGuid;
    }

    public void setParentServiceGuid(String parentServiceGuid) {
        this.parentServiceGuid = parentServiceGuid;
    }

    public String getServiceDescription() {
        return serviceDescription;
    }

    public void setServiceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getInfoUrl() {
        return infoUrl;
    }

    public void setInfoUrl(String infoUrl) {
        this.infoUrl = infoUrl;
    }

    public String getIconMediaGuid() {
        return iconMediaGuid;
    }

    public void setIconMediaGuid(String iconMediaGuid) {
        this.iconMediaGuid = iconMediaGuid;
    }

    public String getEulaGuid() {
        return eulaGuid;
    }

    public void setEulaGuid(String eulaGuid) {
        this.eulaGuid = eulaGuid;
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

    public IBVersion getVersion() {
        return version;
    }

    public void setVersion(IBVersion version) {
        this.version = version;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public String getCreationOrder() {
        return creationOrder;
    }

    public void setCreationOrder(String creationOrder) {
        this.creationOrder = creationOrder;
    }

    public boolean getWasSeen() {
        return wasSeen;
    }

    public void setWasSeen(boolean wasSeen) {
        this.wasSeen = wasSeen;
    }

    public HashMap<String, IBCapability> getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(HashMap<String, IBCapability> capabilities) {
        this.capabilities = capabilities;
    }

    public IBAgentInfo getAgentInfo() {
        return agentInfo;
    }

    public void setAgentInfo(IBAgentInfo agentInfo) {
        this.agentInfo = agentInfo;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getServiceDescriptionRenderer() {
        return serviceDescriptionRenderer;
    }

    public void setServiceDescriptionRenderer(String serviceDescriptionRenderer) {
        this.serviceDescriptionRenderer = serviceDescriptionRenderer;
    }

    public String[] getChildServicesGuid() {
        return childServicesGuid;
    }

    public void setChildServicesGuid(String[] childServicesGuid) {
        this.childServicesGuid = childServicesGuid;
    }



    //region Countable

    private final static String  CLASSTAG       = kModulePrefix + "." + IBService.class.getSimpleName();
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

    //region GSON helpers


    public String asJson() {
        GsonBuilder builder        = GsonFactory.standardBuilder();
        Gson        jsonSerializer = builder.create();
        String      theJson        = jsonSerializer.toJson(this, this.getClass());
        return theJson;
    }

    public static IBService fromJson(String json) {
        GsonBuilder builder          = GsonFactory.standardBuilder();
        Gson        jsonDeserializer = builder.create();
        IBService   theObject        = jsonDeserializer.fromJson(json, IBService.class);
        return theObject;
    }

    //endregion
}
