package com.portableehr.patientsdk.models.device;

public enum DeviceModelFilterTypeEnum {
    ALL(0);

    private int _value;

    DeviceModelFilterTypeEnum(int value) {
        this._value = value;
    }

    public int getValue() {
        return _value;
    }

    public static DeviceModelFilterTypeEnum fromInt(int i) {
        for (DeviceModelFilterTypeEnum b : DeviceModelFilterTypeEnum.values()) {
            if (b.getValue() == i) {
                return b;
            }
        }
        return null;
    }
}
