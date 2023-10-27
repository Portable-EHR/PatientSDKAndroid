package com.portableehr.sdk.network.NAO.inbound.conversations;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;


public enum MentionedParticipantState {

    @SerializedName("enabled") ENABLED("enabled"),
    @SerializedName("disabled") DISABLED("disabled"),
    @SerializedName("canceled") CANCELED("canceled"),
    @SerializedName("sent") SENT("sent");

    private final String text;


    MentionedParticipantState(String textValue) {
        this.text = textValue;
    }

    @Override
    public String toString() {
        return text;
    }


}
