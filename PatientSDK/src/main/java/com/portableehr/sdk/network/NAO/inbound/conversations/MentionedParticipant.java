package com.portableehr.sdk.network.NAO.inbound.conversations;

public class MentionedParticipant {
    private String participantId;
    private String reminder;
    private MentionedParticipantReminderMethod reminderMethod;
    private MentionedParticipantState reminderState;

    public String getParticipantId() {
        return participantId;
    }

    public void setParticipantId(String participantId) {
        this.participantId = participantId;
    }

    public String getReminder() {
        return reminder;
    }

    public void setReminder(String reminder) {
        this.reminder = reminder;
    }

    public MentionedParticipantReminderMethod getReminderMethod() {
        return reminderMethod;
    }

    public void setReminderMethod(String reminderMethod) {
        this.reminderMethod = MentionedParticipantReminderMethod.valueOf(reminderMethod);
    }

    public MentionedParticipantState getReminderState() {
        return reminderState;
    }

    public void setReminderState(String reminderState) {
        this.reminderState = MentionedParticipantState.valueOf(reminderState);
    }

    public void setReminderState(MentionedParticipantState reminderState) {
        this.reminderState = reminderState;
    }
}
