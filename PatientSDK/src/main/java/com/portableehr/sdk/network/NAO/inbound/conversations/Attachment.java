package com.portableehr.sdk.network.NAO.inbound.conversations;


import android.util.Base64;
import android.util.Log;

public class Attachment {
    private String id;
    private String name;
    private String mimeType;
    private String b64;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getB64() {
        return b64;
    }

    public void setB64(String b64) {
        this.b64 = b64;
    }

    public byte[] getDocument() {
        String b64 = this.getB64();
        byte[] messageBytes;
        if (b64 != null) {
            try {
                byte[] bytes = b64.getBytes();
                messageBytes = Base64.decode(bytes, 0);
            } catch (Exception e) {
                Log.wtf("EntryAttachment", "getMessage: Caucht exception while bessageB64", e);
                return null;
            }
            return messageBytes;
        } else {
            return null;
        }
    }

}
