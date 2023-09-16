package com.portableehr.sdk.network.enums;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;


public enum ConversationEntryType {

    @SuppressWarnings("unused")
    @SerializedName("move") MOVE("move"),
    @SerializedName("status_change") STATUS_CHANGE("status_change"),
    @SerializedName("participant") PARTICIPANT("participant"),
    @SerializedName("share") SHARE("share");

    @NonNull
    private final String text;


    ConversationEntryType(@NonNull String textValue) {
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
