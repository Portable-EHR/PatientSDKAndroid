package com.portableehr.sdk.network.enums;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;


public enum ConversationEntryPayloadAction {

    @SuppressWarnings("unused")
    @SerializedName("remove") REMOVE("remove");

    @NonNull
    private final String text;


    ConversationEntryPayloadAction(@NonNull String textValue) {
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
