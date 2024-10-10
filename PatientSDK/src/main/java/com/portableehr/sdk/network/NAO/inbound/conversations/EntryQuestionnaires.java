package com.portableehr.patient.sdk.network.NAO.inbound.conversations;

import java.util.List;

public class EntryQuestionnaires {

    private String surveyId;
    private String title;

    private List<participants> participants;

    public List<participants> getParticipants() {
        return participants;
    }

    public void setParticipants(List<participants> participants) {
        this.participants = participants;
    }

    public String getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(String surveyId) {
        this.surveyId = surveyId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



}
