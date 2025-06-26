package com.portableehr.patient.sdk.network.NAO.responses;

import com.portableehr.sdk.network.NAO.inbound.IBConsent;

import java.util.List;

public class ConsentsResponseContent {

    public List<IBConsent> user;
    public Patients patients;

    public static class Patients {
        public List<IBConsent> all;
    }
}
