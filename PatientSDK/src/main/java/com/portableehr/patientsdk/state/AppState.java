package com.portableehr.patientsdk.state;

import static com.portableehr.patientsdk.utils.PatientRuntimeConstants.kDefaultClassCountable;
import static com.portableehr.patientsdk.utils.PatientRuntimeConstants.kSecureStoreName;
import static com.portableehr.sdk.EHRLibRuntime.kEpochStart;
import static com.portableehr.sdk.EHRLibRuntime.kModulePrefix;
import static com.portableehr.sdk.network.gson.GsonFactory.standardBuilder;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.portableehr.patientsdk.models.SecureCredentials;
import com.portableehr.sdk.EHRLibRuntime;
import com.portableehr.sdk.PehrSDKConfiguration;
import com.portableehr.sdk.models.ModelRefreshPolicyEnum;
import com.portableehr.sdk.models.UserModel;
import com.portableehr.sdk.models.notification.NotificationModel;
import com.portableehr.sdk.models.service.ServiceModel;
import com.portableehr.sdk.network.NAO.inbound.IBAppInfo;
import com.portableehr.sdk.network.NAO.inbound.IBConsent;
import com.portableehr.sdk.network.NAO.inbound.IBDeviceInfo;
import com.portableehr.sdk.network.NAO.inbound.IBPatient;
import com.portableehr.sdk.network.NAO.inbound.IBUser;
import com.portableehr.sdk.network.NAO.inbound.conversations.EntryAttachment;
import com.portableehr.sdk.network.ehrApi.EHRApiServer;
import com.portableehr.sdk.network.gson.GSONexcludeInbound;
import com.portableehr.sdk.network.gson.GSONexcludeOutbound;
import com.portableehr.sdk.network.gson.GsonFactory;
import com.portableehr.sdk.util.FileUtils;
import com.q42.qlassified.Qlassified;
import com.q42.qlassified.Storage.QlassifiedSharedPreferencesService;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AppState {


    private static final String fileName = "appState.json";
    private static AppState _instance = null;
    public static int activityStackSize;


    @GSONexcludeOutbound
    Date mStartTime;

    @GSONexcludeOutbound
    @GSONexcludeInbound
    private Qlassified keyStore;

    static {
        // we will always inherit our activities from AppCompatActivityExt , which manages the
        // onCreate and onDestroy activity stack.  This will permit us to figure out when the patient
        // app context has been taken away, and thus if the app is compromised from a security
        // perspective.

        activityStackSize = 0;

    }

    public static boolean create(Context context, boolean deepReset) {

        // in the beginning, there was a keystore !

        if (null != _instance && deepReset) {
            Log.e(CLASSTAG, "create() : called but we already have an instance. Trying to release prior instance");
            _instance = null;
        }

        _instance = new AppState();


        Qlassified keyStore = Qlassified.Service;
        keyStore.setStorageService(new QlassifiedSharedPreferencesService(context, kSecureStoreName));
        keyStore.start(context);
        _instance.keyStore = keyStore;
        SecureCredentials.setKeyStore(keyStore);


        boolean newInstall = !_instance.loadFromDevice();

        boolean success;

        String desiredStackKey = PehrSDKConfiguration.getInstance().getAppStackKey();
        if (!newInstall) {
            boolean keepStack;
            keepStack = _instance.getStackKey().contentEquals(desiredStackKey);
            if (keepStack) {
                if (deepReset) {
                    success = _instance.initOnDevice(context) && _instance.save();
                } else {
                    success = true;
                }
            } else {
                // switching stack here !!!
                success = _instance.initOnDevice(context);
                if (success) {
                    _instance.setStackKey(desiredStackKey);
                    _instance.save();
                }
            }

            if (_instance.deviceInfo != null
                    && _instance.getSecureCredentials() != null
                    && _instance.getSecureCredentials().getUserCredentials() != null) {
                String guid = _instance.getSecureCredentials().getUserCredentials().getDeviceGuid();
                _instance.deviceInfo.setDeviceGuid(guid);
                if (EHRLibRuntime.getInstance().getDeviceInfo() != null) {
                    EHRLibRuntime.getInstance().getDeviceInfo().setDeviceGuid(guid);
                }
            }
        } else {
            success = _instance.initOnDevice(context) && _instance.save();
        }


        String ip = _instance.getDeviceIpAddress();
        Log.d(CLASSTAG, "IP addy : " + ip);
        return success;
    }

    public static AppState getInstance() {

        return _instance;
    }

    private Date timeOfLastSync;
    private String language;
    private IBDeviceInfo deviceInfo;
    private IBPatient patient;
    private String deviceLanguage;
    private EHRApiServer server;
    private boolean enforcePrivacy;
    private boolean userHasSeenEULAwarning;
    private boolean userHasSeenVaultWarning;
    private boolean userHasReadEULA;
    private IBAppInfo appInfo;
    private List<IBConsent> consents;
    private IBConsent currentConsent;
    private List<EntryAttachment> entryAttachments;
    private int currentArrachmentIndex = 0;


    @GSONexcludeOutbound
    @GSONexcludeInbound
    private SecureCredentials secureCredentials;


    @Nullable
    String stackKey;

    @GSONexcludeOutbound
    @GSONexcludeInbound
    private EHRApiServer OAMPserver;

    @GSONexcludeOutbound
    @GSONexcludeInbound
    public NotificationModel notificationModel;

    @GSONexcludeOutbound
    @GSONexcludeInbound
    public ServiceModel serviceModel;

    @GSONexcludeOutbound
    @GSONexcludeInbound
    public UserModel userModel;

    public void reset() {
        Context context = EHRLibRuntime.getInstance().getContext();

        AppState as = getInstance();

        as.timeOfLastSync = kEpochStart;
        as.deviceInfo = IBDeviceInfo.initFromDevice(context);
        as.patient = null;
        as.enforcePrivacy = false;
//        app.setPrivacyCompromised(true);
        if (as.userModel != null) {
            as.userModel.setPollingPolicy(ModelRefreshPolicyEnum.NONE);
        }
        UserModel.resetOnDevice();
        as.userModel = UserModel.getInstance();
        as.userModel.setPollingPolicy(ModelRefreshPolicyEnum.NONE);

        as.getNotificationModel().setPollingPolicy(ModelRefreshPolicyEnum.NONE);
        NotificationModel.resetOnDevice();

        as.serviceModel.setPollingPolicy(ModelRefreshPolicyEnum.NONE);
        ServiceModel.resetOnDevice();
        as.serviceModel = ServiceModel.getInstance();

        as.secureCredentials.reset();

        as.save();
    }

    private AppState() {
        mStartTime = new Date();
        setClassCountable(false);
        this.onNew();
    }


    //region Getters/setters


    public int maximumNumberOfDevices() {
        return 3;
    }

    public Date getStartTime() {
        return mStartTime;
    }

    public IBUser getUser() {
        return this.userModel.getUser();
    }

    public SecureCredentials getSecureCredentials() {
        return secureCredentials;
    }

    public boolean isEulaConsented() {
        if (null == appInfo || null == appInfo.getEula()) {
            return false;
        }
        if (null == secureCredentials || null == secureCredentials.getUserCredentials()) {
            return false;
        }
        if (!secureCredentials.getUserCredentials().hasConsentedEula()) {
            return false;
        }
        if (!secureCredentials.getUserCredentials().getAppEula().eulaVersion.toString().equals(appInfo.getEula().version.toString())) {
            return false;
        }
        return true;
    }

    public boolean isResearchConsentDismissed() {
        if (null == secureCredentials || null == secureCredentials.getUserCredentials()) {
            return false;
        }
        return secureCredentials.getUserCredentials().getResearchConsentDismissedKey();
    }

    public Date getTimeOfLastSync() {
        return timeOfLastSync;
    }

    public void setTimeOfLastSync(Date timeOfLastSync) {
        this.timeOfLastSync = timeOfLastSync;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public IBPatient getPatient() {
        return patient;
    }

    public void setPatient(IBPatient patient) {
        this.patient = patient;
    }

    public IBAppInfo getAppInfo() {
        return appInfo;
    }

    public void setAppInfo(IBAppInfo appInfo) {
        this.appInfo = appInfo;
        if (null == secureCredentials.getUserCredentials().getEulaGuid()) {
            resetCredentials(appInfo);
        } else if (null == secureCredentials.getUserCredentials().getEulaVersion()) {
            resetCredentials(appInfo);
        } else if (!appInfo.getEula().getVersion().toString().contentEquals(secureCredentials.getUserCredentials().getEulaVersion().toString())) {
            resetCredentials(appInfo);
        }
    }

    public List<IBConsent> getConsents() {
        return consents;
    }

    public void setConsents(List<IBConsent> consents) {
        this.consents = consents;
    }

    public IBConsent getCurrentConsent() {
        return currentConsent;
    }

    public void setCurrentConsent(IBConsent consent) {
        this.currentConsent = consent;
    }

    private void resetCredentials(IBAppInfo appInfo) {
        secureCredentials.getUserCredentials().setEulaVersion(appInfo.getEula().version);
        secureCredentials.getUserCredentials().getAppEula().eulaVersion = appInfo.getEula().version;
        secureCredentials.getUserCredentials().setEulaDateConsented(null);
        secureCredentials.getUserCredentials().setEulaDateSeen(null);
        secureCredentials.getUserCredentials().setEulaGuid(appInfo.getEula().guid);
    }

    public String getDeviceLanguage() {
        return deviceLanguage;
    }

    public void setDeviceLanguage(String deviceLanguage) {
        this.deviceLanguage = deviceLanguage;
    }

    public boolean isEnforcePrivacy() {
        return enforcePrivacy;
    }

    public boolean isUserHasSeenEULAwarning() {
        return userHasSeenEULAwarning;
    }

    public void setUserHasSeenEULAwarning(boolean has) {
        this.userHasSeenEULAwarning = has;
    }

    public boolean hasConsentedEula() {
        return secureCredentials.getUserCredentials().hasConsentedEula();
    }

    public void setEnforcePrivacy(boolean enforcePrivacy) {
        this.enforcePrivacy = enforcePrivacy;
    }


    public boolean isUserHasSeenVaultWarning() {
        return userHasSeenVaultWarning;
    }

    public void setUserHasSeenVaultWarning(boolean userHasSeenVaultWarning) {
        this.userHasSeenVaultWarning = userHasSeenVaultWarning;
    }

    public boolean isUserHasReadEULA() {
        return userHasReadEULA;
    }

    public void setUserHasReadEULA(boolean userHasReadEULA) {
        this.userHasReadEULA = userHasReadEULA;
    }

    public IBDeviceInfo getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(IBDeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public void setServer(EHRApiServer server) {
        this.server = server;
    }

    public void setOAMPserver(EHRApiServer server) {
        this.OAMPserver = server;
    }

    public EHRApiServer getServer() {
        return this.server;
    }

    public EHRApiServer getOAMPserver() {
        return this.OAMPserver;
    }

    public List<EntryAttachment> getEntryAttachments() {
        return entryAttachments;
    }

    public void setEntryAttachments(List<EntryAttachment> entryAttachments, int currentArrachmentIndex) {
        this.entryAttachments = entryAttachments;
        this.currentArrachmentIndex = currentArrachmentIndex;
    }

    public int getCurrentArrachmentIndex() {
        return currentArrachmentIndex;
    }

    @Nullable
    public String getStackKey() {
        return stackKey;
    }

    public void setStackKey(@Nullable String stackKey) {
        this.stackKey = stackKey;
        this.server = EHRLibRuntime.getServerForStackKey(stackKey);
        this.OAMPserver = EHRLibRuntime.getOAMPserver(stackKey);
    }

    //endregion

    public boolean loadFromDevice() {

        boolean success = false;
        String json = FileUtils.readAppStateJson();
        if (json != null) {
            GsonBuilder b = standardBuilder();
            Gson gson = b.create();
            AppState old = gson.fromJson(json, AppState.class);

            if (old.getStackKey() == null) {
                // this should take care of devices that were activated
                // prior to having a stack key in the model !
                if (PehrSDKConfiguration.getInstance() != null
                        && !TextUtils.isEmpty(PehrSDKConfiguration.getInstance().getAppStackKey())) {
                    this.setStackKey(PehrSDKConfiguration.getInstance().getAppStackKey());
                } else {
                    this.setStackKey("CA.prod");
                }
            } else {
                // server and OAMPserver are not persisted
                this.setStackKey(old.stackKey);
            }

            // get the stuff that is persisted

            this.appInfo = old.appInfo;
            this.deviceInfo = old.deviceInfo;
//            this.deviceLanguage = old.deviceLanguage;
            // Rahul: This should always pickup language from device
            String dl = Locale.getDefault().getISO3Language();
            if (dl.equals("eng")) {
                dl = "en";
            } else if (dl.startsWith("fr")) {
                dl = "fr";
            } else {
                dl = old.deviceLanguage;
            }
            this.deviceLanguage = dl;
            this.enforcePrivacy = old.enforcePrivacy;
            this.notificationModel = NotificationModel.getInstance();
            this.serviceModel = ServiceModel.getInstance();
            this.timeOfLastSync = kEpochStart;
            Log.d(getLogTAG(), "Loaded appState from device.");
            success = loadSecureCreadentials();
            if (!success) {
                Log.e(getLogTAG(), "Reloaded appState, but there were no credentials, resetting.");
                this.reset();
                this.secureCredentials.reset();
            }
            this.userModel = UserModel.getInstance();
        }
        return success;
    }

    private boolean deleteFromDevice(Context context) {

        String filePath = getFQN(context);
        File file = new File(filePath);
        return !file.exists() || file.delete();
    }

    public static boolean saveOnDevice() {
        return getInstance().getUserModel().save() && getInstance().getNotificationModel().save() && getInstance().save();
    }

//    public static boolean saveInSecureCredentials() {
//        return  getInstance().getUserModel()
//    }

    public boolean save() {
        Context context = EHRLibRuntime.getInstance().getContext();
        AppState as = this;
        boolean ret = false;
        File fd = context.getFilesDir();
        if (null == fd) {
            Log.e(getLogTAG(), "Unable to get files dir from ApplicationExt");
        } else {
            if (fd.isDirectory()) {

                try {
                    String json = this.asJson();
//                    Log.d(getLogTAG(), "AppState as json \n" + json);
                    Writer out = new BufferedWriter(new OutputStreamWriter(
                            new FileOutputStream(getFQN(context)), StandardCharsets.UTF_8));
                    out.write(json);
                    out.close();
                    ret = true;
                } catch (Exception e) {
                    Log.e(getLogTAG(), "Caught exception while writing[" + getFQN(context) + "]\n", e);
                }

            } else {
                Log.e(getLogTAG(), "getFilesDIr returned a file !!!");
            }
        }
        return ret;
    }

    public boolean saveSecureCredentials() {

        boolean ret = secureCredentials.saveInKeyStore();
        if (!ret) {
            Log.e(getLogTAG(), "An error occured when persisting secure credentials.");
        }
        return ret;
    }

    public boolean loadSecureCreadentials() {
        this.secureCredentials = new SecureCredentials();
        return secureCredentials.loadFromKeyStore();
    }

    private boolean initOnDevice(Context context) {

        boolean success;
        if (success = deleteFromDevice(context)) {

            setDeviceInfo(IBDeviceInfo.initFromDevice(context));

            if (secureCredentials == null) {
                secureCredentials = new SecureCredentials();
            } else {
                secureCredentials.reset();
            }
            secureCredentials.saveInKeyStore();

            String dl = Locale.getDefault().getISO3Language();
            if (dl.equals("eng")) {
                dl = "en";
            } else if (dl.startsWith("fr")) {
                dl = "fr";
            } else {
                dl = "en";
            }
            setDeviceLanguage(dl);
            UserModel.deleteFromDevice();
            NotificationModel.resetOnDevice();
            serviceModel = ServiceModel.getInstance();
            ServiceModel.resetOnDevice();
            userModel = UserModel.getInstance();
            notificationModel = NotificationModel.getInstance();
            setTimeOfLastSync(kEpochStart);

            server = EHRLibRuntime.getCurrentServer();
            OAMPserver = EHRLibRuntime.getOAMPserver();
            success = saveOnDevice();
        }

        return success;

    }

    private static String getFQN(Context context) {
        String filePath = null;
        File fd = context.getFilesDir();
        if (null == fd) {
            Log.e(CLASSTAG, "Unable to get files dir from ApplicationExt");
        } else {
            if (fd.isDirectory()) {
                filePath = fd.getAbsolutePath() + "/" + fileName;
            } else {
                Log.e(CLASSTAG, "getFilesDIr returned a file !!!");
            }
        }
        return filePath;
    }

    // expose app State

    //region Models

    public NotificationModel getNotificationModel() {
        return NotificationModel.getInstance();
    }

    public ServiceModel getServiceModel() {
        if (null == serviceModel) {
            serviceModel = ServiceModel.getInstance();
        }
        return serviceModel;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    //endregion


    //region device info

    @NonNull
    public String getDeviceIpAddress() {
        return "*** REDACTED ***";
//        String              actualConnectedToNetwork = null;
//        ConnectivityManager connManager              = (ConnectivityManager) ApplicationExt.getContext().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
//        if (connManager != null) {
//            NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//            if (mWifi.isConnected()) {
//                actualConnectedToNetwork = getWifiIp();
//            }
//        }
//        if (TextUtils.isEmpty(actualConnectedToNetwork)) {
//            actualConnectedToNetwork = getNetworkInterfaceIpAddress();
//        }
//        if (TextUtils.isEmpty(actualConnectedToNetwork)) {
//            actualConnectedToNetwork = "127.0.0.1";
//        }
//        return actualConnectedToNetwork;
    }

    @Nullable
    public String getWifiIp() {
        return "*** REDACTED ***";

//        final WifiManager mWifiManager = (WifiManager) ApplicationExt.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//        if (mWifiManager != null && mWifiManager.isWifiEnabled()) {
//            int ip = mWifiManager.getConnectionInfo().getIpAddress();
//            return (ip & 0xFF) + "." + ((ip >> 8) & 0xFF) + "." + ((ip >> 16) & 0xFF) + "."
//                    + ((ip >> 24) & 0xFF);
//        }
//        return null;
    }


    @Nullable
    public String getNetworkInterfaceIpAddress() {

        return "*** REDACTED ***";

//        try {
//            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
//                NetworkInterface networkInterface = en.nextElement();
//                for (Enumeration<InetAddress> enumIpAddr = networkInterface.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
//                    InetAddress inetAddress = enumIpAddr.nextElement();
//                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
//                        String host = inetAddress.getHostAddress();
//                        if (!TextUtils.isEmpty(host)) {
//                            return host;
//                        }
//                    }
//                }
//
//            }
//        } catch (Exception ex) {
//            Log.e("IP Address", "getLocalIpAddress", ex);
//        }
//        return null;
    }
    //endregion


    //region Countable


    //region Countable

    private final static String CLASSTAG = kModulePrefix + "." + AppState.class.getSimpleName();
    @GSONexcludeOutbound
    private String TAG;
    private static int lifeTimeInstances;
    private static int numberOfInstances;
    @GSONexcludeOutbound
    private int instanceNumber;
    @GSONexcludeOutbound
    private static boolean classCountable = kDefaultClassCountable;

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


    public String asJson() {
        GsonBuilder builder = GsonFactory.standardBuilder();
        Gson jsonSerializer = builder.create();
        String theJson = jsonSerializer.toJson(this, this.getClass());
        return theJson;
    }

    public static AppState fromJson(String json) {
        GsonBuilder builder = GsonFactory.standardBuilder();
        Gson jsonDeserializer = builder.create();
        AppState theObject = jsonDeserializer.fromJson(json, AppState.class);
        return theObject;
    }

    //endregion


}

