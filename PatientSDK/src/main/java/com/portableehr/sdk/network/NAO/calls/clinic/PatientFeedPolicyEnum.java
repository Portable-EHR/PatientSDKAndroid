package com.portableehr.sdk.network.NAO.calls.clinic;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * Created by : yvesleborg
 * Date       : 2020-07-05
 * <p>
 * Copyright Portable Ehr Inc, 2020
 */
@SuppressWarnings("unused")
public enum PatientFeedPolicyEnum {


    @SuppressWarnings("unused")
    @SerializedName("manual") MANUAL("manual"),
    @SerializedName("automatic") AUTOMATIC("automatic"),
    @SerializedName("never") NEVER("never");

    @NonNull
    private final String text;


    PatientFeedPolicyEnum(@NonNull String textValue) {
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
