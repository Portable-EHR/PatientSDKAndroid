package com.portableehr.sdk.network.enums;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;


public enum ConversationEntryPayloadType {

    @SuppressWarnings("unused")
    @SerializedName("private_message") PRIVATE_MESSAGE("private_message");

    @NonNull
    private final String text;


    ConversationEntryPayloadType(@NonNull String textValue) {
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
