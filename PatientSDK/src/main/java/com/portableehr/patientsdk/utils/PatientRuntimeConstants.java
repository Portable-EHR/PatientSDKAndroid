package com.portableehr.patientsdk.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Size;

import com.portableehr.sdk.network.NAO.inbound.IBVersion;

import java.util.Date;

@SuppressWarnings("unused")
public class PatientRuntimeConstants {


    public static int pelsToDp(int pels) {
        final float scale = kDensity;
        int         dps   = (int) (pels / scale + 0.5f);
        return dps;
    }

    public static int dpToPels(int dp) {
        final float scale = kDensity;
        int         pels  = (int) (dp * scale + 0.5f);
        return pels;
    }

    public static Size screenSizeInPels() {
        return kScreenSizeInPels;
    }

    public static Size screenSizeInDp() {
        return kScreenSizeInDp;
    }

    private static DisplayMetrics kDisplayMetrics;
    private static Size           kScreenSizeInPels;
    private static Size           kScreenSizeInDp;
    public static  float          kDensity;
    public static  float          kScaledDensity;

    //region App constants
    public static final String    kAppAlias       = "pehr.patient.android";
    public static final String    kAppGuid        = "27991c67-79db-4a71-a69d-9ef8737940b7";
    public static final IBVersion kAppVersion     = new IBVersion("1.1.038");
    public static final int       kAppBuildNumber = 1;
    //endregion

    //region Precanned accounts : Guest, Google, Guest
    public static final String kGuestApiKey    = "K7ICfFOwS3ELdHfAzWBhPt";
    public static final String kGuestUserGuid  = "67b1c035-9d12-4bd6-9f94-df75182da183";
    public static final String kGoogleApiKey   = "1VV77hKKaI8wqnS0VUtRM9";
    public static final String kGoogleUserGuid = "7a7607ac-3e12-4b95-b247-2cd74ecd1cb0";
    public static final String kGoogleUsername = "google.portableehr.ca@mailinator.com";
    public static final String kYlbApiKey      = "AIJyExFhfYrsQrovOx950c";
    public static final String kYlbUserGuid    = "d358d9eb-32e7-4722-8589-6c61e16971c6";
    public static final String kYlbUsername    = "yves@leborgne.org";
    //endregion

    //region User look & feel
    public static final int kOpacityDisabled = 100;
    public static final int kOpacityyEnabled = 255;
    public static final int kOpacitySelected = 175;

    public static final int kColorBackground     = 0x225B78;
    public static final int kColorBackgroundDark = 0xebf5f6;
    public static final int kColorText           = 0x225B78;
    public static final int kColorErrorText      = 0xfeb01a;

    public static final boolean kDefaultClassCountable = true;
    //endregion


    public static final String kSecureCredentialsKey = "kSecureCredentialsKey";
    public static final String kSecureStoreName      = "com.portableehr.patient";
    public static final String kModulePrefix         = "patient";

    public static final boolean kNotificationsImplemented  = false;         // debuggish, consider removing
    public static final String  kEventNotificationsUpdated = "kEventNotificationsUpdated";
    public static final String  kEventUserModelUpdated     = "kEventUserModelUpdated";
    public static final String  kEventServiceModelUpdated  = "kEventServiceModelUpdated";
    public static final String  kEventDeactivated          = "kEventDeactivated";

    public static String getCountableInstanceLogLabel(int instanceNumber, int numberOfInstances) {
        return Integer.toHexString(instanceNumber) + "/" + Integer.toHexString(numberOfInstances);
    }

    //region intent REQUEST and RESULT codes

    public static final int ACCESS_SCAN_REQUEST_CODE       = 237;
    public static final int ACCESS_SCAN_RESULT_SUCCESS     = 238;
    public static final int ACCESS_SCAN_RESULT_FAIL        = 237;
    public static final int PM_VIEW_OWN_REQUEST_CODE       = 240;
    public static final int PM_VIEW_ACCESSED_REQUERST_CODE = 241;
    public static final int APPNT_VIEW_REQUEST_CODE        = 245;
    public static final int APPNT_RESULT_CONRIFMED         = 246;
    public static final int APPNT_RESULT_CANCELLED         = 247;
    public static final int APPNT_RESULT_FAILED            = 248;

    //endregion


    @SuppressWarnings("deprecation")
    public static Date kEpochStart = new Date("1 jan 1970 GMT");

    static {


    }

    public static void initialize(Context context) {
        kDisplayMetrics = new DisplayMetrics();
        Activity ac = (Activity) context;
        ac.getWindowManager().getDefaultDisplay().getMetrics(kDisplayMetrics);
        kDensity = kDisplayMetrics.density;
        kScaledDensity = kDisplayMetrics.scaledDensity;
        kScreenSizeInPels = new Size(kDisplayMetrics.widthPixels, kDisplayMetrics.heightPixels);
        kScreenSizeInDp = new Size(pelsToDp(kScreenSizeInPels.getWidth()), pelsToDp(kScreenSizeInPels.getHeight()));
    }


}
