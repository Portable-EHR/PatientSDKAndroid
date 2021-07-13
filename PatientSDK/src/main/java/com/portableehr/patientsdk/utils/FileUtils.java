package com.portableehr.patientsdk.utils;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.portableehr.sdk.models.notification.NotificationModel;
import com.portableehr.sdk.models.service.ServiceModel;
import com.portableehr.sdk.network.gson.GSONexcludeOutbound;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

import static com.portableehr.patientsdk.utils.PatientRuntimeConstants.kDefaultClassCountable;
import static com.portableehr.sdk.EHRLibRuntime.kModulePrefix;


public class FileUtils {


    public final static String appStateFileName           = "appState.json";
    public final static String userModelFileName          = "userModel.json";
    public final static String notificationsModelFileName = "notificationsModel.json";
    public final static String serviceModelFileName       = "serviceModel.json";

    public static String readAppStateJson(Application app) {
        String filePath = getFQN(app, appStateFileName);
        return FileUtils.readJsonFromFilePath(filePath);
    }

    public static boolean appStateExistsOnDevice(Application app){
        return fileExists(app, appStateFileName);

    }

    /**
     * Reads the userModel persisted on the device, as a json string
     *
     * @return the json, UTF-8
     */
    public static String readUserModelJson(Context context) {
        String filePath = getFQN(context, userModelFileName);
        return FileUtils.readJsonFromFilePath(filePath);
    }



    //region NotificationsModel

    public static String readNotificationsModelJson(Application app) {
        String filePath = getFQN(app, notificationsModelFileName);
        return FileUtils.readJsonFromFilePath(filePath);
    }

    public static boolean notificationsModelExistsOnDevice(Application app) {
        return fileExists(app, notificationsModelFileName);
    }

    public static boolean saveNotificationsModelOnDevice(Application app, NotificationModel notificationModel) {
        boolean ret      = false;
        String  filePath = getFQN(app, notificationsModelFileName);
        if (notificationsModelExistsOnDevice(app)) { // todo, could be a major regression : was serviceModelExistsOnDevice
            deleteNotificationsModelFromDevice(app);
        }
        try {
            Writer out = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(getFQN(app, notificationsModelFileName)),
                            StandardCharsets.UTF_8)
            );
            out.write(notificationModel.asJson());
            out.close();
            ret = true;
        } catch (Exception e) {
            Log.e(CLASSTAG, "Caught exception while writing[" + getFQN(app, notificationsModelFileName) + "]\n", e);
        }

        return ret;
    }

    public static void deleteNotificationsModelFromDevice(Application app){
        String  filePath = getFQN(app, notificationsModelFileName);
        File file = new File(filePath);
        if (!file.delete()) {
            Log.e(CLASSTAG, "*** Unable to delete previous notificationsModel");
        }
    }

    //endregion

    //region Service Model

    public static String reasServiceModelJson(Application app) {
        String filePath = getFQN(app, serviceModelFileName);
        return FileUtils.readJsonFromFilePath(filePath);
    }

    public static boolean serviceModelExistsOnDevice(Application app) {
        return fileExists(app, serviceModelFileName);
    }

    public static boolean saveServiceModelOnDevice(Application app, ServiceModel serviceModel) {
        boolean ret      = false;
        String  filePath = getFQN(app, serviceModelFileName);
        if (serviceModelExistsOnDevice(app)) {
            File file = new File(filePath);
            if (!file.delete()) {
                Log.e(CLASSTAG, "*** Unable to delete previous serviceModel");
            }
        }
        try {
            Writer out = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(getFQN(app, serviceModelFileName)),
                            StandardCharsets.UTF_8)
            );
            out.write(serviceModel.asJson());
            out.close();
            ret = true;
        } catch (Exception e) {
            Log.e(CLASSTAG, "Caught exception while writing[" + getFQN(app, serviceModelFileName) + "]\n", e);
        }

        return ret;
    }

    public static void deleteServiceModelFromDevice(Application app){
        String  filePath = getFQN(app, serviceModelFileName);
        File file = new File(filePath);
        if (!file.delete()) {
            Log.e(CLASSTAG, "*** Unable to delete previous serviceModel");
        }
    }

    //endregion


    //region IO support

    /**
     * @param filePath Fully qualified path
     * @return String the json, UTF-8
     */
    private static String readJsonFromFilePath(String filePath) {
        String json = null;
        if (null != filePath) {
            try {
                FileReader      fr   = new FileReader(filePath);
                FileInputStream fis  = new FileInputStream(filePath);
                Reader          isr  = new InputStreamReader(fis, StandardCharsets.UTF_8);
                byte[]          data = new byte[fis.available()];
                int             bc   = fis.read(data);
                fis.close();
                if (bc > 0) {
                    json = new String(data);
                } else {
                    Log.e(CLASSTAG, "readAppStateJson : got empty file[" + filePath + "]");
                }
            } catch (Exception e) {
                Log.e(CLASSTAG, "readAppStateJson : Caught exception when reading file[" + filePath + "]");
                Log.wtf(CLASSTAG, e);
            }
        }
        return json;
    }

    private static String getFQN(Context context, String fileName) {
        String filePath = null;
        File   fd       = context.getFilesDir();
        if (null == fd) {
            Log.e(CLASSTAG, "Unable to get files dir from ApplicationExt");
        } else {
            filePath = fd.getAbsolutePath() + "/" + fileName;
        }
        return filePath;
    }


    private static boolean fileExists(Application app, String fileName) {
        File fd = app.getFilesDir();
        if (null == fd) {
            return false;
        }
        String filePath = fd.getAbsolutePath() + "/" + fileName;
        File   f        = new File(filePath);
        return f.exists();
    }
    //endregion


    //region Countable

    private final static String  CLASSTAG       = kModulePrefix + "." + FileUtils.class.getSimpleName();
    @GSONexcludeOutbound
    private              String  TAG;
    private static       int     lifeTimeInstances;
    private static       int     numberOfInstances;
    @GSONexcludeOutbound
    private              int     instanceNumber;
    @GSONexcludeOutbound
    private static       boolean classCountable = kDefaultClassCountable;

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

    /**
     * By default File#delete fails for non-empty directories, it works like "rm".
     * We need something a little more brutual - this does the equivalent of "rm -r"
     *
     * @param path Root File Path
     * @return true iff the file and all sub files/directories have been removed
     * @throws FileNotFoundException when a file is absent or any other error occurs
     */
    public static boolean deleteRecursive(File path) throws FileNotFoundException {
        if (!path.exists()) {
            throw new FileNotFoundException(path.getAbsolutePath());
        }
        boolean ret = true;
        if (path.isDirectory()) {
            for (File f : path.listFiles()) {
                ret = ret && FileUtils.deleteRecursive(f);
            }
        }
        return ret && path.delete();
    }

}

