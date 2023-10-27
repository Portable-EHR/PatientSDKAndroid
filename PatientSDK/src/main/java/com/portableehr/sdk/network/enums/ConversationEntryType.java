package com.portableehr.sdk.network.enums;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;


public enum ConversationEntryType {

    @SuppressWarnings("unused")
    @SerializedName("move") MOVE("move"),
    @SerializedName("status_change") STATUS_CHANGE("status_change"),
    @SerializedName("participant") PARTICIPANT("participant"),
    @SerializedName("share") SHARE("share"),
    @SerializedName("assign") TASK("assign"),
    @SerializedName("message") MESSAGE("message");

    private final String text;


    ConversationEntryType(String textValue) {
        this.text = textValue;
    }

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return text;
    }


}
