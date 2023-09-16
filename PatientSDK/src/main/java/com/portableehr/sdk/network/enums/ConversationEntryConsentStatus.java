package com.portableehr.sdk.network.enums;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;


public enum ConversationEntryConsentStatus {

    @SuppressWarnings("unused")
    @SerializedName("active") ACTIVE("active"),
    @SerializedName("revoked") REVOKED("revoked"),
    @SerializedName("expired") EXPIRED("expired"),
    @SerializedName("not_found") NOT_FOUND("not_found"),
    @SerializedName("invalid") INVALID("invalid");

    @NonNull
    private final String text;


    ConversationEntryConsentStatus(@NonNull String textValue) {
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
