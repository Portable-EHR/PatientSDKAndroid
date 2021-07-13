package com.portableehr.sdk.network.NAO.inbound;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * Created by : yvesleborg
 * Date       : 2020-05-31
 * <p>
 * Copyright Portable Ehr Inc, 2020
 */
@SuppressWarnings("unused")


public enum TextRendererEnum {
    @SerializedName("markdown") MARKDOWN("markdown"),
    @SerializedName("html") HTML("html"),
    @SerializedName("text") TEXT("text");

    @NonNull
    private final String text;


    TextRendererEnum(@NonNull String textValue) {
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




