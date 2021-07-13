package com.portableehr.sdk.network.NAO.responses;

import com.portableehr.sdk.network.NAO.inbound.IBDeviceInfo;

import java.util.HashMap;

/**
 * Created by : yvesleborg
 * Date       : 2019-01-07
 * <p>
 * Copyright Portable Ehr Inc, 2019
 */

public class IBDevicesListResponse {

    private HashMap<String, IBDeviceInfo> devices;

    public HashMap<String,IBDeviceInfo> getDevices() {
        return devices;
    }

    public void setDevices(HashMap<String,IBDeviceInfo> devices) {
        this.devices = devices;
    }
}
