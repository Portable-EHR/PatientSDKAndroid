package com.portableehr.sdk.network.enums;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;


public enum ConversationParticipantType {

    @SerializedName("staff") STAFF("staff"),
    @SerializedName("admin") ADMIN("admin"),
    @SerializedName("system") SYSTEM("system"),
    @SerializedName("client") CLIENT("client");


    @NonNull
    private final String text;


    ConversationParticipantType(@NonNull String textValue) {
        this.text = textValue;
    }

    @Override
    @NonNull
    public String toString() {
        return text;
    }


}
