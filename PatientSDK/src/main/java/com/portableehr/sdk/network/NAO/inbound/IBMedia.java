package com.portableehr.sdk.network.NAO.inbound;

/**
 * Created by : yvesleborg
 * Date       : 2017-04-01
 *
 * Copyright Portable Ehr Inc, 2019
 */

public class IBMedia {

    private String guid;
    private String fileName;
    private String mediaType;
    private String mediaLocation;
    private String content;

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getMediaLocation() {
        return mediaLocation;
    }

    public void setMediaLocation(String mediaLocation) {
        this.mediaLocation = mediaLocation;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
