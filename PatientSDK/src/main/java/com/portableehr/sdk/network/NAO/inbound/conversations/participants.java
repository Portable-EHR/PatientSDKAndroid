package com.portableehr.patient.sdk.network.NAO.inbound.conversations;

public class participants {

    private String participantUUID;
    private String status;
    private String token;

    public String getParticipantUUID() {
        return participantUUID;
    }

    public void setParticipantUUID(String participantUUID) {
        this.participantUUID = participantUUID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
