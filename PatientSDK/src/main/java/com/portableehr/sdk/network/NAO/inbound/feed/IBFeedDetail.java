package com.portableehr.sdk.network.NAO.inbound.feed;

import androidx.annotation.Nullable;
import android.util.Log;

import com.portableehr.sdk.network.NAO.inbound.IBVersion;
import com.portableehr.sdk.network.gson.GSONexcludeOutbound;

import java.util.Date;
import java.util.HashMap;

import static com.portableehr.sdk.EHRLibRuntime.kModulePrefix;


/**
 * Created by : yvesleborg
 * Date       : 2019-04-25
 * <p>
 * Copyright Portable Ehr Inc, 2019
 */
@SuppressWarnings("unused")
public class IBFeedDetail {

    {
        setClassCountable(false);
    }

    @SuppressWarnings("InnerClassMayBeStatic")
    class SettingsPolicy {

        class FeedSyncPolicy {
            boolean mustSyncStaff;
            boolean mustSyncAppointmentTypes;
            boolean mustSyncAppointmentResources;

            public boolean isMustSyncStaff() {
                return mustSyncStaff;
            }

            public void setMustSyncStaff(boolean mustSyncStaff) {
                this.mustSyncStaff = mustSyncStaff;
            }

            public boolean isMustSyncAppointmentTypes() {
                return mustSyncAppointmentTypes;
            }

            public void setMustSyncAppointmentTypes(boolean mustSyncAppointmentTypes) {
                this.mustSyncAppointmentTypes = mustSyncAppointmentTypes;
            }

            public boolean isMustSyncAppointmentResources() {
                return mustSyncAppointmentResources;
            }

            public void setMustSyncAppointmentResources(boolean mustSyncAppointmentResources) {
                this.mustSyncAppointmentResources = mustSyncAppointmentResources;
            }
        }

        class FeedPrivateMessagePolicy {
            boolean mustFallback;
            boolean mustRemindPatient;
            boolean failOnFallback;
            String  fallbackDelay;
            String  reminderDelay;

            public boolean isMustFallback() {
                return mustFallback;
            }

            public void setMustFallback(boolean mustFallback) {
                this.mustFallback = mustFallback;
            }

            public boolean isMustRemindPatient() {
                return mustRemindPatient;
            }

            public void setMustRemindPatient(boolean mustRemindPatient) {
                this.mustRemindPatient = mustRemindPatient;
            }

            public boolean isFailOnFallback() {
                return failOnFallback;
            }

            public void setFailOnFallback(boolean failOnFallback) {
                this.failOnFallback = failOnFallback;
            }

            public String getFallbackDelay() {
                return fallbackDelay;
            }

            public void setFallbackDelay(String fallbackDelay) {
                this.fallbackDelay = fallbackDelay;
            }

            public String getReminderDelay() {
                return reminderDelay;
            }

            public void setReminderDelay(String reminderDelay) {
                this.reminderDelay = reminderDelay;
            }
        }


        int                      majorityAge;
        int                      minimumPatientAge;
        IBVersion                version;
        FeedSyncPolicy           feedSyncPolicy;
        FeedPrivateMessagePolicy privateMessagePolicy;

    }

    @SuppressWarnings("InnerClassMayBeStatic")
    class Settings {

        class PrivateMessageSettings {
            @Nullable
            String   fallbackEmail;
            @Nullable
            String   fallbackDelay;
            @Nullable
            String   remindPatientsAfter;
            @Nullable
            String[] fallbackUsers;


            @Nullable
            public String getFallbackEmail() {
                return fallbackEmail;
            }

            public void setFallbackEmail(@Nullable String fallbackEmail) {
                this.fallbackEmail = fallbackEmail;
            }

            @Nullable
            public String getFallbackDelay() {
                return fallbackDelay;
            }

            public void setFallbackDelay(@Nullable String fallbackDelay) {
                this.fallbackDelay = fallbackDelay;
            }

            @Nullable
            public String getRemindPatientsAfter() {
                return remindPatientsAfter;
            }

            public void setRemindPatientsAfter(@Nullable String remindPatientsAfter) {
                this.remindPatientsAfter = remindPatientsAfter;
            }

            @Nullable
            public String[] getFallbackUsers() {
                return fallbackUsers;
            }

            public void setFallbackUsers(@Nullable String[] fallbackUsers) {
                this.fallbackUsers = fallbackUsers;
            }
        }

        IBVersion policyVersion;
        Date      lastUpdated;
        @Nullable
        Date staffSyncedOn;
        @Nullable
        Date appointmentTypesSyncedOn;
        Date                   appointmentResourcesSyncedOn;
        int                    minimumPatientAge;
        int                    majorityAge;
        PrivateMessageSettings privateMessageSettings;

        public IBVersion getPolicyVersion() {
            return policyVersion;
        }

        public void setPolicyVersion(IBVersion policyVersion) {
            this.policyVersion = policyVersion;
        }

        public Date getLastUpdated() {
            return lastUpdated;
        }

        public void setLastUpdated(Date lastUpdated) {
            this.lastUpdated = lastUpdated;
        }

        @Nullable
        public Date getStaffSyncedOn() {
            return staffSyncedOn;
        }

        public void setStaffSyncedOn(@Nullable Date staffSyncedOn) {
            this.staffSyncedOn = staffSyncedOn;
        }

        @Nullable
        public Date getAppointmentTypesSyncedOn() {
            return appointmentTypesSyncedOn;
        }

        public void setAppointmentTypesSyncedOn(@Nullable Date appointmentTypesSyncedOn) {
            this.appointmentTypesSyncedOn = appointmentTypesSyncedOn;
        }

        public Date getAppointmentResourcesSyncedOn() {
            return appointmentResourcesSyncedOn;
        }

        public void setAppointmentResourcesSyncedOn(Date appointmentResourcesSyncedOn) {
            this.appointmentResourcesSyncedOn = appointmentResourcesSyncedOn;
        }

        public int getMinimumPatientAge() {
            return minimumPatientAge;
        }

        public void setMinimumPatientAge(int minimumPatientAge) {
            this.minimumPatientAge = minimumPatientAge;
        }

        public int getMajorityAge() {
            return majorityAge;
        }

        public void setMajorityAge(int majorityAge) {
            this.majorityAge = majorityAge;
        }

        public PrivateMessageSettings getPrivateMessageSettings() {
            return privateMessageSettings;
        }

        public void setPrivateMessageSettings(PrivateMessageSettings privateMessageSettings) {
            this.privateMessageSettings = privateMessageSettings;
        }
    }

    @SuppressWarnings("InnerClassMayBeStatic")
    class FDStaffMemberDisposition {

        class FDStaffPractice {
            String jurisdiction;
            String number;
        }

        IBVersion version;
        boolean   isDisposed;
        Date      disposedOn;
        String    dispensaryRole;
        @Nullable
        String            userGuid;
        @Nullable
        String            practitionerGuid;
        @Nullable
        FDStaffPractice[] practices;

        public FDStaffMemberDisposition() {
        }

        public IBVersion getVersion() {
            return version;
        }

        public void setVersion(IBVersion version) {
            this.version = version;
        }

        public boolean isDisposed() {
            return isDisposed;
        }

        public void setDisposed(boolean disposed) {
            isDisposed = disposed;
        }

        public Date getDisposedOn() {
            return disposedOn;
        }

        public void setDisposedOn(Date disposedOn) {
            this.disposedOn = disposedOn;
        }

        public String getDispensaryRole() {
            return dispensaryRole;
        }

        public void setDispensaryRole(String dispensaryRole) {
            this.dispensaryRole = dispensaryRole;
        }

        @Nullable
        public String getUserGuid() {
            return userGuid;
        }

        public void setUserGuid(@Nullable String userGuid) {
            this.userGuid = userGuid;
        }

        @Nullable
        public String getPractitionerGuid() {
            return practitionerGuid;
        }

        public void setPractitionerGuid(@Nullable String practitionerGuid) {
            this.practitionerGuid = practitionerGuid;
        }

        @Nullable
        public FDStaffPractice[] getPractices() {
            return practices;
        }

        public void setPractices(@Nullable FDStaffPractice[] practices) {
            this.practices = practices;
        }
    }

    class FDStaffMember {

        private String                   name;
        @Nullable
        private String                   middleName;
        private String                   firstName;
        private String                   staffEid;
        private String                   dispensaryGuid;
        private String                   feedGuid;
        private String                   dispensaryEid;
        private FDStaffMemberDisposition disposition;

        public FDStaffMember() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Nullable
        public String getMiddleName() {
            return middleName;
        }

        public void setMiddleName(@Nullable String middleName) {
            this.middleName = middleName;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getStaffEid() {
            return staffEid;
        }

        public void setStaffEid(String staffEid) {
            this.staffEid = staffEid;
        }

        public String getDispensaryGuid() {
            return dispensaryGuid;
        }

        public void setDispensaryGuid(String dispensaryGuid) {
            this.dispensaryGuid = dispensaryGuid;
        }

        public String getFeedGuid() {
            return feedGuid;
        }

        public void setFeedGuid(String feedGuid) {
            this.feedGuid = feedGuid;
        }

        public String getDispensaryEid() {
            return dispensaryEid;
        }

        public void setDispensaryEid(String dispensaryEid) {
            this.dispensaryEid = dispensaryEid;
        }

        public FDStaffMemberDisposition getDisposition() {
            return disposition;
        }

        public void setDisposition(FDStaffMemberDisposition disposition) {
            this.disposition = disposition;
        }
    }


    SettingsPolicy                 settingsPolicy;
    Settings                       settings;
    String                         feedAlias;
    String                         feedGuid;
    String                         dispensaryGuid;
    String                         humanName;
    Boolean                        active;
    HashMap<String, FDStaffMember> staffMembers;
    @Nullable
    IBFeedStatus status;

    public IBFeedDetail() {
        onNew();
    }

    public SettingsPolicy getSettingsPolicy() {
        return settingsPolicy;
    }

    public void setSettingsPolicy(SettingsPolicy settingsPolicy) {
        this.settingsPolicy = settingsPolicy;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public String getFeedAlias() {
        return feedAlias;
    }

    public void setFeedAlias(String feedAlias) {
        this.feedAlias = feedAlias;
    }

    public String getFeedGuid() {
        return feedGuid;
    }

    public void setFeedGuid(String feedGuid) {
        this.feedGuid = feedGuid;
    }

    public String getDispensaryGuid() {
        return dispensaryGuid;
    }

    public void setDispensaryGuid(String dispensaryGuid) {
        this.dispensaryGuid = dispensaryGuid;
    }

    public String getHumanName() {
        return humanName;
    }

    public void setHumanName(String humanName) {
        this.humanName = humanName;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public HashMap<String, FDStaffMember> getStaffMembers() {
        return staffMembers;
    }

    public void setStaffMembers(HashMap<String, FDStaffMember> staffMembers) {
        this.staffMembers = staffMembers;
    }

    @Nullable
    public IBFeedStatus getStatus() {
        return status;
    }

    public void setStatus(@Nullable IBFeedStatus status) {
        this.status = status;
    }


    //region Countable

    private final static String  CLASSTAG       = kModulePrefix + "." + IBFeedDetail.class.getSimpleName();
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
