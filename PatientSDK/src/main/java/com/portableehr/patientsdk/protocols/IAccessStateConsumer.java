package com.portableehr.patientsdk.protocols;


import androidx.annotation.Nullable;

import com.portableehr.sdk.network.NAO.inbound.patient.consent.AccessState;

public interface IAccessStateConsumer {
    void setAccessState(@Nullable AccessState accessState);
}
