package com.portableehr.patientsdk.protocols;

import com.portableehr.sdk.network.NAO.inbound.IBNotification;

public interface INotificationConsumer {
    void onNotificationClicked(IBNotification notification);
}
