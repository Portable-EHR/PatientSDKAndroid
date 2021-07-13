package com.portableehr.sdk.network.NAO.inbound;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.portableehr.sdk.network.gson.GSONexcludeOutbound;
import com.portableehr.sdk.network.gson.GsonFactory;

import java.util.Date;

import static com.portableehr.sdk.EHRLibRuntime.kModulePrefix;

/**
 *
 * Created by : yvesleborg
 * Date       : 2017-12-20
 *
 * Copyright Portable Ehr Inc, 2019
 */

public class IBUserEula {

    public Date      dateSeen;
    public Date      dateConsented;
    public String    userGuid;
    public String    patientGuid;
    public String    eulaGuid;
    public String    scope;
    public String    aboutGuid;
    public IBVersion eulaVersion;

    public Date getDateSeen() {
        return dateSeen;
    }

    public void setDateSeen(Date dateSeen) {
        this.dateSeen = dateSeen;
    }

    public Date getDateConsented() {
        return dateConsented;
    }

    public void setDateConsented(Date dateConsented) {
        this.dateConsented = dateConsented;
    }

    public String getUserGuid() {
        return userGuid;
    }

    public void setUserGuid(String userGuid) {
        this.userGuid = userGuid;
    }

    public String getPatientGuid() {
        return patientGuid;
    }

    public void setPatientGuid(String patientGuid) {
        this.patientGuid = patientGuid;
    }

    public String getEulaGuid() {
        return eulaGuid;
    }

    public void setEulaGuid(String eulaGuid) {
        this.eulaGuid = eulaGuid;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getAboutGuid() {
        return aboutGuid;
    }

    public void setAboutGuid(String aboutGuid) {
        this.aboutGuid = aboutGuid;
    }

    public IBVersion getEulaVersion() {
        return eulaVersion;
    }

    public void setEulaVersion(IBVersion eulaVersion) {
        this.eulaVersion = eulaVersion;
    }

    public boolean wasSeen() {
        return null != dateSeen;
    }

    public boolean wasConsented() {
        return null != dateConsented;
    }

    public IBUserEula() {
        onNew();
    }

    public IBUserEula(IBEula eula) {
        this.scope = eula.scope;
        this.eulaGuid = eula.guid;
        this.aboutGuid = eula.objectGuid;
        this.eulaVersion = eula.version;
    }


    //region Countable

    private final static String  CLASSTAG       = kModulePrefix + "." + IBUserEula.class.getSimpleName();
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

    public static IBUserEula fromJson(String json) {
        GsonBuilder builder          = GsonFactory.standardBuilder();
        Gson        jsonDeserializer = builder.create();
        IBUserEula  theObject        = jsonDeserializer.fromJson(json, IBUserEula.class);
        return theObject;
    }

    //endregion

}
