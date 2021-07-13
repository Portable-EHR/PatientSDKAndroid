package com.portableehr.patientsdk.protocols;

import com.portableehr.sdk.network.NAO.inbound.IBService;


public interface IServiceConsumer {
    void onServiceClicked(IBService service);

}
