package com.portableehr.sdk.network.ehrApi;

/**
 * Created by : yvesleborg
 * Date       : 2017-04-04
 * <p>
 * Copyright Portable Ehr Inc, 2019
 */

public class EHRServerResponse {
    EHRRequestStatus requestStatus;
//    Object           responseContent;

    public EHRRequestStatus getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(EHRRequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }

//    public Object getResponseContent() {
//        return responseContent;
//    }
//
//    public void setResponseContent(Object responseContent) {
//        this.responseContent = responseContent;
//    }
}
