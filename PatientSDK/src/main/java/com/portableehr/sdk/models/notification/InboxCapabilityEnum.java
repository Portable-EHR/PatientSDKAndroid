package com.portableehr.sdk.models.notification;

/**
 * Created by : yvesleborg
 * Date       : 2017-04-04
 * <p>
 * Copyright Portable Ehr Inc, 2017-2019
 */

public enum InboxCapabilityEnum {
    NOTIFICATIONS(0),
    PRIVATE_MESSAGES(1),
    APPOINTMENTS(2);

    private static String _coreNotifications = "core.notification";
    private static String _privateMessages   = "core.privateMessage";
    private static String _appointments      = "core.appointment";

    private int _value;

    InboxCapabilityEnum(int value) {
        this._value = value;
    }

    public int getValue() {
        return _value;
    }

    public static InboxCapabilityEnum fromInt(int i) {
        for (InboxCapabilityEnum b : InboxCapabilityEnum.values()) {
            if (b.getValue() == i) {
                return b;
            }
        }
        return null;
    }

    public static InboxCapabilityEnum forCapability(String capabilityAlias) throws Exception {
        if (capabilityAlias.equals(_appointments)) {
            return InboxCapabilityEnum.APPOINTMENTS;
        }
        if (capabilityAlias.equals(_coreNotifications)) {
            return InboxCapabilityEnum.NOTIFICATIONS;
        }
        if (capabilityAlias.equals(_privateMessages)) {
            return InboxCapabilityEnum.PRIVATE_MESSAGES;
        }
        throw new Exception("No inbox capability for capatility : " + capabilityAlias);
    }
}
