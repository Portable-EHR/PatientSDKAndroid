package com.portableehr.sdk.network.NAO.inbound.conversations;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;


public enum MentionedParticipantReminderMethod {

    @SerializedName("email") EMAIL("email"),
    @SerializedName("in_convo") IN_CONVO("in_convo"),
    @SerializedName("sms") SMS("sms");

    private final String text;


    MentionedParticipantReminderMethod(String textValue) {
        this.text = textValue;
    }

    @Override
    public String toString() {
        return text;
    }


}
