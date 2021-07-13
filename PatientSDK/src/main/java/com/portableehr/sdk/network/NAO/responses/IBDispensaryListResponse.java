package com.portableehr.sdk.network.NAO.responses;

import com.portableehr.sdk.network.NAO.inbound.IBDispensary;

import java.util.HashMap;

/**
 * Created by : yvesleborg
 * Date       : 2018-06-17
 * <p>
 * Copyright Portable Ehr Inc, 2019
 */

public class IBDispensaryListResponse {

    private HashMap<String, IBDispensary> dispensaries;

    public HashMap<String,IBDispensary> getDispensaries() {
        return dispensaries;
    }

    public void setDispensaries(HashMap<String,IBDispensary> dispensaries) {
        this.dispensaries = dispensaries;
    }
}
