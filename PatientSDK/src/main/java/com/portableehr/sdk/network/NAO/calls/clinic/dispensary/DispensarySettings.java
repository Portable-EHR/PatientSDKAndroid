package com.portableehr.sdk.network.NAO.calls.clinic.dispensary;

import androidx.annotation.Nullable;
import android.util.Log;

import com.portableehr.sdk.network.NAO.calls.clinic.AdmissionSettings;
import com.portableehr.sdk.network.NAO.calls.clinic.ScheduleSettings;
import com.portableehr.sdk.network.gson.GSONexcludeOutbound;

import java.util.Date;

import static com.portableehr.sdk.EHRLibRuntime.kModulePrefix;

/**
 * Created by : yvesleborg
 * Date       : 2019-05-06
 * <p>
 * Copyright Portable Ehr Inc, 2020
 */
@SuppressWarnings("unused")
public class DispensarySettings {
    {
        setClassCountable(false);
    }

    int majorityAge;
    int minimumPatientAge;
    @Nullable
    Date lastUpdated;
    String                 communicationsLanguage;
    String                 patientDefaultLanguage;
    String[]               activatedServices;
    PrivateMessageSettings privateMessageSettings;
    AdmissionSettings      admissionSettings;
    ScheduleSettings       scheduleSettings;


    public DispensarySettings() {
        patientDefaultLanguage = "fr"; // the default default !
        communicationsLanguage = "fr";
        onNew();
    }

    public DispensarySettings(DispensarySettings original) {
        super();
        onNew();
        this.majorityAge = original.getMajorityAge();
        this.minimumPatientAge = original.getMinimumPatientAge();
        this.lastUpdated = original.getLastUpdated();
        this.communicationsLanguage = original.getCommunicationsLanguage();
        this.patientDefaultLanguage = original.getPatientDefaultLanguage();
        this.activatedServices = original.getActivatedServices();
        this.privateMessageSettings = new PrivateMessageSettings(original.privateMessageSettings);
        if (null != original.admissionSettings) {
            this.admissionSettings = new AdmissionSettings(original.admissionSettings);
        } else {
            admissionSettings = new AdmissionSettings();
        }
    }

    public AdmissionSettings getAdmissionSettings() {
        return admissionSettings;
    }

    public void setAdmissionSettings(AdmissionSettings admissionSettings) {
        this.admissionSettings = admissionSettings;
    }

    public int getMajorityAge() {
        return majorityAge;
    }

    public void setMajorityAge(int majorityAge) {
        this.majorityAge = majorityAge;
    }

    @Nullable
    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(@Nullable Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String[] getActivatedServices() {
        return activatedServices;
    }

    public void setActivatedServices(String[] activatedServices) {
        this.activatedServices = activatedServices;
    }

    public String getCommunicationsLanguage() {
        return communicationsLanguage;
    }

    public void setCommunicationsLanguage(String communicationsLanguage) {
        this.communicationsLanguage = communicationsLanguage;
    }

    public String getPatientDefaultLanguage() {
        return patientDefaultLanguage;
    }

    public void setPatientDefaultLanguage(String patientDefaultLanguage) {
        this.patientDefaultLanguage = patientDefaultLanguage;
    }

    public int getMinimumPatientAge() {
        return minimumPatientAge;
    }

    public void setMinimumPatientAge(int minimumPatientAge) {
        this.minimumPatientAge = minimumPatientAge;
    }

    public PrivateMessageSettings getPrivateMessageSettings() {
        return privateMessageSettings;
    }

    public void setPrivateMessageSettings(PrivateMessageSettings privateMessageSettings) {
        this.privateMessageSettings = privateMessageSettings;
    }

    public ScheduleSettings getScheduleSettings() {
        return scheduleSettings;
    }

    public void setScheduleSettings(ScheduleSettings scheduleSettings) {
        this.scheduleSettings = scheduleSettings;
    }

    //region Countable

    private final static String  CLASSTAG       = kModulePrefix + "." + DispensarySettings.class.getSimpleName();
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
