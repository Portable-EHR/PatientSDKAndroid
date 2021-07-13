package com.portableehr.sdk.network.NAO.inbound;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.portableehr.sdk.network.NAO.inbound.patient.appointment.Appointment;
import com.portableehr.sdk.network.NAO.inbound.patient.notification.ReplyEnvelope;
import com.portableehr.sdk.network.gson.GSONexcludeOutbound;
import com.portableehr.sdk.network.gson.GsonFactory;

import java.net.URL;
import java.util.Date;

import static com.portableehr.sdk.EHRLibRuntime.kModulePrefix;

/**
 * Created by : yvesleborg
 * Date       : 2017-04-01
 * <p>
 * Copyright Portable Ehr Inc, 2019
 */

public class IBNotification implements Comparable<IBNotification> {

    {
        setClassCountable(false);
    }

    private String               guid;
    private String               capabilityAlias;
    private String               subject;
    private String               text;
    private String               textRenderer;
    private String               summary;
    private String               seq;
    private String               capabilityGuid;
    private boolean              confidential;
    private String               senderName;
    private String               notificationLevel; // info|patient|alert
    private String               aboutType;
    private String               aboutGuid;
    private String               payloadType;
    private Date                 seenOn;
    private Date                 createdOn;
    private Date                 ackedOn;
    @Nullable
    private Date                 archivedOn;
    @Nullable
    private Date                 deletedOn;
    @Nullable
    private Date                 expiresOn;
    private String               patientGuid;
    private String               practitionerGuid;
    private String               progress;
    private String               thumb;
    private URL                  url;
    private Date                 lastUpdated;
    @Nullable
    private Date                 lastSeen;
    private IBLabRequest         labRequest;
    private IBLabResult          labResult;
    private IBMessageContent     message;
    private IBPrivateMessageInfo telexInfo;
    @Nullable
    private Appointment          appointment;
    @Nullable
    private ReplyEnvelope        replyEnvelope;
    private IBAnnotation         annotation;
    private IBDeviceInfo         deviceInfo;

    /*  backend , 1.1(023)

        aboutType

        const _NONE                 = 'none';
        const _RECORD               = 'record';
        const _AUTHORIZATION        = 'authorization';
        const _PRIVATE_MESSAGE      = 'privateMessage';
        const _MESSAGE              = 'message';
        const _MESSAGE_DISTRIBUTION = 'messageDistribution';
        const _APP_INCIDENT         = "appIncident";
        const _DEVICE               = "device";

        payloadType

        const _TEXT            = "text";
        const _APP_ALERT       = "appAlert";
        const _AUTHORIZATION   = "authorization";
        const _PRIVATE_MESSAGE = "privateMessage";
        const _MESSAGE         = "message";
        const _RECORD          = "record";
        const _NONE            = "none";
        const _ANNOTATION      = "annotation";
        const _APP_INCIDENT    = "appIncident";

        notificationLevel

        const _PATIENT = 'patient';              // notifications for a patient
        const _ALERT   = 'alert';                // any service alert (accept/reject type)
        const _INFO    = 'info';                 // any service info (new, news from subscribed, etc ...)

     */


    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getCapabilityAlias() {
        return capabilityAlias;
    }

    public void setCapabilityAlias(String capabilityAlias) {
        this.capabilityAlias = capabilityAlias;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isConfidential() {
        return confidential;
    }

    public boolean isArchived() {
        if (null == archivedOn) {
            return false;
        }

        if (progress.contentEquals("archived")) {
            return true;
        }
        return false;
    }

    public boolean isDeleted() {
        return null != deletedOn;
    }

    public boolean isMessage() {
        if (this.message != null) {
            return true;
        }
        return false;
    }

    public boolean isPrivateMessage() {

        // private messages are not embedded in the notification for security reasons
        // they can never persist on the device. Instead we get a guid

        if (payloadType.contentEquals("privateMessage")) {
            return (null != aboutGuid);
        }
        return false;
    }

    public boolean isAppointment() {

        // appointments will be sent to an inbox (keyed on capability alias

        return (getCapabilityAlias().contentEquals("core.appointment"));

    }

    public boolean isAlert() {
        if (null == notificationLevel) {
            return false;
        }
        if (notificationLevel.contentEquals("alert")) {
            return true;
        }
        return false;
    }

    public boolean isInfo() {
        if (null == notificationLevel) {
            return false;
        }
        if (notificationLevel.contentEquals("info")) {
            return true;
        }
        return false;
    }

    public boolean isPractitioner() {
        if (null != practitionerGuid) {
            return true;
        }
        return false;
    }

    public boolean isPatient() {
        if (null == notificationLevel) {
            return false;
        }
        if (notificationLevel.contentEquals("patient")) {
            return true;
        }
        return false;
    }

    public boolean isExpired() {
        if (null == expiresOn) {
            return false;
        }
        Date now = new Date();
        if (now.compareTo(expiresOn) > 0) {
            return true;
        }
        return false;
    }

    public boolean isAcknowledged() {
        if (null != ackedOn) {
            return true;
        }
        if (null != progress && progress.contentEquals("acknowledged")) {
            return true;
        }

        if (isPrivateMessage()) {
            if (this.telexInfo != null && null != this.telexInfo.getAcknowledgedOn()) {
                return true;
            }
        }

        return false;
    }

    public boolean isSeen() {
        if (null != seenOn) {
            return true;
        }
        return false;
    }

    public boolean isActionRequired(@SuppressWarnings("unused") IBUser user) {
        if (isAppointment()) {
            return (getAppointment() != null) && (getAppointment().isActionRequired() || (getAppointment().isInPlay() && hasUnseenContent()));
        } else if (isPrivateMessage()) {
            if (this.telexInfo != null && null == this.telexInfo.getAcknowledgedOn()) {
                return true;
            }
        }

        return envelopeActionRequired() || hasUnseenContent();
    }

    public boolean hasUnseenContent() {
        if (null == lastSeen) {
            return isSeen();
        }
        if (null == seenOn) {
            return true;
        }
        if (null == lastUpdated) {
            Log.w(TAG, "hasUnseenContent: Notification has no lastUpdated !" + getSeq());
            return false;
        }
        if (lastSeen.getTime() < lastUpdated.getTime()) {
            return true;
        }
        return false;
    }

    public boolean hasReplyEnvelope() {
        return this.replyEnvelope != null;
    }

    private boolean envelopeActionRequired() {
        if (isArchived()) {
            return false;
        }
        if (isExpired()) {
            return false;
        }
        return !isSeen();
    }

    public void setConfidential(boolean confidential) {
        this.confidential = confidential;
    }

    public String getAboutGuid() {
        return aboutGuid;
    }

    public void setAboutGuid(String aboutGuid) {
        this.aboutGuid = aboutGuid;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public Date getSeenOn() {
        return seenOn;
    }

    public void setSeenOn(Date seenOn) {
        this.seenOn = seenOn;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getAckedOn() {
        return ackedOn;
    }

    public void setAckedOn(Date ackedOn) {
        this.ackedOn = ackedOn;
    }

    @Nullable
    public Date getExpiresOn() {
        return expiresOn;
    }

    public void setExpiresOn(@Nullable Date expiresOn) {
        this.expiresOn = expiresOn;
    }

    @Nullable
    public Date getArchivedOn() {
        return archivedOn;
    }

    public void setArchivedOn(@Nullable Date archivedOn) {
        this.archivedOn = archivedOn;
    }

    @Nullable
    public Date getDeletedOn() {
        return deletedOn;
    }

    public void setDeletedOn(@Nullable Date deletedOn) {
        this.deletedOn = deletedOn;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Nullable
    public Date getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(@Nullable Date lastSeen) {
        this.lastSeen = lastSeen;
    }

    public String getPatientGuid() {
        return patientGuid;
    }

    public void setPatientGuid(String patientGuid) {
        this.patientGuid = patientGuid;
    }

    public String getPractitionerGuid() {
        return practitionerGuid;
    }

    public void setPractitionerGuid(String practitionerGuid) {
        this.practitionerGuid = practitionerGuid;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public IBPrivateMessageInfo getTelexInfo() {
        return telexInfo;
    }

    public void setTelexInfo(IBPrivateMessageInfo telexInfo) {
        this.telexInfo = telexInfo;
    }

    @Nullable
    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(@Nullable Appointment appointment) {
        this.appointment = appointment;
    }

    public IBLabRequest getLabRequest() {
        return labRequest;
    }

    public void setLabRequest(IBLabRequest labRequest) {
        this.labRequest = labRequest;
    }

    public IBLabResult getLabResult() {
        return labResult;
    }

    public void setLabResult(IBLabResult labResult) {
        this.labResult = labResult;
    }

    public IBMessageContent getMessageContent() {
        return message;
    }

    public void setMessageContent(IBMessageContent message) {
        this.message = message;
    }

    public IBAnnotation getAnnotation() {
        return annotation;
    }

    public void setAnnotation(IBAnnotation annotation) {
        this.annotation = annotation;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTextRenderer() {
        return textRenderer;
    }

    public void setTextRenderer(String textRenderer) {
        this.textRenderer = textRenderer;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getCapabilityGuid() {
        return capabilityGuid;
    }

    public void setCapabilityGuid(String capabilityGuid) {
        this.capabilityGuid = capabilityGuid;
    }

    public String getNotificationLevel() {
        return notificationLevel;
    }

    public void setNotificationLevel(String notificationLevel) {
        this.notificationLevel = notificationLevel;
    }

    public String getAboutType() {
        return aboutType;
    }

    public void setAboutType(String aboutType) {
        this.aboutType = aboutType;
    }

    public String getPayloadType() {
        return payloadType;
    }

    public void setPayloadType(String payloadType) {
        this.payloadType = payloadType;
    }

    public IBMessageContent getMessage() {
        return message;
    }

    public void setMessage(IBMessageContent message) {
        this.message = message;
    }

    public IBDeviceInfo getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(IBDeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    @Nullable
    public ReplyEnvelope getReplyEnvelope() {
        return replyEnvelope;
    }

    public void setReplyEnvelope(@Nullable ReplyEnvelope replyEnvelope) {
        this.replyEnvelope = replyEnvelope;
    }

    public void updateWith(IBNotification fresh) {
        guid = fresh.guid;
        seq = fresh.seq;
        text = fresh.text;
        textRenderer = fresh.textRenderer;
        summary = fresh.summary;
        capabilityGuid = fresh.capabilityGuid;
        capabilityAlias = fresh.capabilityAlias;
        confidential = fresh.confidential;
        senderName = fresh.senderName;
        notificationLevel = fresh.notificationLevel;
        aboutType = fresh.aboutType;
        aboutGuid = fresh.aboutGuid;
        payloadType = fresh.payloadType;
        seenOn = fresh.seenOn;
        createdOn = fresh.createdOn;
        ackedOn = fresh.ackedOn;
        archivedOn = fresh.archivedOn;
        deletedOn = fresh.deletedOn;
        expiresOn = fresh.expiresOn;
        patientGuid = fresh.patientGuid;
        practitionerGuid = fresh.practitionerGuid;
        thumb = fresh.thumb;
        url = fresh.url;
        lastUpdated = fresh.lastUpdated;
        lastSeen = fresh.lastSeen;
        message = fresh.message;
        labRequest = fresh.labRequest;
        labResult = fresh.labResult;
        annotation = fresh.annotation;
        deviceInfo = fresh.deviceInfo;
        appointment = fresh.appointment;
        replyEnvelope = fresh.replyEnvelope;
        progress = fresh.progress;

        if (null == telexInfo) {
            telexInfo = fresh.telexInfo;
        } else {
            if (null == fresh.telexInfo) {
                telexInfo = null;
            } else {
                telexInfo.updateWith(fresh.telexInfo);
            }
        }

        if (null == appointment) {
            appointment = fresh.appointment;
        } else {
            if (null == fresh.appointment) {
                appointment = null;
            } else {
                appointment.updateWith(fresh.appointment);
            }
        }

    }

    @Override
    public int compareTo(@NonNull IBNotification o) {
        int thisInt  = Integer.parseInt(this.seq);
        int otherInt = Integer.parseInt(o.getSeq());
        return (otherInt - thisInt);                // descending on seq
    }


    public boolean equals(IBNotification other) {
        if (other == null) {
            return false;
        }

        int thisHash  = this.asJson().hashCode();
        int otherHash = other.asJson().hashCode();
        return (thisHash == otherHash);

    }

    public IBNotification() {
        super();
        onNew();
    }


    //region GSON helpers


    public String asJson() {
        GsonBuilder builder        = GsonFactory.standardBuilder();
        Gson        jsonSerializer = builder.create();
        String      theJson        = jsonSerializer.toJson(this, this.getClass());
        return theJson;
    }

    public static IBNotification fromJson(String json) {
        GsonBuilder    builder          = GsonFactory.standardBuilder();
        Gson           jsonDeserializer = builder.create();
        IBNotification theObject        = jsonDeserializer.fromJson(json, IBNotification.class);
        return theObject;
    }

    //endregion


    //region Countable

    private final static String  CLASSTAG       = kModulePrefix + "." + IBNotification.class.getSimpleName();
    @GSONexcludeOutbound
    private              String  TAG;
    private static       int     lifeTimeInstances;
    private static       int     numberOfInstances;
    @GSONexcludeOutbound
    private              int     instanceNumber;
    @GSONexcludeOutbound
    private static       boolean classCountable = false;

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        numberOfInstances--;
        if (numberOfInstances < 0) {
            Log.e(getLogTAG(), "*** You did not call onNew in your ctor(s)");
        }
        if (classCountable) {
            Log.d(getLogTAG(), "finalize  ");
        }
    }

    protected void onNew() {
        TAG = CLASSTAG;
        numberOfInstances++;
        lifeTimeInstances++;
        instanceNumber = lifeTimeInstances;
        if (classCountable) {
            Log.d(getLogTAG(), "onNew ");
        }
    }

    private String getLogLabel() {
        return Integer.toHexString(instanceNumber) + "/" + Integer.toHexString(numberOfInstances);
    }

    public static void setClassCountable(boolean isIt) {
        classCountable = isIt;
    }

    private String getLogTAG() {
        TAG = CLASSTAG + " [" + getLogLabel() + "] ";
        return TAG;
    }

    //endregion


}
