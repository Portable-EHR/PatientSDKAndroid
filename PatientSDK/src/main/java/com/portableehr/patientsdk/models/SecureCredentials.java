package com.portableehr.patientsdk.models;

import static com.portableehr.patientsdk.utils.PatientRuntimeConstants.kDefaultClassCountable;
import static com.portableehr.sdk.EHRLibRuntime.kGuestApiKey;
import static com.portableehr.sdk.EHRLibRuntime.kGuestUserGuid;
import static com.portableehr.sdk.EHRLibRuntime.kModulePrefix;
import static com.portableehr.sdk.network.gson.GsonFactory.standardBuilder;

import android.util.Log;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.portableehr.sdk.network.NAO.inbound.IBUserEula;
import com.portableehr.sdk.network.NAO.inbound.IBVersion;
import com.portableehr.sdk.network.gson.GSONexcludeOutbound;
import com.q42.qlassified.Qlassified;

import java.io.Serializable;
import java.util.Date;

public class SecureCredentials implements Serializable {

    private static Qlassified keyStore;

    public static void setKeyStore(Qlassified keystore) {
        SecureCredentials.keyStore = keystore;
    }

    public class UserCredentials {

        private static final String kDeviceGuidKey = "kDeviceGuidKey";
        private static final String kUserGuidKey = "kUserGuidKey";
        private static final String kUserApiKeyKey = "kUserApiKeyKey";
        private static final String kEulaGuidKey = "kEulaGuidKey";
        private static final String kEulaVersionKey = "kEulaVersionKey";
        private static final String kEulaDateSeenKey = "kEulaDateSeenKey";
        private static final String kEulaDateConsentedKey = "kEulaDateConsentedKey";
        private static final String kNullString = "kNullString";
        private static final String kResearchConsentDismissedKey = "kResearchConsentDismissedKey";
        private static final String kIsUserPasswordSetKey = "kIsUserPasswordSetKey";
        private static final String kIsUserPhoneVerifiedKey = "kIsUserPhoneVerifiedKey";
        private static final String kIsUserEmailVerifiedKey = "kIsUserEmailVerifiedKey";
        private static final String kUserActivationTypeKey = "kUserActivationTypeKey";
        private static final long kNullDate = 0;

        private String deviceGuid;
        private String userGuid;
        private String userApiKey;
        private IBVersion eulaVersion;
        private Date eulaDateSeen;
        private Date eulaDateConsented;
        private String eulaGuid;
        private boolean researchConsentDismissedKey = false;
        private boolean isUserPasswordSet = false;
        private boolean isUserPhoneVerified = false;
        private boolean isUserEmailVerified = false;
        private String activationType;


        /**
         * Constructor for default : guest, has not read eula.
         */

        public UserCredentials() {
            this.userGuid = kGuestUserGuid;
            this.userApiKey = kGuestApiKey;
        }

        public String getDeviceGuid() {
            return deviceGuid;
        }

        public void setDeviceGuid(String deviceGuid) {
            this.deviceGuid = deviceGuid;
            saveString(kDeviceGuidKey, deviceGuid);
        }

        public String getUserGuid() {
            return userGuid;
        }

        public void setUserGuid(String userGuid) {
            this.userGuid = userGuid;
            saveString(kUserGuidKey, userGuid);
        }


        public IBVersion getEulaVersion() {
            return eulaVersion;
        }

        public void setEulaVersion(IBVersion eulaVersion) {
            this.eulaVersion = eulaVersion;
            saveString(kEulaVersionKey, eulaVersion.toString());
        }

        public Date getEulaDateSeen() {
            return eulaDateSeen;
        }

        public void setEulaDateSeen(Date eulaDateSeen) {
            this.eulaDateSeen = eulaDateSeen;
            saveDate(kEulaDateSeenKey, eulaDateSeen);
        }

        public Date getEulaDateConsented() {
            return eulaDateConsented;
        }

        public void setEulaDateConsented(Date eulaDateConsented) {
            this.eulaDateConsented = eulaDateConsented;
            saveDate(kEulaDateConsentedKey, eulaDateConsented);
        }

        public boolean getResearchConsentDismissedKey() {
            return researchConsentDismissedKey;
        }

        public void setResearchConsentDismissedKey(boolean value) {
            this.researchConsentDismissedKey = value;
            saveBoolean(kResearchConsentDismissedKey, researchConsentDismissedKey);
        }

        public String getEulaGuid() {
            return eulaGuid;
        }

        public void setEulaGuid(String eulaGuid) {
            this.eulaGuid = eulaGuid;
            saveString(kEulaGuidKey, eulaGuid);
        }

        public String getUserApiKey() {
            return userApiKey;
        }

        public void setUserApiKey(String userApiKey) {
            this.userApiKey = userApiKey;
            saveString(kUserApiKeyKey, userApiKey);
        }

        public String getUserActivationType() {
            return activationType;
        }

        public void setUserActivationType(String type) {
            this.activationType = type;
            saveString(kUserActivationTypeKey, activationType);
        }

        public IBUserEula getAppEula() {
            IBUserEula eula = new IBUserEula();
            eula.setDateConsented(this.eulaDateConsented);
            eula.setDateSeen(this.eulaDateSeen);
            eula.setEulaGuid(this.eulaGuid);
            eula.setEulaVersion(this.eulaVersion);
            eula.setScope("app");
            return eula;
        }

        public boolean hasConsentedEula() {

            if (null == eulaDateConsented) {
                return false;
            }
            if (eulaDateConsented.getTime() == kNullDate) {
                return false;
            }
            return true;
        }

        public boolean isUserPasswordSet() {
            return isUserPasswordSet;
        }

        public void setIsUserPasswordSet(boolean value) {
            this.isUserPasswordSet = value;
            saveBoolean(kIsUserPasswordSetKey, isUserPasswordSet);
        }

        public boolean isUserPhoneVerified() {
            return isUserPhoneVerified;
        }

        public void setIsUserPhoneVerified(boolean value) {
            this.isUserPhoneVerified = value;
            saveBoolean(kIsUserPhoneVerifiedKey, isUserPhoneVerified);
        }

        public boolean isUserEmailVerified() {
            return isUserEmailVerified;
        }

        public void setIsUserEmailVerified(boolean value) {
            this.isUserEmailVerified = value;
            saveBoolean(kIsUserEmailVerifiedKey, isUserEmailVerified);
        }

        public boolean loadFromKeyStore() {

            try {

                this.deviceGuid = loadString(kDeviceGuidKey);
                this.eulaGuid = loadString(kEulaGuidKey);
                String versionString = loadString(kEulaVersionKey);
                this.eulaVersion = null;
                if (null != versionString) {
                    this.eulaVersion = new IBVersion(versionString);
                }
                this.eulaDateSeen = loadDate(kEulaDateSeenKey);
                this.eulaDateConsented = loadDate(kEulaDateConsentedKey);
                this.userGuid = loadString(kUserGuidKey);
                this.userApiKey = loadString(kUserApiKeyKey);
                this.researchConsentDismissedKey = loadBoolean(kResearchConsentDismissedKey);
                this.isUserPasswordSet = loadBoolean(kIsUserPasswordSetKey);
                this.isUserEmailVerified = loadBoolean(kIsUserEmailVerifiedKey);
                this.isUserPhoneVerified = loadBoolean(kIsUserPhoneVerifiedKey);

            } catch (Exception e) {
                Log.wtf(getLogTAG(), "loadFromKeyStore:  An error occured when saving secure credentials", e);
                return false;
            }
            return true;
        }

        public boolean saveInKeyStore() {
            try {
                saveString(kEulaGuidKey, this.eulaGuid);
                if (this.eulaVersion != null) {
                    saveString(kEulaVersionKey, this.eulaVersion.toString());
                } else {
                    saveString(kEulaVersionKey, kNullString);
                }
                saveDate(kEulaDateConsentedKey, this.eulaDateConsented);
                saveDate(kEulaDateSeenKey, this.eulaDateSeen);
                saveString(kUserGuidKey, this.userGuid);
                saveString(kUserApiKeyKey, this.userApiKey);
                saveString(kDeviceGuidKey, this.deviceGuid);
                saveBoolean(kResearchConsentDismissedKey, this.researchConsentDismissedKey);
                saveBoolean(kIsUserEmailVerifiedKey, this.isUserEmailVerified);
                saveBoolean(kIsUserPhoneVerifiedKey, this.isUserPhoneVerified);
                saveBoolean(kIsUserPasswordSetKey, this.isUserPasswordSet);
            } catch (Exception e) {
                Log.d(getLogTAG(), "An error occured when saving secure credentials");
                e.printStackTrace();
                return false;
            }
            return true;
        }

        //region Kestore save/load by key
        private void saveString(String key, @Nullable String stringToSave) {
            if (stringToSave != null) {
                keyStore.put(key, stringToSave);
            } else {
                keyStore.put(key, kNullString);
            }
        }

        private String loadString(String key) {
            String strawman = keyStore.getString(key);
            String result = null;
            if (null != strawman && !strawman.contentEquals(kNullString)) {
                result = strawman;
            }
            return result;
        }

        //region Kestore save/load by key
        private void saveBoolean(String key, @Nullable Boolean valueToSave) {
            if (valueToSave != null) {
                keyStore.put(key, valueToSave);
            } else {
                keyStore.put(key, false);
            }
        }

        private Boolean loadBoolean(String key) {
            Boolean ret = keyStore.getBoolean(key);
            Boolean result = false;
            if (null != ret) {
                result = ret;
            }
            return result;
        }

        private void saveDate(String key, @Nullable Date dateToSave) {
            if (dateToSave != null) {
                keyStore.put(key, dateToSave.getTime());
            } else {
                keyStore.put(key, kNullDate);
            }
        }

        private Date loadDate(String key) {
            Long strawman = keyStore.getLong(key);
            Date result = null;
            if (strawman != kNullDate) {
                result = new Date(strawman);
            }
            return result;
        }
        //endregion

    }

    public SecureCredentials() {
        setClassCountable(false);
        onNew();
        this.userCredentials = new UserCredentials();
    }


    private UserCredentials userCredentials;

    public UserCredentials getUserCredentials() {
        return userCredentials;
    }

    public void reset() {
        this.userCredentials = new UserCredentials();
        this.getUserCredentials().setUserApiKey(kGuestApiKey);
        this.getUserCredentials().setUserGuid(kGuestUserGuid);
        this.saveInKeyStore();
    }

    public boolean saveInKeyStore() {
        return this.getUserCredentials().saveInKeyStore();
    }

    public boolean loadFromKeyStore() {
        return this.getUserCredentials().loadFromKeyStore();
    }


    //region Countable

    private final static String CLASSTAG = kModulePrefix + "." + SecureCredentials.class.getSimpleName();
    @GSONexcludeOutbound
    private String TAG;
    private static int lifeTimeInstances;
    private static int numberOfInstances;
    @GSONexcludeOutbound
    private int instanceNumber;
    @GSONexcludeOutbound
    private boolean classCountable = kDefaultClassCountable;

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        numberOfInstances--;
        if (numberOfInstances < 0) {
            Log.e(getLogTAG(), "*** You did not call onNew in your ctor(s)");
        }
        if (classCountable) {
            // Log.v(getLogTAG(), "finalize  ");
        }
    }

    protected void onNew() {
        TAG = CLASSTAG;
        numberOfInstances++;
        lifeTimeInstances++;
        instanceNumber = lifeTimeInstances;
        if (classCountable) {
            // Log.v(getLogTAG(), "onNew ");
        }
    }

    private String getLogLabel() {
        return Integer.toHexString(instanceNumber) + "/" + Integer.toHexString(numberOfInstances);
    }

    private void setClassCountable(@SuppressWarnings("SameParameterValue") boolean classCountable) {
        this.classCountable = classCountable;
    }

    protected String getLogTAG() {
        TAG = CLASSTAG + " [" + getLogLabel() + "] ";
        return TAG;
    }

    //endregion


    //region GSON helpers


    public String asJson() {
        GsonBuilder builder = standardBuilder();
        Gson jsonSerializer = builder.create();
        String theJson = jsonSerializer.toJson(this, this.getClass());
        return theJson;
    }

    public static SecureCredentials fromJson(String json) {
        GsonBuilder builder = standardBuilder();
        Gson jsonDeserializer = builder.create();
        SecureCredentials theObject = jsonDeserializer.fromJson(json, SecureCredentials.class);
        return theObject;
    }

    //endregion

}
