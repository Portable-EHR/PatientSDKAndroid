package com.portableehr.sdk.network.NAO.inbound;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.portableehr.sdk.network.gson.GSONexcludeOutbound;
import com.portableehr.sdk.network.gson.GsonFactory;

import java.util.HashMap;

import static com.portableehr.sdk.EHRLibRuntime.kModulePrefix;

/**
 * Created by : yvesleborg
 * Date       : 2018-06-17
 * <p>
 * Copyright Portable Ehr Inc, 2019
 */

public class IBDispensary {

    //todo : should have users ?

    private IBAddress                              address;
    private IBContact                              tech;
    private IBContact                              admin;
    private IBContact                              publicContact;
    private String                                 description;
    private IBMedia                                thumb;
    private String                                 name;
    private String                                 shortName;
    private String                                 type;
    private String                                 guid;
    private String                                 infoUrl;
    private String                                 landPhone;
    private HashMap<String, IBDispensaryStaffUser> staffUsers;

    public IBDispensary() {
        onNew();
    }

    public HashMap<String, IBDispensaryStaffUser> getStaffUsers() {
        return staffUsers;
    }

    public void setStaffUsers(HashMap<String, IBDispensaryStaffUser> staffUsers) {
        this.staffUsers = staffUsers;
    }

    public String getLandPhone() {
        return landPhone;
    }

    public void setLandPhone(String landPhone) {
        this.landPhone = landPhone;
    }

    public IBContact getAdmin() {
        return admin;
    }

    public void setAdmin(IBContact admin) {
        this.admin = admin;
    }

    public IBContact getPublicContact() {
        return publicContact;
    }

    public void setPublicContact(IBContact publicContact) {
        this.publicContact = publicContact;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public IBMedia getThumb() {
        return thumb;
    }

    public void setThumb(IBMedia thumb) {
        this.thumb = thumb;
    }

    public IBAddress getAddress() {
        return address;
    }

    public void setAddress(IBAddress address) {
        this.address = address;
    }

    public IBContact getTech() {
        return tech;
    }

    public void setTech(IBContact tech) {
        this.tech = tech;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getInfoUrl() {
        return infoUrl;
    }

    public void setInfoUrl(String infoUrl) {
        this.infoUrl = infoUrl;
    }


    //region GSON helpers


    public String asJson() {
        GsonBuilder builder        = GsonFactory.standardBuilder();
        Gson        jsonSerializer = builder.create();
        String      theJson        = jsonSerializer.toJson(this, this.getClass());
        return theJson;
    }

    public static IBDispensary fromJson(String json) {
        GsonBuilder  builder          = GsonFactory.standardBuilder();
        Gson         jsonDeserializer = builder.create();
        IBDispensary theObject        = jsonDeserializer.fromJson(json, IBDispensary.class);
        return theObject;
    }

    //endregion


    //region Countable

    private final static String  CLASSTAG       = kModulePrefix + "." + IBDispensary.class.getSimpleName();
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
