package com.portableehr.sdk.network.NAO.inbound.patient.appointment;

import androidx.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.portableehr.sdk.network.NAO.inbound.IBPatientInfo;
import com.portableehr.sdk.network.NAO.inbound.IBPractitioner;
import com.portableehr.sdk.network.NAO.inbound.IBUserInfo;
import com.portableehr.sdk.network.NAO.inbound.clinic.IBDispensaryInfo;
import com.portableehr.sdk.network.gson.GSONexcludeOutbound;
import com.portableehr.sdk.network.gson.GsonFactory;

import java.util.Date;

import static com.portableehr.sdk.EHRLibRuntime.kModulePrefix;

/**
 * Created by : yvesleborg
 * Date       : 2019-07-27
 * <p>
 * Copyright Portable Ehr Inc, 2019
 */
public class Appointment {

    {
        setClassCountable(false);
    }

    private String           guid;
    private Date             lastUpdated;
    @Nullable
    private IBPractitioner   practitioner;
    private String           withPersonNamed;
    private String           location;
    private String           description;
    private Date             startTime;
    private Date             endTime;
    @Nullable
    private IBUserInfo       confirmedBy;
    @Nullable
    private IBUserInfo       cancelledBy;
    @Nullable
    private Date             cancelledOn;
    @Nullable
    private Date             confirmedOn;
    private Date             createdOn;
    private String           state;
    private IBDispensaryInfo dispensaryInfo;
    private IBPatientInfo    patientInfo;
    @Nullable
    private Boolean          patientCanCancel;
    @Nullable
    private Boolean          patientMustConfirm;
    @Nullable
    private Date             confirmBefore;
    private String           confirmationStatus;
    private boolean          cancelFeesApply;
    @Nullable
    private Date             sendSMSon;
    @Nullable
    private Date             smsSentOn;
    @Nullable
    private Date             remindOn;


    //region ctors

    public Appointment() {
        super();
        onNew();
    }

    //endregion

    //region setters/getters

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Nullable
    public IBPractitioner getPractitioner() {
        return practitioner;
    }

    public void setPractitioner(@Nullable IBPractitioner practitioner) {
        this.practitioner = practitioner;
    }

    public String getWithPersonNamed() {
        return withPersonNamed;
    }

    public void setWithPersonNamed(String withPersonNamed) {
        this.withPersonNamed = withPersonNamed;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    @Nullable
    public IBUserInfo getConfirmedBy() {
        return confirmedBy;
    }

    public void setConfirmedBy(@Nullable IBUserInfo confirmedBy) {
        this.confirmedBy = confirmedBy;
    }

    @Nullable
    public IBUserInfo getCancelledBy() {
        return cancelledBy;
    }

    public void setCancelledBy(@Nullable IBUserInfo cancelledBy) {
        this.cancelledBy = cancelledBy;
    }

    @Nullable
    public Date getCancelledOn() {
        return cancelledOn;
    }

    public void setCancelledOn(@Nullable Date cancelledOn) {
        this.cancelledOn = cancelledOn;
    }

    @Nullable
    public Date getConfirmedOn() {
        return confirmedOn;
    }

    public void setConfirmedOn(@Nullable Date confirmedOn) {
        this.confirmedOn = confirmedOn;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public IBDispensaryInfo getDispensaryInfo() {
        return dispensaryInfo;
    }

    public void setDispensaryInfo(IBDispensaryInfo dispensaryInfo) {
        this.dispensaryInfo = dispensaryInfo;
    }

    public IBPatientInfo getPatientInfo() {
        return patientInfo;
    }

    public void setPatientInfo(IBPatientInfo patientInfo) {
        this.patientInfo = patientInfo;
    }

    @Nullable
    public Boolean getPatientCanCancel() {
        return patientCanCancel;
    }

    public void setPatientCanCancel(@Nullable Boolean patientCanCancel) {
        this.patientCanCancel = patientCanCancel;
    }

    @Nullable
    public Boolean getPatientMustConfirm() {
        return patientMustConfirm;
    }

    public void setPatientMustConfirm(@Nullable Boolean patientMustConfirm) {
        this.patientMustConfirm = patientMustConfirm;
    }

    @Nullable
    public Date getConfirmBefore() {
        return confirmBefore;
    }

    public void setConfirmBefore(@Nullable Date confirmBefore) {
        this.confirmBefore = confirmBefore;
    }

    public String getConfirmationStatus() {
        return confirmationStatus;
    }

    public void setConfirmationStatus(String confirmationStatus) {
        this.confirmationStatus = confirmationStatus;
    }

    public boolean isCancelFeesApply() {
        return cancelFeesApply;
    }

    public void setCancelFeesApply(boolean cancelFeesApply) {
        this.cancelFeesApply = cancelFeesApply;
    }

    @Nullable
    public Date getSendSMSon() {
        return sendSMSon;
    }

    public void setSendSMSon(@Nullable Date sendSMSon) {
        this.sendSMSon = sendSMSon;
    }

    @Nullable
    public Date getSmsSentOn() {
        return smsSentOn;
    }

    public void setSmsSentOn(@Nullable Date smsSentOn) {
        this.smsSentOn = smsSentOn;
    }

    @Nullable
    public Date getRemindOn() {
        return remindOn;
    }

    public void setRemindOn(@Nullable Date remindOn) {
        this.remindOn = remindOn;
    }

    public long getSortOrder() {
        return calculateSortOrder();
    }


    //endregion

    //region utilitary qualifiers

    public boolean isInThePast() {
        Date now = new Date();
        return now.after(this.startTime);
    }

    public boolean isActionRequired() {
        if (!isInPlay()) {
            return false;
        }

        if (isResolved()) {
            return false;
        }

        if (confirmBefore == null) {
            Log.e(TAG, "isActionRequired: confirmBefore is null, bailing out");
            return false;
        }

        if (startTime == null) {
            Log.e(TAG, "isActionRequired: startTime is null, bailing out");
            return false;
        }

        if (isRemindable()) {
            return true;
        }


        return false;

    }

    public boolean isConfirmed() {
        return state.equals("confirmed");
    }

    public boolean isCancelled() {
        return state.equals("cancelled");
    }

    public boolean isPending() {
        if (state.equals("pending")) {
            return true;
        }
        return false;
    }

    public boolean isResolved() {
        return isConfirmed() || isCancelled();
    }

    public boolean areCancelFeesInPlay() {
        if (!isPending()) {
            return false;
        }
        if (!cancelFeesApply) {
            return false;
        }
        if (isInThePast()) {
            return false;
        }
        if (!isInPlay()) {
            return false;
        }
        if (null == confirmBefore) {
            return false;
        }
        Date now = new Date();
        if (now.getTime() < confirmBefore.getTime()) {
            return false;
        }
        if (now.getTime() > startTime.getTime()) {
            return false;
        }
        return true;
    }

    public boolean isRemindable() {

        if (isResolved()) {
            return false;
        }

        //|now     |remindOn        |startTime
        //+--------+----------------+----------->
        //  false  +     true       +   false

        if (null == this.remindOn) {
            return false;
        }

        // neither cancelled nor confirmed, future
        Date now = new Date();
        if (now.getTime() < this.remindOn.getTime()) {
            return false;
        }
        if (now.getTime() < this.startTime.getTime()) {
            return true;
        }
        return false;

    }

    public boolean isInPlay() {
        if (this.isInThePast()) {
            return false;
        }
        if (this.isCancelled()) {
            return false;
        }
        return true;
    }

    private long calculateSortOrder() {
        // the normal sort oreder will be
        //    isInPlay first , followed by
        //    ! isInPlay
        // isInplayAppointments will be nearest first (between now and start date)
        // !isInPlay will be also in nearest to now to furthest
        long sortOrder;
        if (null == this.startTime) {
            Log.e(getLogTAG(), "Appointment with guid " + this.getGuid() + " has no start date, cant set sort order.");
            sortOrder = 0;

        } else {

            Date eternity = new Date(Long.MAX_VALUE);
            Date now      = new Date();
            /*
            0                             now                Eternity
            +------------------------------+=================+
                notInPlay                        isInPlay
                   - in the past or
                   - future cancelled
            *.
             */

            @SuppressWarnings("unused")
            long nowToEternity = eternity.getTime() - now.getTime(); // always positive
            long nowToDarkAges = now.getTime(); // always positive

            // sort will layout in-play appointments in nowToEternity , most recent at top, oldest at bottom of nowToEternity
            // sort will layout non-in-play appointments in nowToDarkAges , most recent on top, oldest at bottom of nowToDarkAges
            if (isInPlay()) {
                long timeToEternity = eternity.getTime() - this.startTime.getTime();
                sortOrder = (now.getTime() + timeToEternity); // always positive,
            } else {
                if (this.isInThePast()) {
                    sortOrder = (this.startTime.getTime());
                } else {
                    // future but cancelled , sort should be < now
                    long delta = Math.abs(now.getTime() - this.startTime.getTime());
                    sortOrder = (nowToDarkAges - delta);
                }
            }
        }
        return sortOrder;

    }

    //endregion


    public void updateWith(Appointment other) {
        /*
           private String           guid;
    private Date             lastUpdated;
    @Nullable
    private IBPractitioner   practitioner;
    private String           withPersonNamed;
    private String           location;
    private String           description;
    private Date             startTime;
    private Date             endTime;
    @Nullable
    private IBUserInfo       confirmedBy;
    @Nullable
    private IBUser           cancelledBy;
    @Nullable
    private Date             cancelledOn;
    @Nullable
    private Date             confirmedOn;
    private Date             createdOn;
    private String           state;
    private IBDispensaryInfo dispensaryInfo;
    private IBPatientInfo    patientInfo;
    @Nullable
    private Boolean          patientCanCancel;
    @Nullable
    private Boolean          patientMustConfirm;
    @Nullable
    private Date             confirmBefore;
    private String           confirmationStatus;
    private boolean          cancelFeesApply;
    @Nullable
    private Date             sendSMSon;
    @Nullable
    private Date             smsSentOn;
    @Nullable
    private Date             remindOn;
         */
        setGuid(other.guid);
        setLastUpdated(other.lastUpdated);
        setPractitioner(other.practitioner);
        setWithPersonNamed(other.withPersonNamed);
        setLocation(other.location);
        setDescription(other.description);
        setStartTime(other.startTime);
        setEndTime(other.endTime);
        setConfirmedBy(other.confirmedBy);
        setCancelledBy(other.cancelledBy);
        setCancelledOn(other.cancelledOn);
        setConfirmedOn(other.confirmedOn);
        setCreatedOn(other.createdOn);
        setState(other.state);
        setDispensaryInfo(other.dispensaryInfo); // todo : make dispensaryInfo updatable, and refresh it here
        setPatientInfo(other.patientInfo); // todo : ditto for patientInfo
        setPatientCanCancel(other.patientCanCancel);
        setPatientMustConfirm(other.patientMustConfirm);
        setConfirmBefore(other.confirmBefore);
        setConfirmationStatus(other.confirmationStatus);
        setCancelFeesApply(other.cancelFeesApply);
        setSendSMSon(other.sendSMSon);
        setSmsSentOn(other.smsSentOn);
        setRemindOn(other.remindOn);
    }

    //region Countable

    {
        setClassCountable(false);
    }

    private final static String  CLASSTAG = kModulePrefix + "." + Appointment.class.getSimpleName();
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

    public static Appointment fromJson(String json) {
        GsonBuilder builder          = GsonFactory.standardBuilder();
        Gson        jsonDeserializer = builder.create();
        Appointment theObject        = jsonDeserializer.fromJson(json, Appointment.class);
        return theObject;
    }

    //endregion

}
