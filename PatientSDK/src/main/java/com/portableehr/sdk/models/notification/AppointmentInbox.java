package com.portableehr.sdk.models.notification;

import android.util.Log;

import com.portableehr.sdk.network.NAO.inbound.IBNotification;
import com.portableehr.sdk.network.NAO.inbound.IBUser;
import com.portableehr.sdk.network.gson.GSONexcludeOutbound;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static com.portableehr.sdk.EHRLibRuntime.kModulePrefix;

/**
 * Created by : yvesleborg
 * Date       : 2019-07-27
 * <p>
 * Copyright Portable Ehr Inc, 2019
 */
public class AppointmentInbox extends AbstractInbox {

    private static String                    capabilityAlias = "core.appointment";
    private static NotificationModelTypeEnum modelType       = NotificationModelTypeEnum.APPOINTMENTS;

    AppointmentInbox() {
        super();
        onNew();
    }

    //region AbstractInboxFilter implementation
    @Override
    public String getCapabilityAlias() {
        return AppointmentInbox.capabilityAlias;
    }

    @Override
    public NotificationModelTypeEnum getType() {
        return modelType;
    }

    @Override
    public InboxCapabilityEnum getKind() {
        try {
            return InboxCapabilityEnum.forCapability(getCapabilityAlias());
        } catch (Exception e) {
            Log.e(TAG, "getKind: caught exception : ", e);
            return null;
        }
    }

    @Override
    public void sort(ArrayList<IBNotification> set) {

        Collections.sort(set, new Comparator<IBNotification>() {
            @Override
            public int compare(IBNotification o1, IBNotification o2) {
                boolean ok = (o1.getAppointment() != null) && (o2.getAppointment() != null);
                if (ok) {
                    long delta = o1.getAppointment().getSortOrder() - o2.getAppointment().getSortOrder();
                    if (delta > 0) {
                        return -1;
                    } else if (delta < 0) {
                        return 1;
                    }
                } else {
                    Log.e(getLogTAG(), "Sort comparator compares on null ! returning 0");
                }
                return 0;
            }
        });

    }

    @Override
    public Integer numberOfActionRequired(IBUser user) {
        int number = super.numberOfActionRequired(user);
        return number;
    }

    //endregion


    //region Countable

    public boolean getVerbose() {
        return classCountable;
    }

    static {
        setClassCountable(false);
    }

    private final static String  CLASSTAG = kModulePrefix + "." + AppointmentInbox.class.getSimpleName();
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

    String getLogTAG() {
        TAG = CLASSTAG + " [" + getLogLabel() + "] ";
        return TAG;
    }

    //endregion

}
