package com.portableehr.sdk.network.enums;

import androidx.annotation.NonNull;

/**
 * Created by : yvesleborg
 * Date       : 2020-08-24
 * <p>
 * Copyright Portable Ehr Inc, 2020
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
public enum PatientGenderEnum {
    M("M"),
    F("F"),
    NON_BINARY("N");

    private final String text;


    PatientGenderEnum(String textValue) {
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
