package com.portableehr.patientsdk.protocols;

import com.portableehr.sdk.network.NAO.inbound.IBPrivateMessage;
import com.portableehr.sdk.network.NAO.inbound.IBPrivateMessageInfo;


public interface IPrivateMessageProvider {

    IBPrivateMessageInfo getPrivateMessageInfo();

    IBPrivateMessage getPrivateMessage();

}
