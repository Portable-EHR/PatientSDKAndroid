package com.portableehr.sdk.models.notification;

/**
 * Created by : yvesleborg
 * Date       : 2017-04-04
 * <p>
 * Copyright Portable Ehr Inc, 2017-2019
 */

public enum NotificationModelTypeEnum {
    ALL(0),
    PATIENT(1),
    INFO(2),
    ALERT(3),
    PRACTITIONER(4),
    MESSAGE(5),
    PRIVATE_MESSAGE(6),
    APPOINTMENTS(7),
    NOTIFICATIONS(8);

    private int _value;

    NotificationModelTypeEnum(int value) {
        this._value = value;
    }

    public int getValue() {
        return _value;
    }

    public static NotificationModelTypeEnum fromInt(int i) {
        for (NotificationModelTypeEnum b : NotificationModelTypeEnum.values()) {
            if (b.getValue() == i) {
                return b;
            }
        }
        return null;
    }


}
