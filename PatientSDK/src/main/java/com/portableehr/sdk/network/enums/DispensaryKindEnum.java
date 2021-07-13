package com.portableehr.sdk.network.enums;

import androidx.annotation.NonNull;

/**
 * Created by : yvesleborg
 * Date       : 2019-12-23
 * <p>
 * Copyright Portable Ehr Inc, 2019
 */

/*
            'lab' ,
            'communityCare' ,
            'imagery' ,
            'pharmacy' ,
            'acuteCare' ,
            'virtualCLinic' ,
            'privatePractice'
 */

@SuppressWarnings("unused")
public enum DispensaryKindEnum {
    LAB("lab"),
    COMMUNITY_CARE("communityCare"),
    IMAGERY("imagery"),
    PHARMACY("pharmacy"),
    ACUTE_CARE("acuteCare"),
    VIRTUAL_CLINIC("virtualClinic"),
    PRIVATE_PRACTICE("privatePractice"
    );

    private final String text;


    DispensaryKindEnum(String textValue) {
        this.text = textValue;
    }

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    @NonNull
    public String toString() {
        return text;
    }

}
