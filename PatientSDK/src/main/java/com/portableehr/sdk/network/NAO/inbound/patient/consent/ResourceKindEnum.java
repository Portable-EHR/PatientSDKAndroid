package com.portableehr.sdk.network.NAO.inbound.patient.consent;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * Created by : yvesleborg
 * Date       : 2020-01-14
 * <p>
 * Copyright Portable Ehr Inc, 2020
 */
@SuppressWarnings("unused")
public enum ResourceKindEnum {
    @SerializedName("unknown") UNKNOWN("unknown"),
    @SerializedName("privateMessage") PRIVATE_MESSAGE("privateMessage"),
    @SerializedName("appointment") APPOINTMENT("appointment"),
    @SerializedName("record") RECORD("record"),
    @SerializedName("identity") IDENTITY("identity");

    @NonNull
    private final String text;


    ResourceKindEnum(@NonNull String textValue) {
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
