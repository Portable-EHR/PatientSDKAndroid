package com.portableehr.sdk.models.notification;

import android.util.Log;

import com.portableehr.sdk.network.NAO.inbound.IBMessageContent;
import com.portableehr.sdk.network.NAO.inbound.IBMessageDistribution;
import com.portableehr.sdk.network.NAO.inbound.IBNotification;
import com.portableehr.sdk.network.NAO.inbound.IBUser;
import com.portableehr.sdk.network.gson.GSONexcludeOutbound;

import java.util.ArrayList;
import java.util.Hashtable;

import static com.portableehr.sdk.EHRLibRuntime.kModulePrefix;

/**
 * Created by : yvesleborg
 * Date       : 2017-04-04
 * <p>
 * Copyright Portable Ehr Inc, 2017-2019
 */

public class InboxFilter {


    Integer           cursorIndex;
    boolean           showArchivedNotifications;
    boolean           showUnArchivedNotifications;
    boolean           showAlertNotifications;
    boolean           showPatientNotifications;
    boolean           showInfoNotifications;
    boolean           showPractitionerNotifications;
    boolean           showUnreadOnly;
    ArrayList<String> sortedKeys;
    ArrayList<String> patientSelector;
    AbstractInbox     inbox;

    public InboxFilter() {
        onNew();
        sortedKeys = new ArrayList<>();
        patientSelector = new ArrayList<>();
        showUnreadOnly = false;
        showArchivedNotifications = false;
    }

    public InboxFilter(AbstractInbox inbox) {
        this();
        this.inbox = inbox;
        this.refreshFilter();
    }

    public AbstractInbox getInbox() {
        return inbox; // todo : this could cause destructor deadlock , use weakref ???
    }

    public Integer numberOfUnseen() {
        Integer                           unseen = 0;
        Hashtable<String, IBNotification> all    = NotificationModel.getInstance().getAllNotifications();
        for (String key : this.sortedKeys) {
            IBNotification not = all.get(key);
            if (null == not) {
                Log.e(getLogTAG(), "numberOfUnseen() : Notification with SEQ [" + key + "] is null !");
                continue;
            }

            if (not.isDeleted()) {
                continue;
            }

            if (not.getSeenOn() == null) {
                unseen++;
                continue;
            }

            if (not.hasUnseenContent()) {
                unseen++;
            }

        }
        return unseen;
    }

    public Integer numberOfActionRequired(IBUser user) {
        Integer                           noRequiringAction = 0;
        Hashtable<String, IBNotification> all               = getInbox().getContent();

        for (String key : this.getSortedKeys()) {
            IBNotification not = all.get(key);
            if (null == not) {
                Log.e(getLogTAG(), "numberOfActionRequired() : Notification with SEQ [" + key + "] is null !");
                continue;
            }
            if (not.isArchived()) {
                continue;
            }
            if (not.isDeleted()) {
                continue;
            }
            if (not.isActionRequired(user)) {
                noRequiringAction++;
            }

        }
        return noRequiringAction;
    }


    /*
     * getters and shit
     */

    public boolean isShowAlertNotifications() {
        return showAlertNotifications;
    }

    public void setShowAlertNotifications(boolean showAlertNotifications) {
        this.showAlertNotifications = showAlertNotifications;
    }

    public boolean isShowPatientNotifications() {
        return showPatientNotifications;
    }

    public void setShowPatientNotifications(boolean showPatientNotifications) {
        this.showPatientNotifications = showPatientNotifications;
    }

    public boolean isShowInfoNotifications() {
        return showInfoNotifications;
    }

    public void setShowInfoNotifications(boolean showInfoNotifications) {
        this.showInfoNotifications = showInfoNotifications;
    }

    public boolean isShowPractitionerNotifications() {
        return showPractitionerNotifications;
    }

    public void setShowPractitionerNotifications(boolean showPractitionerNotifications) {
        this.showPractitionerNotifications = showPractitionerNotifications;
    }

    public boolean isShowArchivedNotifications() {
        return showArchivedNotifications;
    }

    public void setShowArchivedNotifications(boolean showArchivedNotifications) {
        this.showArchivedNotifications = showArchivedNotifications;
    }

    public void setShowUnArchivedNotifications(boolean showUnArchivedNotifications) {
        this.showUnArchivedNotifications = showUnArchivedNotifications;
    }

    public boolean isShowUnreadOnly() {
        return showUnreadOnly;
    }

    public void setShowUnreadOnly(boolean showUnreadOnly) {
        this.showUnreadOnly = showUnreadOnly;
    }

    /*
     * cursor control shit
     */

    public int getCount() {
        if (sortedKeys != null) {
            return sortedKeys.size();
        } else {
            return 0;
        }

    }

    public ArrayList<String> getSortedKeys() {
        return sortedKeys;
    }

    public String[] getSortedKeysArray() {
        String[] ar = sortedKeys.toArray(new String[0]);
        return ar;
    }

    public boolean isAtTop() {
        return cursorIndex == 0;
    }

    public boolean isEmpty() {
        return (sortedKeys.isEmpty());
    }

    public boolean isAtBottom() {
        if (sortedKeys.size() == 0) {
            return true;
        }
        return cursorIndex == sortedKeys.size() - 1;
    }

    public void setCursorAtBottom() {
        if (isEmpty()) {
            cursorIndex = 0;
        } else {
            cursorIndex = sortedKeys.size() - 1;
        }
    }

    public void setCursorAtTop() {
        cursorIndex = 0;
    }

    public void setCursorAtNotification(IBNotification notification) {
        Integer target = sortedKeys.indexOf(notification.getSeq());
        if (target >= 0) {
            cursorIndex = target;
        }
    }

    public void moveToNext() {
        if (isEmpty() || isAtBottom()) {
            return;
        }
        cursorIndex++;
    }

    public void moveToPrevious() {
        if (isEmpty() || isAtTop()) {
            return;
        }
        cursorIndex--;
    }

    public IBNotification getCursor() {
        if (isEmpty()) {
            return null;
        }
        if (cursorIndex == 0) {
            return this.notificationAtIndex(0);
        }
        if (cursorIndex <= sortedKeys.size() - 1) {
            return this.notificationAtIndex(cursorIndex);
        }
        return null;
    }

    public IBNotification getNext() {
        if (isEmpty()) {
            return null;
        }
        if (isAtBottom()) {
            return null;
        }
        return this.notificationAtIndex(cursorIndex + 1);
    }

    public IBNotification getPrevious() {
        if (isEmpty()) {
            return null;
        }
        if (isAtTop()) {
            return null;
        }
        return this.notificationAtIndex(cursorIndex - 1);
    }

    public IBNotification notificationAtIndex(int index) {
        if (index >= sortedKeys.size()) {
            Log.e(getLogTAG(), "*** cursorIndex not in sync with notifications model!");
            return null;
        }
        String         key          = sortedKeys.get(index);
        IBNotification notification = NotificationModel.getInstance().getAllNotifications().get(key);
        if (null == notification) {
            Log.e(getLogTAG(), "*** Sorted keys holds a key absent from the notifications model!");
        }
        return notification;

    }

    public void refreshFilter() {

        IBNotification oldCursor = this.getCursor();

        cursorIndex = 0;
        sortedKeys.clear();
        Hashtable<String, IBNotification> all = inbox.getContent();
        if (null == all || all.size() == 0) {
            if (classCountable) {
                Log.d(getLogTAG(), "Refreshing filter for inbox filter [" + getInbox().getKind() + "], has 0 entries in content.");
            }
            return;
        }

        ArrayList<IBNotification> retained = new ArrayList<>();
        for (IBNotification not : all.values()) {
            boolean keeper = shouldKeep(not);
            if (keeper) {
                retained.add(not);
            }
        }

        if (retained.size() == 0) {
            if (classCountable) {
                Log.d(getLogTAG(), "refreshFilter: no keepers !");
            }
            return;    // no keepers
        }

        getInbox().sort(retained);
        for (IBNotification not : retained) {
            sortedKeys.add(not.getSeq());
        }

        if (null != oldCursor) {
            if (sortedKeys.contains(oldCursor.getSeq())) {
                cursorIndex = sortedKeys.indexOf(oldCursor.getSeq());
            } else {
                Log.w(getLogTAG(), "Refresh flushed our old cursor, reseting to top.");
                cursorIndex = 0;
            }
        }

    }

    boolean shouldKeep(IBNotification notification) {

        if (!contentMatchesFilter(notification)) {
            return false;
        }

        if (notification.isSeen() && showUnreadOnly) {
            return false;
        }

        if (notification.isExpired()) {
            return false;
        }

        if (notification.isArchived()) {
            return showArchivedNotifications;
        } else {
            // notification is not archived
            return showUnArchivedNotifications;
        }

    }

    private boolean contentMatchesFilter(IBNotification notification) {

        return !notification.isDeleted();


    }

    private boolean shouldKeepPatient(IBNotification not) {
        boolean keeper = true;
        if (patientSelector.size() > 0) {
            if ((null != not.getPatientGuid()) &&
                    !patientSelector.contains(not.getPatientGuid())) {
                keeper = false;
            }
        } else {
            // empty patient selector means all patients
            //noinspection ConstantConditions
            keeper = true;
        }
        return keeper;
    }

    public void resetFilter() {
        sortedKeys.clear();
        this.resetPatientSelector();
    }

    private void resetPatientSelector() {
    }

    public IBMessageContent messageAtCursor(IBNotification cursor) {
        return cursor.getMessageContent();
    }

    public IBMessageDistribution messageDistributionAtCursor(@SuppressWarnings("unused") IBNotification cursor) {
        return null;
        // todo : figure this one out from the obj-c
    }


    //region Countable


    //region Countable
    static {
        setClassCountable(false);
    }

    protected boolean getVerbose() {
        return classCountable;
    }

    private final static String  CLASSTAG = kModulePrefix + "." + InboxFilter.class.getSimpleName();
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

    //endregion

}
