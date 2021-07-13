package com.portableehr.sdk.models.notification;

import android.util.Log;

import com.portableehr.sdk.network.NAO.inbound.IBMessageContent;
import com.portableehr.sdk.network.NAO.inbound.IBMessageDistribution;
import com.portableehr.sdk.network.NAO.inbound.IBNotification;
import com.portableehr.sdk.network.NAO.inbound.IBUser;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by : yvesleborg
 * Date       : 2017-04-04
 * <p>
 * Copyright Portable Ehr Inc, 2017-2019
 */

public abstract class AbstractInbox {


    Integer cursorIndex;

    ArrayList<String>   sortedKeys;
    ArrayList<String>   patientSelector;
    InboxCapabilityEnum kind;
    InboxFilter         mFilter;

    private Hashtable<String, IBNotification> mAllNotifications;

    public AbstractInbox() {
        sortedKeys = new ArrayList<>();
        patientSelector = new ArrayList<>();
        mAllNotifications = new Hashtable<>();
        kind = this.getKind();
        mFilter = new InboxFilter(this);
    }


    public Integer numberOfUnseen() {
        getFilter().refreshFilter();
        return getFilter().numberOfUnseen();
    }

    public Integer numberOfActionRequired(IBUser user) {
        return getFilter().numberOfActionRequired(user);
    }

    private boolean shouldKeep(IBNotification notification) {
        if (null == notification.getCapabilityAlias()) {
            Log.e(getLogTAG(), "shouldKeep: notification has no capabilityAlias, skipping : " + notification.getSeq());
            return false;
        }
        return notification.getCapabilityAlias().equals(getCapabilityAlias());
    }

    public abstract String getCapabilityAlias();

    public abstract NotificationModelTypeEnum getType();

    public abstract InboxCapabilityEnum getKind();

    public abstract void sort(ArrayList<IBNotification> set);

    public void refreshContent() {

        Hashtable<String, IBNotification> newContent = NotificationModel.getInstance().getInboxContent(this);
        if (getVerbose()) {
            Log.d(getLogTAG(), "refreshContent: have " + newContent.size() + "[" + this.getKind() + "]");
        }
        this.populateInbox(newContent);
    }

    public void populateInbox(Hashtable<String, IBNotification> newContent) {

        boolean wasModified = false;
        if (newContent.size() != mAllNotifications.size()) {
            wasModified = true;
        }

        if (!mAllNotifications.keySet().containsAll(newContent.keySet())) {
            wasModified = true;
        }

        mAllNotifications = newContent;

        if (wasModified) {
            if (getVerbose()) {
                Log.d(getLogTAG(), "populateInbox: a modifications to inbox " + getCapabilityAlias());
            }
        }

        if (getVerbose()) {
            Log.d(getLogTAG(), "Inbox contains " + mAllNotifications.size() + " items of type " + getType());
        }
        getFilter().refreshFilter();

    }

    public Hashtable<String, IBNotification> getContent() {
        return mAllNotifications;
    }


    public InboxFilter getFilter() {
        return mFilter;
    }

    /*
     * cursor control shit
     */

    public int getCount() {
        if (mAllNotifications != null) {
            return mAllNotifications.keySet().size();
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

    private void refreshFilter() {
        // only get here if the content of the
        Hashtable<String, IBNotification> content = NotificationModel.getInstance().getInboxContent(this);

        IBNotification oldCursor = this.getCursor();

        cursorIndex = 0;
        sortedKeys.clear();
        Hashtable<String, IBNotification> all = content;
        if (all.size() == 0) {
            return;
        }

        ArrayList<IBNotification> ar = new ArrayList<>(all.values());
        sort(ar);

        for (IBNotification not : ar) {
            sortedKeys.add(not.getSeq());
        }

        if (null != oldCursor) {
            if (ar.contains(oldCursor)) {
                cursorIndex = ar.indexOf(oldCursor);
            } else {
                Log.w(getLogTAG(), "Refresh flushed our old cursor, reseting to top.");
                cursorIndex = 0;
            }
        }

        // todo : should update cursor
        // todo : cursor does not belong in a Inbox

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


    abstract String getLogTAG();

    abstract boolean getVerbose();

}
