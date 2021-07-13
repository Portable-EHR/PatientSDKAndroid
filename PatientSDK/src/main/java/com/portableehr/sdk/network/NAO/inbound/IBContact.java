package com.portableehr.sdk.network.NAO.inbound;

/*
 * Created by : yvesleborg
 * Date       : 2017-03-31
 *
 * Copyright Portable Ehr Inc, 2020
 */

import androidx.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.portableehr.sdk.network.enums.LanguageEnum;
import com.portableehr.sdk.network.enums.PatientGenderEnum;
import com.portableehr.sdk.network.gson.GSONexcludeOutbound;

import java.util.Date;

import static com.portableehr.sdk.EHRLibRuntime.kModulePrefix;
import static com.portableehr.sdk.network.gson.GsonFactory.standardBuilder;

@SuppressWarnings("unused")
public class IBContact {

    private String            name;
    private String            firstName;
    private String            middleName;
    private String            alternateEmail;
    private String            email;
    private String            landPhone;
    private String            mobilePhone;
    @Nullable
    private String            fax;
    private String            salutation;
    private String            professionalDesignation;
    private String            titles;
    private String            guid;
    private Date              createdOn;
    @Nullable
    private Date              lastUpdated;
    @Nullable
    private String            memo;
    @Nullable
    private PatientGenderEnum preferredGender;
    @Nullable
    private LanguageEnum      preferredLanguage;

    public IBContact() {
        onNew();
    }

    public String getName() {
        return name;
    }

    public void setName(@SuppressWarnings("SameParameterValue") String name) {
        this.name = name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(@SuppressWarnings("SameParameterValue") String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getAlternateEmail() {
        return alternateEmail;
    }

    public void setAlternateEmail(String alternateEmail) {
        this.alternateEmail = alternateEmail;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLandPhone() {
        return landPhone;
    }

    public void setLandPhone(String landPhone) {
        this.landPhone = landPhone;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getSalutation() {
        return salutation;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    public String getProfessionalDesignation() {
        return professionalDesignation;
    }

    public void setProfessionalDesignation(String professionalDesignation) {
        this.professionalDesignation = professionalDesignation;
    }

    public String getTitles() {
        return titles;
    }

    public void setTitles(String titles) {
        this.titles = titles;
    }

    public String getFullName() {
        return String.format("%s %s", getFirstName(), getName());
    }


    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    @Nullable
    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(@Nullable Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Nullable
    public String getMemo() {
        return memo;
    }

    public void setMemo(@Nullable String memo) {
        this.memo = memo;
    }

    @Nullable
    public PatientGenderEnum getPreferredGender() {
        return preferredGender;
    }

    public void setPreferredGender(@Nullable PatientGenderEnum preferredGender) {
        this.preferredGender = preferredGender;
    }

    @Nullable
    public LanguageEnum getPreferredLanguage() {
        return preferredLanguage;
    }

    public void setPreferredLanguage(@Nullable LanguageEnum preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    @Nullable
    public String getFax() {
        return fax;
    }

    public void setFax(@Nullable String fax) {
        this.fax = fax;
    }


    //region Countable

    private final static String  CLASSTAG       = kModulePrefix + "." + IBContact.class.getSimpleName();
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
        GsonBuilder builder        = standardBuilder();
        Gson        jsonSerializer = builder.create();
        String      theJson        = jsonSerializer.toJson(this, this.getClass());
        return theJson;
    }

    public static IBContact fromJson(String json) {
        GsonBuilder builder          = standardBuilder();
        Gson        jsonDeserializer = builder.create();
        IBContact   theObject        = jsonDeserializer.fromJson(json, IBContact.class);
        return theObject;
    }

    //endregion

}
