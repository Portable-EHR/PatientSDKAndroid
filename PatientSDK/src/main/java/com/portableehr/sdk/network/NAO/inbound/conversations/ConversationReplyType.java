package com.portableehr.sdk.network.NAO.inbound.conversations;

import com.google.gson.annotations.SerializedName;


public enum ConversationReplyType {

    @SerializedName("free_text") FREE_TEXT("free_text"),
    @SerializedName("date") DATE("date"),
    @SerializedName("date_time") DATE_TIME("date_time"),
    @SerializedName("choice") CHOICE("choice");

    private final String text;


    ConversationReplyType(String textValue) {
        this.text = textValue;
    }

    @Override
    public String toString() {
        return text;
    }


}
