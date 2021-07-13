package com.portableehr.sdk.network.NAO.form;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * Created by : yvesleborg
 * Date       : 2020-05-31
 * <p>
 * Copyright Portable Ehr Inc, 2020
 */
public enum InputImportanceEnum {
    @SerializedName("dontCare") DONT_CARE("dontCare"),
    @SerializedName("should") SHOULD("should"),
    @SerializedName("must") MUST("must");

    @NonNull
    private final String text;


    InputImportanceEnum(@NonNull String textValue) {
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
