package com.portableehr.sdk.secure;

import androidx.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.portableehr.sdk.EHRLibRuntime;
import com.portableehr.sdk.network.gson.GSONexcludeOutbound;
import com.q42.qlassified.Qlassified;

import java.io.Serializable;
import java.util.Date;

import static com.portableehr.sdk.network.gson.GsonFactory.standardBuilder;

/**
 * Created by : yvesleborg
 * Date       : 2020-07-21
 * <p>
 * Copyright Portable Ehr Inc, 2020
 */
public class ClinicSecureCredentials implements Serializable {
    {
        setClassCountable(false);
    }

    private static Qlassified keyStore;

    public static void setKeyStore(Qlassified keystore) {
        ClinicSecureCredentials.keyStore = keystore;
    }

    public class UserCredentials {

        private static final String kDispensaryGuidKey = "kDispensaryGuidKey";
        private static final String kAdminGuidKey      = "kAdminGuidKey";
        private static final String kAdminApiKeyKey    = "kAdminApiKeyKey";
        private static final String kDeviceGuidKey     = "kDeviceGuidKey";
        private static final String kNullString        = "kNullString";
        private static final long   kNullDate          = 0;

        private String dispensaryGuid;
        private String deviceGuid;
        private String adminGuid;
        private String adminApiKey;


        /**
         * Constructor for default : guest, has not read eula.
         */

        public UserCredentials() {

        }

        @SuppressWarnings("unused")
        public String getAdminGuid() {
            return this.adminGuid;
        }

        public void setAdminGuid(String theGuid) {
            this.adminGuid = theGuid;
            saveString(kAdminGuidKey, theGuid);
        }

        @SuppressWarnings("unused")
        public String getAdminApiKey() {
            return adminApiKey;
        }

        public void setAdminApiKey(String adminApiKey) {
            this.adminApiKey = adminApiKey;
            saveString(kAdminApiKeyKey, adminApiKey);
        }

        public String getDispensaryGuid() {
            return dispensaryGuid;
        }

        public void setDispensaryGuid(String theGuid) {
            this.dispensaryGuid = theGuid;
            saveString(kDispensaryGuidKey, theGuid);
        }


        public String getDeviceGuid() {
            return deviceGuid;
        }

        public void setDeviceGuid(String deviceGuid) {
            this.deviceGuid = deviceGuid;
            saveString(kDeviceGuidKey, deviceGuid);
        }

        public boolean loadFromKeyStore() {

            try {

                this.deviceGuid = loadString(kDeviceGuidKey);
                this.dispensaryGuid = loadString(kDispensaryGuidKey);
                this.adminGuid = loadString(kAdminGuidKey);
                this.adminApiKey = loadString(kAdminApiKeyKey);

            } catch (Exception e) {
                Log.wtf(getLogTAG(), "loadFromKeyStore:  An error occured when saving secure credentials", e);
                return false;
            }
            return true;
        }

        public boolean saveInKeyStore() {
            try {

                saveString(kDeviceGuidKey, this.deviceGuid);
                saveString(kDispensaryGuidKey, this.dispensaryGuid);
                saveString(kAdminGuidKey, this.adminGuid);
                saveString(kAdminApiKeyKey, this.adminApiKey);
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
            String result   = null;
            if (null != strawman && !strawman.contentEquals(kNullString)) {
                result = strawman;
            }
            return result;
        }

        @SuppressWarnings("unused")
        private void saveDate(String key, @Nullable Date dateToSave) {
            if (dateToSave != null) {
                keyStore.put(key, dateToSave.getTime());
            } else {
                keyStore.put(key, kNullDate);
            }
        }

        @SuppressWarnings("unused")
        private Date loadDate(String key) {
            Long strawman = keyStore.getLong(key);
            Date result   = null;
            if (strawman != kNullDate) {
                result = new Date(strawman);
            }
            return result;
        }
        //endregion

    }

    public ClinicSecureCredentials() {
        setClassCountable(false);
        onNew();
        this.userCredentials = new UserCredentials();
    }


    private UserCredentials userCredentials;

    public UserCredentials getUserCredentials() {
        return userCredentials;
    }

    @SuppressWarnings("unused")
    public void reset() {
        this.userCredentials = new UserCredentials();
        this.getUserCredentials().setAdminGuid(null);
        this.getUserCredentials().setAdminApiKey(null);
        this.getUserCredentials().setDispensaryGuid(null);
        this.saveInKeyStore();
    }

    @SuppressWarnings("UnusedReturnValue")
    public boolean saveInKeyStore() {
        return this.getUserCredentials().saveInKeyStore();
    }

    public boolean loadFromKeyStore() {
        return this.getUserCredentials().loadFromKeyStore();
    }


    //region Countable

    private final static String  CLASSTAG       = EHRLibRuntime.kModulePrefix + "." + ClinicSecureCredentials.class.getSimpleName();
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

    //region GSON helpers


    @SuppressWarnings("unused")
    public String asJson() {
        GsonBuilder builder        = standardBuilder();
        Gson        jsonSerializer = builder.create();
        String      theJson        = jsonSerializer.toJson(this, this.getClass());
        return theJson;
    }

    @SuppressWarnings("unused")
    public static ClinicSecureCredentials fromJson(String json) {
        GsonBuilder             builder          = standardBuilder();
        Gson                    jsonDeserializer = builder.create();
        ClinicSecureCredentials theObject        = jsonDeserializer.fromJson(json, ClinicSecureCredentials.class);
        return theObject;
    }

    //endregion

}
