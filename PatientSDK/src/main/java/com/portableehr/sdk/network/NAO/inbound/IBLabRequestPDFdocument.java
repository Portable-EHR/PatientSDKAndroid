package com.portableehr.sdk.network.NAO.inbound;

import java.util.Date;

/**
 * Created by : yvesleborg
 * Date       : 2017-04-03
 *
 * Copyright Portable Ehr Inc, 2019
 */

public class IBLabRequestPDFdocument {
    private Date documentDate;
    private int seq;
    private IBMedia media;

    public Date getDocumentDate() {
        return documentDate;
    }

    public void setDocumentDate(Date documentDate) {
        this.documentDate = documentDate;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public IBMedia getMedia() {
        return media;
    }

    public void setMedia(IBMedia media) {
        this.media = media;
    }
}
