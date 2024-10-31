package com.portableehr.sdk.models;

import static com.portableehr.sdk.EHRLibRuntime.kModulePrefix;

import static java.lang.System.out;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.portableehr.patient.patientsdk.utils.EncryptionUtils;
import com.portableehr.patientsdk.state.AppState;
import com.portableehr.sdk.EHRLibRuntime;
import com.portableehr.sdk.network.NAO.inbound.IBDispensaryStaffUser;
import com.portableehr.sdk.network.NAO.inbound.IBUser;
import com.portableehr.sdk.network.gson.GSONexcludeOutbound;
import com.portableehr.sdk.network.gson.GsonFactory;
import com.portableehr.sdk.util.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;


/**
 * Created by : yvesleborg
 * Date       : 2017-03-31
 * <p>
 * Copyright Portable Ehr Inc, 2018
 */

@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class UserModel extends AbstractPollingModel {

    public static String NEW_PASSWORD = "change!";
    private static long mShortPollInterval = 15 * 60;          // 15 minutes
    private static long mLongPollInterval = 12 * 60 * 60;     // 12 hours

    //*********************************************************************************************/
    //** ctors and shit                                                                          **/
    //*********************************************************************************************/

    private UserModel() {
        this.setEULAread(false);
        this.setVaultWarningRead(false);
        setClassCountable(false);
        onNew();
    }

    private static UserModel _instance;

    public static UserModel getInstance() {
        if (null == UserModel._instance) {
            UserModel._instance = new UserModel();
            String guid = AppState.getInstance().getSecureCredentials().getUserCredentials().getUserGuid();
            if (guid != null && !guid.equals("")) {
                IBUser usr = null;
                UserModel _user = loadFromDevice(guid);
                if (_user == null) usr = IBUser.guest();
                else usr = _user.getUser();
                _instance.setUser(usr);
            } else {
                _instance.setUser(IBUser.guest());
            }
        }
        return _instance;
    }

    private UserModel(IBUser user) {
        this();
        this.user = user;
    }

    public void setUser(IBUser user) {
        _instance.user = user;
        if (user == IBUser.guest()) {
            cancelPoll();
            setPollingPolicy(ModelRefreshPolicyEnum.NONE);
        } else {
            cancelPoll();
            setPollingPolicy(ModelRefreshPolicyEnum.ADAPTATIVE);
        }
    }

    @SuppressWarnings("UnusedReturnValue")
    public static boolean deleteFromDevice() {
        String filePath = getInstance().getFQN();
        File file = new File(filePath);
        return file.delete();
    }

    @SuppressWarnings("UnusedReturnValue")
    public static boolean resetOnDevice() {
        getInstance().user = IBUser.guest();
        getInstance().isEULAread = false;
        getInstance().isVaultWarningRead = false;
        return getInstance().save();
    }

    public static UserModel loadFromDevice(String userGuid) {
        UserModel um = null;
        String folderFqn = FileUtils.pathOfUserDirectory(userGuid);
        if (folderFqn != null) {
            String jsonFqn = getFQN(userGuid);

            String json = FileUtils.readJsonFromFilePath(jsonFqn);


            String decryptedJsonData = null;

            try {
                decryptedJsonData = EncryptionUtils.decrypt(json);
                System.out.println("Decrypted Data: " + decryptedJsonData);
                if (decryptedJsonData != null){
                    um = UserModel.fromJson(decryptedJsonData);
                }else{
                    Log.e(CLASSTAG, "loadFromDevice : got null json for " + userGuid);
                    json = com.portableehr.patientsdk.utils.FileUtils.readUserModelJson(EHRLibRuntime.getInstance().getContext());
                    if (json != null) {
                        um = UserModel.fromJson(json);
                    } else {
                        Log.e(CLASSTAG, "loadFromDevice : got null json for readUserModelJson");
                    }
                }
            } catch (Exception e) {
                Log.e(CLASSTAG, "Unable to decrypt json");
                um = UserModel.fromJson(json);
            }

//            if (json != null) {
//                um = UserModel.fromJson(json);
//            } else {
//                Log.e(CLASSTAG, "loadFromDevice : got null json for " + userGuid);
//                json = com.portableehr.patientsdk.utils.FileUtils.readUserModelJson(EHRLibRuntime.getInstance().getContext());
//                if (json != null) {
//                    um = UserModel.fromJson(json);
//                } else {
//                    Log.e(CLASSTAG, "loadFromDevice : got null json for readUserModelJson");
//                }
//            }
        } else {
            Log.e(CLASSTAG, "loadFromDevice : null path for user model !");
            String json = com.portableehr.patientsdk.utils.FileUtils.readUserModelJson(EHRLibRuntime.getInstance().getContext());
            if (json != null) {
                um = UserModel.fromJson(json);
            } else {
                Log.e(CLASSTAG, "loadFromDevice : got null json for readUserModelJson");
            }
        }

        return um;
    }

    public static UserModel loadFromDevice(IBUser user) {
        UserModel um = loadFromDevice(user.getGuid());
        if (um != null) {
            um.user = user;
        } else {
            Log.e(CLASSTAG, "loadFromDevice : null user model !");
        }
        return um;
    }

    /**
     * @param user the user for which we are creating on device.
     * @return a user model
     */
    public static UserModel createOnDevice(IBUser user) {
        UserModel um = new UserModel(user);
        um.save();
        return um;
    }

    public static boolean userExistsOnDevice(IBUser user) {
        if (user == null) {
            Log.e(CLASSTAG, "userExistsOnDevice : invoked with null user");
            return false;
        }

        return userExistsOnDevice(user.getGuid());

    }

    public static boolean userExistsOnDevice(String userGuid) {
        String dirPath = FileUtils.pathOfUserDirectory(userGuid);
        if (FileUtils.dirExists(dirPath)) {
            return false;
        }
        return true;
    }

    public static boolean wipeUserOnDevice(IBUser user) {
        if (user == null) {
            Log.e(CLASSTAG, "wipeUserOnDevice : invoked with null user");
            return false;
        }
        return wipeUserOnDevice(user.getGuid());
    }

    public static boolean wipeUserOnDevice(String userGuid) {

        String dirPath = FileUtils.pathOfUserDirectory(userGuid);
        if (FileUtils.dirExists(dirPath)) {
            return true;
        }
        boolean success = FileUtils.rmdir(dirPath);
        return success;
    }

    private static final String fileName = "userModel.json";

    private IBUser user;
    private boolean isVaultWarningRead;
    private boolean isEULAread;

    public boolean shouldChangePassword() {
        return shouldChangePassword;
    }

    public void login(String password) {
        this.shouldChangePassword = password.contentEquals(NEW_PASSWORD);
    }

    private boolean shouldChangePassword;

    //*********************************************************************************************/
    //** Abstract Model implementation                                                           **/
    //*********************************************************************************************/

    @Override
    protected void implementPollAction() {
        signalPollActionComplete();
    }

    @Override
    protected void implementPollActionCancel() {
        signalPollActionCancelComplete();
    }

    @Override
    protected void onPollActionStart() {
    }

    @Override
    protected void onPollActionCancelStart() {
    }

    @Override
    public long getSmallestPollIntervalInSeconds() {
        return mShortPollInterval;
    }

    @Override
    public long getLargestPollingIntervalInSeconds() {
        return mLongPollInterval;
    }

    //*********************************************************************************************/
    //** getters and setters                                                                     **/
    //*********************************************************************************************/

    public IBUser getUser() {
        return user;
    }

    public HashMap<String, IBDispensaryStaffUser> dispensaryStaff;

    public HashMap<String, IBDispensaryStaffUser> getDispensaryStaff() {
        return dispensaryStaff;
    }

    public void setDispensaryStaff(HashMap<String, IBDispensaryStaffUser> dispensaryStaff) {
        this.dispensaryStaff = dispensaryStaff;
    }

    public boolean updateWithDispensaryStaff(HashMap<String, IBDispensaryStaffUser> other, boolean save) {
        // todo

        if (null == getDispensaryStaff() || getDispensaryStaff().size() == 0) {
            setDispensaryStaff(other);
            if (save) {
                return this.save();
            }
        }

        // todo : this is a true update

        Log.w(getLogTAG(), "updateWithDispensaryStaff: NOT IMPLEMENTED HYET");
        return true;
    }

    public boolean updateWithUser(IBUser other, boolean save) {
        if (null == this.getUser() || null == other || !other.getGuid().contentEquals(this.user.getGuid())) {
            Log.e(getLogTAG(), "updateWithUser : Attempt to update when other has different guid.");
            return false;
        } else {
            this.user.setEmailVerified(other.isEmailVerified());
            this.user.setMobileVerified(other.isMobileVerified());
            this.user.setIdentityVerified(other.isIdentityVerified());
            this.user.setContact(other.getContact());
            this.user.setLastUpdated(other.getLastUpdated());
            this.user.setStatus(other.getStatus());
            this.user.setDeviceEmailVerified(other.isDeviceEmailVerified());
            this.user.setDeviceMobileVerified(other.isDeviceMobileVerified());
            this.user.setForcePasswordChange(other.isForcePasswordChange());

        }
        if (save) {
            return this.save();
        }
        return true;
    }

    public boolean isVaultWarningRead() {
        return isVaultWarningRead;
    }

    public void setVaultWarningRead(boolean vaultWarningRead) {
        this.isVaultWarningRead = vaultWarningRead;
    }

    public boolean isEULAread() {
        return isEULAread;
    }

    public void setEULAread(boolean EULAread) {
        this.isEULAread = EULAread;
    }

    //*********************************************************************************************/
    //** persist  (public)                                                                       **/
    //*********************************************************************************************/

    //*********************************************************************************************/
    //** persist  (private)                                                                      **/
    //*********************************************************************************************/


    public boolean save() {


        UserModel um = this;
        boolean ret = false;
        File fd = EHRLibRuntime.getInstance().getContext().getFilesDir();
        // in the clinic app, we never save the apiKey (it is fetched upon login)
        String apiKey = this.user.getApiKey();
//        this.user.setApiKey(null);
        if (null == fd) {
            Log.e(getLogTAG(), "Unable to get files dir from ApplicationExt");
        } else {
            if (fd.isDirectory()) {

                try {
                    String dirPath = getFolderFQP(getUser().getGuid());
                    if (FileUtils.dirExists(dirPath)) {
                        FileUtils.mkdir(dirPath);
                    }
                    Writer out = new BufferedWriter(new OutputStreamWriter(
                            new FileOutputStream(getFQN()), StandardCharsets.UTF_8));

//                    out.write(um.asJson());

                    EncryptionUtils.generateKey();
                    String encryptedData = null;
                    try {
                        encryptedData = EncryptionUtils.encrypt(um.asJson());
                        out.write(encryptedData);
                    } catch (Exception e) {
                        System.out.println("Caught exception while encrypting data");
                        throw new RuntimeException(e);
                    }

                    out.close();
                    ret = true;
                } catch (Exception e) {
                    Log.e(getLogTAG(), "Caught exception while writing[" + getFQN() + "]\n", e);
                }

            } else {
                Log.e(getLogTAG(), "getFilesDIr returned a file !!!");
            }
        }
        this.user.setApiKey(apiKey);

        return ret;
    }

    public static boolean existsOnDevice(String userGuid) {
        String fileName = getFQN(userGuid);
        if (fileName != null) {
            File f = new File(fileName);
            return f.exists();
        }
        Log.e(CLASSTAG, "existsOnDevice : error for userGuid " + userGuid);
        return false;
    }

    private static UserModel initOnDevice(IBUser user) {

        UserModel um = new UserModel(user);
        boolean success = um.save();
        if (!success) {
            um = null;
            Log.e(CLASSTAG, "Unable to write the userModel, device not inited properly.");
        }
        return um;
    }

    private static String getFQN(IBUser user) {
        if (null == user) {
            Log.e(CLASSTAG, "getFQN : received null user , bailing out.");
            return null;
        }
        return getFQN(user.getGuid());
    }

    private static String getFQN(String userGuid) {
        String folderPath = getFolderFQP(userGuid);
        if (null == folderPath) {
            return null;
        }

        String filePath = folderPath + File.separator + fileName;
        return filePath;
    }

    private static String getFolderFQP(String userGuid) {
        if (null == userGuid) {
            Log.e(CLASSTAG, "getFolderFQP : received null userGuid , bailing out.");
            return null;
        }

        String filePath = null;
        File fd = EHRLibRuntime.getInstance().getContext().getFilesDir();
        if (null == fd) {
            Log.e(CLASSTAG, "getFolderFQP : Unable to get files dir from ApplicationExt, bailing out.");
        } else {
            filePath = fd.getPath() + File.separator + userGuid;
        }
        return filePath;
    }

    private String getFQN() {
        return getFQN(this.user.getGuid());
    }


    //region Countable

    private final static String CLASSTAG = kModulePrefix + "." + UserModel.class.getSimpleName();
    @GSONexcludeOutbound
    private String TAG;
    private static int lifeTimeInstances;
    private static int numberOfInstances;
    @GSONexcludeOutbound
    private int instanceNumber;
    @GSONexcludeOutbound
    private static boolean classCountable = false;

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

    protected String getLogTAG() {
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

    public static UserModel fromJson(String json) {
        GsonBuilder builder = GsonFactory.standardBuilder();
        Gson jsonDeserializer = builder.create();
        UserModel theObject = jsonDeserializer.fromJson(json, UserModel.class);
        return theObject;
    }

    //endregion


}
