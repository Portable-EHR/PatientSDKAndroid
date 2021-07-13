package com.portableehr.sdk.network.NAO.calls.clinic;

import androidx.annotation.Nullable;
import android.util.Log;

import com.portableehr.sdk.network.gson.GSONexcludeOutbound;

import static com.portableehr.sdk.EHRLibRuntime.kModulePrefix;

/**
 * Created by : yvesleborg
 * Date       : 2020-07-05
 * <p>
 * Copyright Portable Ehr Inc, 2020
 */
public class ScheduleSettings {

    {
        setClassCountable(false);
    }


    @Nullable
    Boolean openOnWeekends;
    @Nullable
    Boolean openOnStatutoryHolidays;
    String  timeZone;
    float[] openingHour;
    float[] hoursOpen;


    public ScheduleSettings() {
        onNew();
    }

    public ScheduleSettings(ScheduleSettings original) {
        this.timeZone = original.timeZone;
        this.openingHour = original.openingHour;
        this.hoursOpen = original.hoursOpen;
    }


    @Nullable
    public Boolean getOpenOnWeekends() {
        return openOnWeekends;
    }

    public void setOpenOnWeekends(@Nullable Boolean openOnWeekends) {
        this.openOnWeekends = openOnWeekends;
    }

    @Nullable
    public Boolean getOpenOnStatutoryHolidays() {
        return openOnStatutoryHolidays;
    }

    public void setOpenOnStatutoryHolidays(@Nullable Boolean openOnStatutoryHolidays) {
        this.openOnStatutoryHolidays = openOnStatutoryHolidays;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public float[] getOpeningHour() {
        return openingHour;
    }

    public void setOpeningHour(float[] openingHour) {
        this.openingHour = openingHour;
    }

    public float[] getHoursOpen() {
        return hoursOpen;
    }

    public void setHoursOpen(float[] hoursOpen) {
        this.hoursOpen = hoursOpen;
    }

    //region Countable
    static {
        setClassCountable(false);
    }

    protected boolean getVerbose() {
        return classCountable;
    }

    private final static String  CLASSTAG = kModulePrefix + "." + ScheduleSettings.class.getSimpleName();
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
