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
public enum FulfillmentStateEnum {
    @SerializedName("pending") PENDING("pending"),
    @SerializedName("fulfilled") FULFILLED("fulfilled"),
    @SerializedName("expired") EXPIRED("expired"),
    @SerializedName("seen") SEEN("seen");

    @NonNull
    private final String text;


    FulfillmentStateEnum(@NonNull String textValue) {
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
