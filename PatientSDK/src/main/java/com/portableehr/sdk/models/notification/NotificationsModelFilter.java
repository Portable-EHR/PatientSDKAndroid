package com.portableehr.sdk.models.notification;

import android.util.Log;

import com.portableehr.sdk.network.NAO.inbound.IBMessageContent;
import com.portableehr.sdk.network.NAO.inbound.IBMessageDistribution;
import com.portableehr.sdk.network.NAO.inbound.IBNotification;
import com.portableehr.sdk.network.NAO.inbound.IBUser;
import com.portableehr.sdk.network.gson.GSONexcludeOutbound;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Set;

import static com.portableehr.sdk.EHRLibRuntime.kModulePrefix;


/**
 * Created by : yvesleborg
 * Date       : 2017-04-04
 * <p>
 * Copyright Portable Ehr Inc, 2017-2019
 */

public class NotificationsModelFilter {

    {
        setClassCountable(false);
    }

    Integer cursorIndex;
    boolean showDeletedNotifications;
    boolean showArchivedNotifications;
    boolean showAlertNotifications;
    boolean showPatientNotifications;
    boolean showInfoNotifications;
    boolean showPractitionerNotifications;
    boolean showMessageNotifications;
    boolean showPrivateMessageNotifications;
    boolean showUnreadOnly;
    boolean keepAppointments; // temporary hack until we can replace this clunky model with the Inbox model
    Integer notificationsPerPage;
    ArrayList<String> sortedKeys;
    ArrayList<String> patientSelector;
    private NotificationModelTypeEnum type;

    public NotificationsModelFilter() {
        onNew();
        sortedKeys = new ArrayList<>();
        patientSelector = new ArrayList<>();
        keepAppointments = false;
    }

    public NotificationsModelFilter(NotificationModelTypeEnum type) {
        this();
        this.setType(type);
        showUnreadOnly = false;
        showArchivedNotifications = false;
        showDeletedNotifications = true;
        this.setType(type);
    }


    public Integer numberOfUnseen() {
        Integer unseen = 0;
        Hashtable<String, IBNotification> all = NotificationModel.getInstance().getAllNotifications();
        for (String key : this.sortedKeys) {
            IBNotification not = all.get(key);
            if (null == not) {
                Log.e(getLogTAG(), "numberOfUnseen() : Notification with SEQ [" + key + "] is null !");
                continue;
            }
            if (not.isArchived()) {
                continue;
            }

            if (not.isDeleted()) {
                continue;
            }

            if (not.getSeenOn() == null) {
                unseen++;
            }

        }
        return unseen;
    }

    public Integer numberOfUnseenConversations() {
        Integer unseen = 0;
        Hashtable<String, IBNotification> all = NotificationModel.getInstance().getAllNotifications();
        Set<String> keys = all.keySet();
        for (String key : keys) {
            IBNotification not = all.get(key);
            if (null == not) {
                Log.e(getLogTAG(), "numberOfUnseen() : Notification with SEQ [" + key + "] is null !");
                continue;
            }
            if (not.isArchived()) {
                continue;
            }

            if (not.isDeleted()) {
                continue;
            }

            if (not.hasUnseenContent() && not.isConversation()) {
                unseen++;
            }
        }
        return unseen;
    }

    public Integer numberOfActionRequried(IBUser user) {
        Integer noRequiringAction = 0;
        Hashtable<String, IBNotification> all = NotificationModel.getInstance().getAllNotifications();
        for (String key : this.sortedKeys) {
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

    public NotificationModelTypeEnum getType() {
        return type;
    }

    public void setType(NotificationModelTypeEnum type) {

        this.type = type;

        switch (type) {
            case PATIENT:
                showPatientNotifications = true;
                showPrivateMessageNotifications = false;
                break;
            case ALERT:
                showAlertNotifications = true;
                break;
            case INFO:
                showInfoNotifications = true;
                break;
            case PRACTITIONER:
                showPractitionerNotifications = true;
                break;
            case MESSAGE:
                showMessageNotifications = true;
                break;
            case PRIVATE_MESSAGE:
                showPrivateMessageNotifications = true;
                showPatientNotifications = false;
                showAlertNotifications = false;
                showInfoNotifications = false;
                showPractitionerNotifications = false;
                showMessageNotifications = false;
                break;
            case ALL:
                showPatientNotifications = true;
                showAlertNotifications = true;
                showInfoNotifications = true;
                showPractitionerNotifications = true;
                showMessageNotifications = true;
                showPrivateMessageNotifications = true;
            default:
                break;
        }

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

    public boolean isShowMessageNotifications() {
        return showMessageNotifications;
    }

    public void setShowMessageNotifications(boolean showMessageNotifications) {
        this.showMessageNotifications = showMessageNotifications;
    }

    public boolean isShowPrivateMessageNotifications() {
        return showPrivateMessageNotifications;
    }

    public void setShowPrivateMessageNotifications(boolean showPrivateMessageNotifications) {
        this.showPrivateMessageNotifications = showPrivateMessageNotifications;
    }

    public boolean isShowArchivedNotifications() {
        return showArchivedNotifications;
    }

    public void setShowArchivedNotifications(boolean showArchivedNotifications) {
        this.showArchivedNotifications = showArchivedNotifications;
    }

    public boolean isShowDeletedNotifications() {
        return showDeletedNotifications;
    }

    public void setShowDeletedNotifications(boolean showDeletedNotifications) {
        this.showDeletedNotifications = showDeletedNotifications;
    }

    public boolean isShowUnreadOnly() {
        return showUnreadOnly;
    }

    public void setShowUnreadOnly(boolean showUnreadOnly) {
        this.showUnreadOnly = showUnreadOnly;
    }

    public Integer getNotificationsPerPage() {
        return notificationsPerPage;
    }

    public void setNotificationsPerPage(Integer notificationsPerPage) {
        this.notificationsPerPage = notificationsPerPage;
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
        String key = sortedKeys.get(index);
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
        Hashtable<String, IBNotification> all = NotificationModel.getInstance().getAllNotifications();
        if (all.size() == 0) {
            return;
        }

        ArrayList<IBNotification> ar = new ArrayList<>();
        for (IBNotification not : all.values()) {
            boolean keeper = shouldKeep(not);
            if (keeper) {
                ar.add(not);
            }
        }

        if (ar.size() == 0) {
            return;    // no keepers
        }

        for (IBNotification not : ar) {
            sortedKeys.add(not.getSeq());
        }

        Collections.sort(sortedKeys, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return Integer.parseInt(o2) - Integer.parseInt(o1);
            }
        });

        if (null != oldCursor) {
            if (ar.contains(oldCursor)) {
                cursorIndex = ar.indexOf(oldCursor);
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

            if (showArchivedNotifications) {
                return false;
            } else {
                return true;
            }
        }

    }

    private boolean contentMatchesFilter(IBNotification notification) {

        // todo : patient/info/alert are breaking this shit , because backend is sending "patient" level for
        // todo : level , ie the backend does not behave as "mailbox" dispatcher

        if (notification.isDeleted() && !showDeletedNotifications) {
            return false;
        }

        if (notification.isAppointment()) {
            return keepAppointments;
        }

        if (notification.isPrivateMessage()) {
            return showPrivateMessageNotifications;
        }
        if (notification.isPatient() && showPatientNotifications) {
            return true;
        }
        if (notification.isInfo() && showInfoNotifications) {
            return true;
        }
        if (notification.isAlert() && showAlertNotifications) {
            return true;
        }
        if (notification.isMessage() && showMessageNotifications) {
            return true;
        }
        if (notification.isPractitioner() && showPractitionerNotifications) {
            return true;
        }
        return false;
    }

    private boolean shouldKeepPatient(IBNotification not) {
        boolean keeper;
        if (patientSelector.size() > 0) {
            if ((null != not.getPatientGuid()) &&
                    !patientSelector.contains(not.getPatientGuid())) {
                keeper = false;
            } else {
                keeper = true;
            }
        } else {
            // empty patient selector means all patients
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

    private final static String CLASSTAG = kModulePrefix + "." + NotificationsModelFilter.class.getSimpleName();
    @GSONexcludeOutbound
    private String TAG;
    private static int lifeTimeInstances;
    private static int numberOfInstances;
    @GSONexcludeOutbound
    private int instanceNumber;
    @GSONexcludeOutbound
    private static boolean classCountable = false;

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
