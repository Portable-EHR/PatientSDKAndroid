package com.portableehr.sdk.network.enums;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * Created by : yvesleborg
 * Date       : 2020-11-06
 * <p>
 * Copyright Portable Ehr Inc, 2020
 */

@SuppressWarnings("unused")
public enum AdmissionLocationEnum {
    @SuppressWarnings("unused")
    @SerializedName("home") HOME("home"),
    @SerializedName("clinic") CLINIC("clinic"),
    @SerializedName("both") BOTH("both");

    private final String text;

    AdmissionLocationEnum(String textValue) {
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
