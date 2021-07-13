package com.portableehr.patientsdk.models.consent;


import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public enum SharableKindEnum {
    @SerializedName("privateMessage") PRIVATE_MESSAGE("privateMessage"),
    @SerializedName("appointment") APPOINTMENT("appointment"),
    @SerializedName("record") RECORD("record");

    @NonNull
    private final String text;


    SharableKindEnum(@NonNull String textValue) {
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

