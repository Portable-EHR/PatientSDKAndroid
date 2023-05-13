package com.portableehr.sdk.network.NAO.inbound.conversations;

import java.util.List;

public class ConversationEntryPayload {
    private String text;
    private List<Attachment> attachments;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

}
