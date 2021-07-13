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
public enum FulfillmentMethodEnum {
    @SerializedName("qrScanByOfferer") QR_SCAN_BY_OFFERER("qrScanByOfferer"),
    @SerializedName("qrScanByOfferee") QR_SCAN_BY_OFFEREE("qrScanByOfferee"),
    @SerializedName("feed") FEED("feed"),
    @SerializedName("notification") NOTIFICATION("notification");

    @NonNull
    private final String text;


    FulfillmentMethodEnum(@NonNull String textValue) {
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
