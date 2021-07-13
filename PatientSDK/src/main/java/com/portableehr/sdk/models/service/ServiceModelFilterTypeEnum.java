package com.portableehr.sdk.models.service;

/**
 * Created by : yvesleborg
 * Date       : 2018-01-24
 * <p>
 * Copyright Portable Ehr Inc, 2018
 */

public enum ServiceModelFilterTypeEnum {
    ALL(0),
    SUBSCRIBED(1),
    AVAILABLE(2),
    REQUIRED_EULA(3);

    private int _value;

    ServiceModelFilterTypeEnum(int value) {
        this._value = value;
    }

    public int getValue() {
        return _value;
    }

    public static ServiceModelFilterTypeEnum fromInt(int i) {
        for (ServiceModelFilterTypeEnum b : ServiceModelFilterTypeEnum.values()) {
            if (b.getValue() == i) {
                return b;
            }
        }
        return null;
    }
}
