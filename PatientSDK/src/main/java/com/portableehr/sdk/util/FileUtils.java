package com.portableehr.sdk.util;

import androidx.annotation.Nullable;
import android.util.Log;

import com.portableehr.sdk.EHRLibRuntime;
import com.portableehr.sdk.models.notification.NotificationModel;
import com.portableehr.sdk.models.service.ServiceModel;
import com.portableehr.sdk.network.NAO.calls.NotificationsListCall;
import com.portableehr.sdk.network.NAO.inbound.IBUser;
import com.portableehr.sdk.network.gson.GSONexcludeOutbound;
import com.portableehr.sdk.network.protocols.ICaller;
import com.portableehr.sdk.network.protocols.ICompletionHandler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.portableehr.sdk.EHRLibRuntime.kModulePrefix;


/**
 * Created by : yvesleborg
 * Date       : 2017-04-24
 * <p>
 * Copyright Portable Ehr Inc, 2018
 */

public class FileUtils {


    {
        setClassCountable(false);
    }

    public final static String appStateFileName           = "appState.json";
    public final static String serviceModelFileName       = "serviceModel.json";
    public final static String notificationsModelFileName = "notificationsModel.json";
    public final static String userModelFileName          = "userModel.json";

    @SuppressWarnings("unused")
    public static String readAppStateJson() {
        String filePath = getFQN(appStateFileName);
        return FileUtils.readJsonFromFilePath(filePath);
    }

    @SuppressWarnings("unused")
    public static boolean appStateExistsOnDevice() {
        return fileExistsInAppFilesDir(appStateFileName);

    }


    //region NotificationsModel

    /**
     * @param user the user for which we are creating on device.
     * @return a user model
     */
    public static NotificationModel createOnDevice(IBUser user) {
        EHRLibRuntime.getInstance().getNotificationModel().setUser(user, new ICompletionHandler() {
            @Override
            public void handleSuccess(@Nullable ICaller theCall) {
                EHRLibRuntime.getInstance().getNotificationModel().save();
            }

            @Override
            public void handleError(@Nullable ICaller theCall) {
                Log.e(CLASSTAG, "An error occured when creating a notification model on the device.");
                if (theCall != null) {
                    try {
                        NotificationsListCall nlc       = (NotificationsListCall) theCall;
                        String                reqstatus = nlc.getRequestStatus().asJson();
                        Log.d(CLASSTAG, "Got response for backend : \n" + reqstatus);
                    } catch (Exception e) {
                        String es = StringUtils.getStackTrace(e);
                        Log.e(CLASSTAG, "An error occured when creating a notification model on the device.\n" + es);
                    }
                }
            }

            @Override
            public void handleCancel(@Nullable ICaller theCall) {
                handleError(theCall);
            }
        });

        return EHRLibRuntime.getInstance().getNotificationModel();
    }

    public static String readNotificationsModelJson() {
        String filePath = getFQN(notificationsModelFileName);
        return FileUtils.readJsonFromFilePath(filePath);
    }

    public static boolean notificationsModelExistsOnDevice() {
        return fileExists(notificationsModelFileName);
    }

    public static boolean saveNotificationsModelOnDevice(NotificationModel notificationModel) {
        boolean ret      = false;
        String  filePath = getFQN(notificationsModelFileName);
        if (notificationsModelExistsOnDevice()) { // todo, could be a major regression : was serviceModelExistsOnDevice
            deleteNotificationsModelFromDevice();
        }
        try {
            Writer out = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(getFQN(notificationsModelFileName)),
                            StandardCharsets.UTF_8)
            );
            out.write(notificationModel.asJson());
            out.close();
            ret = true;
        } catch (Exception e) {
            Log.e(CLASSTAG, "Caught exception while writing[" + getFQN(notificationsModelFileName) + "]\n", e);
        }

        return ret;
    }

    public static void deleteNotificationsModelFromDevice() {
        String filePath = getFQN(notificationsModelFileName);
        File   file     = new File(filePath);
        if (!file.delete()) {
            Log.e(CLASSTAG, "*** Unable to delete previous notificationsModel");
        }
    }

    public static boolean saveJson(String fqn, String json) {
        boolean ret = false;
        try {
            Writer out = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(fqn),
                            StandardCharsets.UTF_8)
            );
            out.write(json);
            out.close();
            ret = true;
        } catch (Exception e) {
            Log.e(CLASSTAG, "Caught exception while writing[" + fqn + "]\n", e);
            Log.e(CLASSTAG, DebugUtils.formatStackTrace(e.getStackTrace()));
        }

        return ret;
    }
    //endregion


    //region Service Model

    public static String reasServiceModelJson() {
        String filePath = getFQN(serviceModelFileName);
        return FileUtils.readJsonFromFilePath(filePath);
    }

    public static boolean serviceModelExistsOnDevice() {
        return fileExistsInAppFilesDir(serviceModelFileName);
    }

    public static boolean saveServiceModelOnDevice(ServiceModel serviceModel) {
        boolean ret      = false;
        String  filePath = getFQN(serviceModelFileName);
        if (serviceModelExistsOnDevice()) {
            File file = new File(filePath);
            if (!file.delete()) {
                Log.e(CLASSTAG, "*** Unable to delete previous serviceModel");
            }
        }
        try {
            Writer out = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(getFQN(serviceModelFileName)),
                            StandardCharsets.UTF_8)
            );
            out.write(serviceModel.asJson());
            out.close();
            ret = true;
        } catch (Exception e) {
            Log.e(CLASSTAG, "Caught exception while writing[" + getFQN(serviceModelFileName) + "]\n", e);
        }

        return ret;
    }

    public static void deleteServiceModelFromDevice() {
        String filePath = getFQN(serviceModelFileName);
        File   file     = new File(filePath);
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
    public static String readJsonFromFilePath(String filePath) {
        String json = null;
        if (null != filePath) {
            try {
                FileInputStream fis  = new FileInputStream(filePath);
                byte[]          data = new byte[fis.available()];
                int             bc   = fis.read(data);
                fis.close();
                if (bc > 0) {
                    json = new String(data);
                } else {
                    Log.e(CLASSTAG, "readJsonFromFilePath : got empty file[" + filePath + "]");
                }
            } catch (Exception e) {
                Log.e(CLASSTAG, "readJsonFromFilePath : Caught exception when reading file[" + filePath + "]");
                Log.wtf(CLASSTAG, e);
            }
        }
        return json;
    }

    private static String getFQN(String fileName) {
        String filePath = null;
        File   fd       = EHRLibRuntime.getInstance().getContext().getFilesDir();
        if (null == fd) {
            Log.e(CLASSTAG, "Unable to get files dir from ApplicationExt");
        } else {
            filePath = fd.getAbsolutePath() + "/" + fileName;
        }
        return filePath;
    }


    private static boolean fileExistsInAppFilesDir(String fileName) {
        File fd = EHRLibRuntime.getInstance().getContext().getFilesDir();
        if (null == fd) {
            return false;
        }
        String filePath = fd.getAbsolutePath() + "/" + fileName;
        File   f        = new File(filePath);
        return f.exists();
    }
    //endregion

    //region directory handling
    @SuppressWarnings("UnusedReturnValue")
    public static boolean mkdir(String folderPath) {
        File fd = EHRLibRuntime.getInstance().getContext().getFilesDir();
        if (null == fd) {
            Log.wtf(CLASSTAG, "mkdir: context Returns null files dir!");
            return false;
        }
        File    f       = new File(folderPath);
        boolean success = f.mkdirs();
        if (!success) {
            Log.e(CLASSTAG, "mkdir: failed to makedir on " + folderPath);
        }
        return success;
    }

    public static boolean rmdir(String folderPath) {

        Log.d(CLASSTAG, "rmdir: deleting folder " + folderPath);
        File f = new File(folderPath);
        if (!f.exists()) {
            return true;
        }
        File[] files = f.listFiles();
        assert files != null;
        for (File cf : files) {
            boolean success;
            if (cf.isDirectory()) {
                success = FileUtils.rmdir(cf.getPath());
            } else {
                Log.d(CLASSTAG, "rmdir: deleting file " + cf.getPath());
                success = cf.delete();
            }
            if (!success) {
                return false;
            }
        }
        return f.delete();
    }

    @SuppressWarnings("unused")

    /*
     * beware of this : the entirety of the files folder will be wiped.
     */

    public static boolean purgeRoot() {
        Log.w(CLASSTAG, "purgeRoot : invoked, proceeding at risk !!!");

        File fd = EHRLibRuntime.getInstance().getContext().getFilesDir();
        if (null == fd) {
            Log.wtf(CLASSTAG, "purgeRoot: context Returns null files dir, bailing out !!!");
            return false;
        }
        List<File> content = new ArrayList<>(Arrays.asList(Objects.requireNonNull(fd.listFiles())));

        for (File cf : content) {
            boolean res;
            if (cf.isDirectory()) {
                try {
                    res = deleteRecursive(cf);
                } catch (Exception ex) {
                    Log.wtf(CLASSTAG, "purgeRoot: ", ex);
                    return false;
                }
            } else {
                res = cf.delete();
            }
            if (!res) {
                Log.e(CLASSTAG, "purgeRoot : failed to delete " + cf.getPath());
                return false;
            }
        }

        return true;
    }

    public static String getRootPath() {
        File fd = EHRLibRuntime.getInstance().getContext().getFilesDir();
        if (null == fd) {
            Log.wtf(CLASSTAG, "purgeRoot: context Returns null files dir, bailing out !!!");
            return null;
        }
        return fd.getPath();
    }

    public static boolean dirExists(String folderPath) {
        File f = new File(folderPath);
        if (!f.exists()) {
            return true;
        }
        return !f.isDirectory();
    }

    public static boolean fileExists(String filePath) {
        File f = new File(filePath);
        if (!f.exists()) {
            return false;
        }
        return (!f.isDirectory());
    }

    public static String pathOfUserDirectory(IBUser user) {
        return pathOfUserDirectory(user.getGuid());
    }

    public static String pathOfUserDirectory(String userGuid) {

        if (null == userGuid) {
            Log.wtf(CLASSTAG, "pathOfUserDirectory: invoked with null guid!");
            return null;
        }

        File fd = EHRLibRuntime.getInstance().getContext().getFilesDir();
        if (null == fd) {
            Log.wtf(CLASSTAG, "pathOfUserDirectory: context Returns null files dir!");
            return null;
        }

        String dir = fd.getAbsolutePath() + File.separator + userGuid;
        return dir;
    }


    @SuppressWarnings("unused")
    public static String[] listRootDirectoriesPath() {
        List<String> result = new ArrayList<>();
        File         fd     = EHRLibRuntime.getInstance().getContext().getFilesDir();
        if (null == fd) {
            Log.wtf(CLASSTAG, "listRootDirectories: context Returns null files dir!");
            String[] res = new String[result.size()];
            return result.toArray(res);
        }

        for (File cf : Objects.requireNonNull(fd.listFiles())) {
            if (!cf.isDirectory()) {
                result.add(fd.getPath());
            }
        }
        String[] res = new String[result.size()];
        return result.toArray(res);
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
            for (File f : Objects.requireNonNull(path.listFiles())) {
                ret = ret && FileUtils.deleteRecursive(f);
            }
        }
        return ret && path.delete();
    }

}
