package com.portableehr.patientsdk.protocols;

import android.view.View;

import com.portableehr.sdk.network.NAO.inbound.IBDeviceInfo;

public interface IDeviceConsumer {
    void onDeviceClicked(IBDeviceInfo deviceInfo);

    void longTouchOnButton(View menuButton);
}
