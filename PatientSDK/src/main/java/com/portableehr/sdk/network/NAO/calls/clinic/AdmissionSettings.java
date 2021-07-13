package com.portableehr.sdk.network.NAO.calls.clinic;

import androidx.annotation.Nullable;
import android.util.Log;

import com.portableehr.sdk.network.enums.AdmissionLocationEnum;
import com.portableehr.sdk.network.gson.GSONexcludeOutbound;

import static com.portableehr.sdk.EHRLibRuntime.kModulePrefix;

/**
 * Created by : yvesleborg
 * Date       : 2020-07-05
 * <p>
 * Copyright Portable Ehr Inc, 2020
 */
public class AdmissionSettings {

    {
        setClassCountable(false);
    }


    PatientFeedPolicyEnum patientFeed;
    AdmissionLocationEnum admissionLocation;
    boolean               inviteOnAppointmentHashtag;
    @Nullable
    Boolean showAddresablesOnly;
    @Nullable
    String  appointmentHashTag;


    public AdmissionSettings() {
        onNew();
        this.admissionLocation = AdmissionLocationEnum.HOME;
    }

    public AdmissionSettings(AdmissionSettings original) {
        this.patientFeed = original.patientFeed;
        this.admissionLocation = original.admissionLocation;
    }

    public PatientFeedPolicyEnum getPatientFeed() {
        return patientFeed;
    }

    public void setPatientFeed(PatientFeedPolicyEnum patientFeed) {
        this.patientFeed = patientFeed;
    }

    public AdmissionLocationEnum getAdmissionLocation() {
        return admissionLocation;
    }

    public void setAdmissionLocation(AdmissionLocationEnum admissionLocation) {
        this.admissionLocation = admissionLocation;
    }

    public boolean isInviteOnAppointmentHashtag() {
        return inviteOnAppointmentHashtag;
    }

    public void setInviteOnAppointmentHashtag(boolean inviteOnAppointmentHashtag) {
        this.inviteOnAppointmentHashtag = inviteOnAppointmentHashtag;
    }

    @Nullable
    public String getAppointmentHashTag() {
        return appointmentHashTag;
    }

    public void setAppointmentHashTag(@Nullable String appointmentHashTag) {
        this.appointmentHashTag = appointmentHashTag;
    }

    public Boolean isShowAddresablesOnly() {
        return showAddresablesOnly;
    }

    public void setShowAddresablesOnly(boolean showAddresablesOnly) {
        this.showAddresablesOnly = showAddresablesOnly;
    }

    //region Countable
    static {
        setClassCountable(false);
    }

    protected boolean getVerbose() {
        return classCountable;
    }

    private final static String  CLASSTAG = kModulePrefix + "." + AdmissionSettings.class.getSimpleName();
    @GSONexcludeOutbound
    private              String  TAG;
    private static       int     lifeTimeInstances;
    private static       int     numberOfInstances;
    @GSONexcludeOutbound
    private              int     instanceNumber;
    @GSONexcludeOutbound
    private static       boolean classCountable;

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        numberOfInstances--;
        if (numberOfInstances < 0) {
            Log.e(getLogTAG(), "*** You did not call onNew in your ctor(s)");
        }
        if (getVerbose()) {
            Log.d(getLogTAG(), "finalize  ");
        }
    }

    protected void onNew() {
        TAG = CLASSTAG;
        numberOfInstances++;
        lifeTimeInstances++;
        instanceNumber = lifeTimeInstances;
        if (getVerbose()) {
            Log.d(getLogTAG(), "onNew ");
        }
    }

    private String getLogLabel() {
        return Integer.toHexString(instanceNumber) + "/" + Integer.toHexString(numberOfInstances);
    }

    public static void setClassCountable(boolean isIt) {
        classCountable = isIt;
    }

    protected String getLogTAG() {
        TAG = CLASSTAG + " [" + getLogLabel() + "] ";
        return TAG;
    }

    //endregion
}
