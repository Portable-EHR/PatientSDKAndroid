package com.portableehr.patientsdk.models;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.portableehr.patientsdk.utils.FileUtils;
import com.portableehr.sdk.models.AbstractPollingModel;
import com.portableehr.sdk.network.NAO.inbound.IBUser;
import com.portableehr.sdk.network.gson.GSONexcludeOutbound;
import com.portableehr.sdk.network.gson.GsonFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

import static com.portableehr.sdk.EHRLibRuntime.kModulePrefix;


@SuppressWarnings("FieldCanBeLocal")
public class UserModel extends AbstractPollingModel {


    private static long mShortPollInterval = 15 * 60;          // 15 minutes
    private static long mLongPollInterval  = 12 * 60 * 60;     // 12 hours

    //*********************************************************************************************/
    //** ctors and shit                                                                          **/
    //*********************************************************************************************/

    private UserModel() {
        this.setUser(IBUser.guest());
        this.setEULAread(false);
        this.setVaultWarningRead(false);
        setClassCountable(false);
        onNew();
    }

    public static UserModel getInstance(Context context) {

        if (null == ourInstance) {
            if (existsOnDevice(context)) {
                ourInstance = loadFromDevice(context);
            } else {
                ourInstance = initOnDevice(context);
            }

            if (ourInstance == null) {
                throw new RuntimeException("Unable to create a userModel instance.");
            }
        }
        return ourInstance;
    }

    //*********************************************************************************************/
    //** end boilerplate stuff that i dunno how to make in java                                  **/
    //*********************************************************************************************/

    private static final String    fileName    = "userModel.json";
    private static       UserModel ourInstance = null;

    private IBUser  user;
    private boolean isVaultWarningRead;
    private boolean isEULAread;

    //*********************************************************************************************/
    //** Abstract Model implementation                                                           **/
    //*********************************************************************************************/

//    @Override
    protected boolean getVerbose() {
        return UserModel.classCountable;
    }

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

    public void setUser(IBUser user) {
        this.user = user;
    }

    public boolean updateWithUser(Application app, IBUser other, boolean save) {
        if (this.user == null) {
            this.user = other;
        } else {
            this.user.setEmailVerified(other.isEmailVerified());
            this.user.setMobileVerified(other.isMobileVerified());
            this.user.setIdentityVerified(other.isIdentityVerified());
            this.user.setContact(other.getContact());
            this.user.setLastUpdated(other.getLastUpdated());
            this.user.setStatus(other.getStatus());
            // todo : need to update patient too
        }
        if (save) {
            return this.save(app);
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

    @SuppressWarnings("UnusedReturnValue")
    public static boolean deleteFromDevice(Context context) {
        String filePath = getFQN(context);
        File   file     = new File(filePath);
        return file.delete();
    }

    @SuppressWarnings("UnusedReturnValue")
    public static boolean resetOnDevice(Context context) {
        getInstance(context).user = IBUser.guest();
        getInstance(context).isEULAread = false;
        getInstance(context).isVaultWarningRead = false;
        return getInstance(context).save(context);
    }

    public static boolean saveOnDevice(Application app) {
        return getInstance(app).save(app);
    }

    public static UserModel readFromDevice(Application app) {
        UserModel userModel = loadFromDevice(app);
        return userModel;
    }

    //*********************************************************************************************/
    //** persist  (private)                                                                      **/
    //*********************************************************************************************/


    public boolean save(Context context) {
        UserModel um  = this;
        boolean   ret = false;
        File      fd  = context.getFilesDir();
        if (null == fd) {
            Log.e(getLogTAG(), "Unable to get files dir from ApplicationExt");
        } else {
            if (fd.isDirectory()) {

                try {
                    Writer out = new BufferedWriter(new OutputStreamWriter(
                            new FileOutputStream(getFQN(context)), StandardCharsets.UTF_8));
                    out.write(um.asJson());
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

    private static UserModel loadFromDevice(Context context) {

        String    json = FileUtils.readUserModelJson(context);
        UserModel um   = null;
        if (json != null) {
            um = UserModel.fromJson(json);
        }
//        UserModel um       = null;
//        String    filePath = getFQN();
//        File      file     = new File(filePath);
//
//        if (file.exists()) {
//            try {
//                FileReader  fr   = new FileReader(file);
//                GsonBuilder b    = standardBuilder();
//                Gson        gson = b.create();
//                um = gson.fromJson(fr, UserModel.class);
//            } catch (Exception e) {
//                Log.e(getLogTAG(), "Caught exception while opening[" + filePath + "]\n", e);
//                e.printStackTrace();
//            }
//
//        }
        return um;
    }

    public static boolean existsOnDevice(Context context) {
        String fileName = getFQN(context);
        File   f        = new File(fileName);
        return f.exists();
    }

    private static UserModel initOnDevice(Context context) {

        UserModel um      = new UserModel();
        boolean   success = um.save(context);
        if (!success) {
            um = null;
            Log.e(CLASSTAG, "Unable to write the userModel, device not inited properly.");
        }
        return um;
    }

    private static String getFQN(Context context) {
        String filePath = null;
        File   fd       = context.getFilesDir();
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


    //region Countable
    static {
        setClassCountable(false);
    }

    private final static String  CLASSTAG = kModulePrefix + "." + UserModel.class.getSimpleName();
    @GSONexcludeOutbound
    private              String  TAG;
    private static       int     lifeTimeInstances;
    private static       int     numberOfInstances;
    @GSONexcludeOutbound
    private              int     instanceNumber;
    @GSONexcludeOutbound
    private static       boolean classCountable;

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
        GsonBuilder builder        = GsonFactory.standardBuilder();
        Gson        jsonSerializer = builder.create();
        String      theJson        = jsonSerializer.toJson(this, this.getClass());
        return theJson;
    }

    public static UserModel fromJson(String json) {
        GsonBuilder builder          = GsonFactory.standardBuilder();
        Gson        jsonDeserializer = builder.create();
        UserModel   theObject        = jsonDeserializer.fromJson(json, UserModel.class);
        return theObject;
    }

    //endregion


}

