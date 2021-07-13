package com.portableehr.sdk.models.notification;

import android.util.Log;

import com.portableehr.sdk.network.NAO.inbound.IBNotification;
import com.portableehr.sdk.network.NAO.inbound.IBUser;
import com.portableehr.sdk.network.gson.GSONexcludeOutbound;
import com.portableehr.sdk.util.StringUtils;

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
public class NotificationInbox extends AbstractInbox {

    private static String                    capabilityAlias = "core.notification";
    private static NotificationModelTypeEnum modelType       = NotificationModelTypeEnum.NOTIFICATIONS;

    NotificationInbox() {
        super();
        onNew();
        InboxFilter mFilter = getFilter();
        mFilter.setShowArchivedNotifications(false);
        mFilter.setShowUnArchivedNotifications(true);
        mFilter.setShowPractitionerNotifications(false);
        mFilter.setShowAlertNotifications(true);
        mFilter.setShowInfoNotifications(true);
        mFilter.setShowPatientNotifications(true);
        mFilter.setShowUnreadOnly(false);
    }


    //region AbstractInboxFilter implementation
    @Override
    public String getCapabilityAlias() {
        return NotificationInbox.capabilityAlias;
    }

    @Override
    public NotificationModelTypeEnum getType() {
        return modelType;
    }

    @Override
    public void refreshContent() {
        super.refreshContent();
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
                String seq2  = StringUtils.trimLeadingZeros(o2.getSeq(), 20);
                String seq1  = StringUtils.trimLeadingZeros(o1.getSeq(), 20);
                long   delta = Long.parseLong(seq2) - Long.parseLong(seq1);
                if (delta > 0) {
                    return 1;
                } else if (delta < 0) {
                    return -1;
                }
                return 0;
            }
        });

    }

    //endregion


    @Override
    public Integer numberOfActionRequired(IBUser user) {
        int number = super.numberOfActionRequired(user);
        return number;
    }
    //region Countable

    protected boolean getVerbose() {
        return classCountable;
    }

    static {
        setClassCountable(false);
    }

    private final static String  CLASSTAG = kModulePrefix + "." + NotificationInbox.class.getSimpleName();
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
