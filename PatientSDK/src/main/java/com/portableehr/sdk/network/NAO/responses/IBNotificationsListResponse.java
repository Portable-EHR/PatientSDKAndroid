package com.portableehr.sdk.network.NAO.responses;

import com.portableehr.sdk.network.NAO.inbound.IBNotification;

import java.util.HashMap;

/**
 * Created by : yvesleborg
 * Date       : 2017-04-14
 * <p>
 * Copyright Portable Ehr Inc, 2019
 */

public class IBNotificationsListResponse {

    private HashMap<String, IBNotification> notifications;

    public HashMap<String,IBNotification> getNotifications() {
        return notifications;
    }

    public void setNotifications(HashMap<String,IBNotification> notifications) {
        this.notifications = notifications;
    }
}
