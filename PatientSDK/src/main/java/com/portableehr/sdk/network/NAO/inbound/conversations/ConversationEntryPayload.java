package com.portableehr.sdk.network.NAO.inbound.conversations;

import java.util.List;

public class ConversationEntryPayload {
    private String text;
    private List<EntryAttachment> attachments;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<EntryAttachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<EntryAttachment> attachments) {
        this.attachments = attachments;
    }

}
