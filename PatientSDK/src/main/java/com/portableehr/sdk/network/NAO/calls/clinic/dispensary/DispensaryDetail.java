package com.portableehr.sdk.network.NAO.calls.clinic.dispensary;

import androidx.annotation.Nullable;
import android.util.Log;

import com.portableehr.sdk.network.NAO.inbound.IBAddress;
import com.portableehr.sdk.network.NAO.inbound.IBContact;
import com.portableehr.sdk.network.NAO.inbound.IBMedia;
import com.portableehr.sdk.network.NAO.inbound.feed.IBFeedDetail;
import com.portableehr.sdk.network.gson.GSONexcludeOutbound;

import java.util.Date;
import java.util.HashMap;

import static com.portableehr.sdk.EHRLibRuntime.kModulePrefix;

/**
 * Created by : yvesleborg
 * Date       : 2019-04-30
 * <p>
 * Copyright Portable Ehr Inc, 2019
 */
@SuppressWarnings("unused")
public class DispensaryDetail {

    {
        setClassCountable(false);
    }

    @SuppressWarnings({"unused", "InnerClassMayBeStatic"})
    public class DispensarySettingsPolicy {
        @Nullable
        Integer majorityAge;
        String[] availableServices;
        Date     lastUpdated;

        public DispensarySettingsPolicy(DispensarySettingsPolicy original) {
            this.majorityAge = original.getMajorityAge();
            this.availableServices = original.getAvailableServices();
            this.lastUpdated = original.getLastUpdated();
        }

        @Nullable
        public Integer getMajorityAge() {
            return majorityAge;
        }

        public void setMajorityAge(@Nullable Integer majorityAge) {
            this.majorityAge = majorityAge;
        }

        public String[] getAvailableServices() {
            return availableServices;
        }

        public void setAvailableServices(String[] availableServices) {
            this.availableServices = availableServices;
        }

        public Date getLastUpdated() {
            return lastUpdated;
        }

        public void setLastUpdated(Date lastUpdated) {
            this.lastUpdated = lastUpdated;
        }
    }

    String  type;
    boolean active;
    String  guid;
    String  alias;
    String  name;
    @Nullable
    String shortName;
    @Nullable
    String landPhone;
    String    description;
    IBAddress address;
    IBContact adminContact;
    IBContact techContact;
    IBContact publicContact;
    @Nullable
    IBMedia thumb;
    String                               hcoGuid;
    HashMap<String, IBFeedDetail>        feeds;
    HashMap<String, DispensaryStaffUser> staffUsers;
    DispensarySettings                   settings;
    DispensarySettingsPolicy             settingsPolicy;

    public DispensaryDetail() {
        onNew();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Nullable
    public String getShortName() {
        return shortName;
    }

    public void setShortName(@Nullable String shortName) {
        this.shortName = shortName;
    }

    @Nullable
    public String getLandPhone() {
        return landPhone;
    }

    public void setLandPhone(@Nullable String landPhone) {
        this.landPhone = landPhone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public IBAddress getAddress() {
        return address;
    }

    public void setAddress(IBAddress address) {
        this.address = address;
    }

    public IBContact getAdminContact() {
        return adminContact;
    }

    public void setAdminContact(IBContact adminContact) {
        this.adminContact = adminContact;
    }

    public IBContact getTechContact() {
        return techContact;
    }

    public void setTechContact(IBContact techContact) {
        this.techContact = techContact;
    }

    public IBContact getPublicContact() {
        return publicContact;
    }

    public void setPublicContact(IBContact publicContact) {
        this.publicContact = publicContact;
    }

    @Nullable
    public IBMedia getThumb() {
        return thumb;
    }

    public void setThumb(@Nullable IBMedia thumb) {
        this.thumb = thumb;
    }

    public String getHcoGuid() {
        return hcoGuid;
    }

    public void setHcoGuid(String hcoGuid) {
        this.hcoGuid = hcoGuid;
    }

    public HashMap<String, IBFeedDetail> getFeeds() {
        return feeds;
    }

    public void setFeeds(HashMap<String, IBFeedDetail> feeds) {
        this.feeds = feeds;
    }

    public HashMap<String, DispensaryStaffUser> getStaffUsers() {
        return staffUsers;
    }

    public void setStaffUsers(HashMap<String, DispensaryStaffUser> staffUsers) {
        this.staffUsers = staffUsers;
    }

    public DispensarySettings getSettings() {
        return settings;
    }

    public void setSettings(DispensarySettings settings) {
        this.settings = settings;
    }

    public DispensarySettingsPolicy getSettingsPolicy() {
        return settingsPolicy;
    }

    public void setSettingsPolicy(DispensarySettingsPolicy settingsPolicy) {
        this.settingsPolicy = settingsPolicy;
    }

    //region utility getters
    public DispensaryStaffUser staffUserWithGuid(String guid) {
        return getStaffUsers().get(guid);
    }
    //endregion

    //region Countable

    private final static String  CLASSTAG       = kModulePrefix + "." + DispensaryDetail.class.getSimpleName();
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
