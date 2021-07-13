package com.portableehr.sdk.network.NAO.inbound;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.portableehr.sdk.network.gson.GSONexcludeOutbound;
import com.portableehr.sdk.network.gson.GsonFactory;

import java.util.Date;
import java.util.List;

import static com.portableehr.sdk.EHRLibRuntime.kModulePrefix;

/**
 * Created by : yvesleborg
 * Date       : 2017-03-31
 * <p>
 * Copyright Portable Ehr Inc, 2019
 */

@SuppressWarnings("unused")
public class IBDeviceInfo {

    private String  deviceGuid;
    private String  osType;
    private String  osVersion;
    private String  manufacturer;
    private String  modelCode;
    private Date    createdOn;
    private Date    lastSeen;
    private boolean isPhone;

    @SuppressWarnings("FieldCanBeLocal")
    private boolean localAuthenticationTested = false;
    private boolean hasBiometricDevice;
    private boolean hasEnrolledFingerprints;
    private boolean canAuthenticate;
    private boolean canAuthenticateWithBiometrics;
    private boolean canAuthenticateWithPIN;
    @GSONexcludeOutbound
    private Context context;

    IBDeviceInfo() {
        onNew();
    }


    @SuppressLint("ObsoleteSdkInt")
    public static IBDeviceInfo initFromDevice(Context context) {
        IBDeviceInfo d = new IBDeviceInfo();
        d.context = context;
        d.testAuthentication();
        d.createdOn = new Date();
        d.modelCode = android.os.Build.MODEL;
        d.manufacturer = android.os.Build.MANUFACTURER;
        d.osVersion = android.os.Build.VERSION.RELEASE;
        d.osType = "android";

        if (d.manufacturer.equals("unknown")) {
            if (android.os.Build.HARDWARE.equals("ranchu")) {
                d.manufacturer = "AndroidStudio";
            }
        }

        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm != null) {


            d.isPhone = !(tm.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE);
            if (d.isPhone) {
                //noinspection TryWithIdenticalCatches
                try {
                    if (Build.VERSION.SDK_INT > 22 && context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                        /// TODO: 2017-12-28 fix this , what about SDK 21 ?
                        TelecomManager ttm = (TelecomManager) context.getSystemService(Context.TELECOM_SERVICE);
                        @SuppressLint("MissingPermission") List<PhoneAccountHandle> list = ttm != null
                                                        ? ttm.getCallCapablePhoneAccounts()
                                                        : null;
                        @SuppressLint({"MissingPermission", "HardwareIds"}) String mPhoneNumber = tm.getLine1Number();
                        Log.d(CLASSTAG, "Read phone number from device : " + mPhoneNumber);
                    } else {
                        Log.d(CLASSTAG, "Unabled to obtain callable phone accounts from this device.");
                    }
                } catch (SecurityException e) {
                    Log.e(CLASSTAG, "Unable to fetch phone number from device.");
                    Log.e(CLASSTAG, e.toString());
                    e.printStackTrace();
                } catch (Exception e) {
                    Log.e(CLASSTAG, "Unable to fetch phone number from device.");
                    Log.e(CLASSTAG, e.toString());
                    e.printStackTrace();
                }
            } else {
                Log.e(CLASSTAG, "Got null TelephonyManage, cant detect if this is a phone.");

            }

        }

        return d;

    }

    public void testAuthentication() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.testModernAuthentication();
        } else {
            this.hasBiometricDevice = false;
            this.canAuthenticateWithBiometrics = false;
        }
        this.localAuthenticationTested = true;
    }

//    @TargetApi(Build.VERSION_CODES.M)
    private void testModernAuthentication() {
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                //Fingerprint API only available on from Android 6.0 (M)

                FingerprintManager fingerprintManager = (FingerprintManager) context.getSystemService(Context.FINGERPRINT_SERVICE);
                try {
                    if (null != fingerprintManager && !fingerprintManager.isHardwareDetected()) {
                        // Device doesn't support fingerprint authentication
                        this.hasBiometricDevice = false;
                        this.canAuthenticateWithBiometrics = false;
                    } else if (null != fingerprintManager && !fingerprintManager.hasEnrolledFingerprints()) {
                        // User hasn't enrolled any fingerprints to authenticate with
                        this.hasBiometricDevice = true;
                        this.hasEnrolledFingerprints = false;
                        this.canAuthenticateWithBiometrics = false;
                    } else {
                        this.hasBiometricDevice = true;
                        this.hasEnrolledFingerprints = true;
                        this.canAuthenticateWithBiometrics = true;
                    }
                } catch (SecurityException se) {
                    se.printStackTrace();
                }
            }
        }
    }


    public boolean useFingerprints() {
        return true;
    }


    public String getDeviceGuid() {
        return deviceGuid;
    }

    public void setDeviceGuid(String deviceGuid) {
        this.deviceGuid = deviceGuid;
    }

    public String getOsType() {
        return osType;
    }

    public void setOsType(String osType) {
        this.osType = osType;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModelCode() {
        return modelCode;
    }

    public void setModelCode(String modelCode) {
        this.modelCode = modelCode;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(Date lastSeen) {
        this.lastSeen = lastSeen;
    }

    public boolean isPhone() {
        return isPhone;
    }

    public void setPhone(boolean phone) {
        isPhone = phone;
    }

    public boolean isHasBiometricDevice() {
        return hasBiometricDevice;
    }

    public void setHasBiometricDevice(boolean hasBiometricDevice) {
        this.hasBiometricDevice = hasBiometricDevice;
    }

    public boolean isHasEnrolledFingerprints() {
        return hasEnrolledFingerprints;
    }

    public void setHasEnrolledFingerprints(boolean hasEnrolledFingerprints) {
        this.hasEnrolledFingerprints = hasEnrolledFingerprints;
    }

    public boolean isCanAuthenticate() {
        return canAuthenticate;
    }

    public void setCanAuthenticate(boolean canAuthenticate) {
        this.canAuthenticate = canAuthenticate;
    }

    public boolean isCanAuthenticateWithBiometrics() {
        return canAuthenticateWithBiometrics;
    }

    public void setCanAuthenticateWithBiometrics(boolean canAuthenticateWithBiometrics) {
        this.canAuthenticateWithBiometrics = canAuthenticateWithBiometrics;
    }

    public boolean isCanAuthenticateWithPIN() {
        return canAuthenticateWithPIN;
    }

    public void setCanAuthenticateWithPIN(boolean canAuthenticateWithPIN) {
        this.canAuthenticateWithPIN = canAuthenticateWithPIN;
    }


    //region GSON helpers


    public String asJson() {
        GsonBuilder builder        = GsonFactory.standardBuilder();
        Gson        jsonSerializer = builder.create();
        String      theJson        = jsonSerializer.toJson(this, this.getClass());
        return theJson;
    }

    public static IBDeviceInfo fromJson(String json) {
        GsonBuilder  builder          = GsonFactory.standardBuilder();
        Gson         jsonDeserializer = builder.create();
        IBDeviceInfo theObject        = jsonDeserializer.fromJson(json, IBDeviceInfo.class);
        return theObject;
    }

    //endregion



     //region Countable
     static {
         setClassCountable(false);
     }

     protected boolean getVerbose() {
         return classCountable;
     }

     private final static String CLASSTAG = kModulePrefix + "." + IBDeviceInfo.class.getSimpleName();
     @GSONexcludeOutbound
     private        String TAG;
     private static int    lifeTimeInstances;
     private static int    numberOfInstances;
     @GSONexcludeOutbound
     private        int    instanceNumber;
     @GSONexcludeOutbound
     private static boolean classCountable;

     @Override
     protected void finalize() throws Throwable {
         super.finalize();
         numberOfInstances--;
         if (numberOfInstances < 0) {
             Log.e(getLogTAG(), "*** You did not call onNew in your ctor(s)");
         }
         if (getVerbose()) {
             Log.d(getLogTAG(), "finalize  ");
         }
     }

     protected void onNew() {
         TAG = CLASSTAG;
         numberOfInstances++;
         lifeTimeInstances++;
         instanceNumber = lifeTimeInstances;
         if (getVerbose()) {
             Log.d(getLogTAG(), "onNew ");
         }
     }

     private String getLogLabel() {
         return Integer.toHexString(instanceNumber) + "/" + Integer.toHexString(numberOfInstances);
     }

     public static  void setClassCountable( boolean isIt) {
         classCountable = isIt;
     }

     protected String getLogTAG() {
         TAG = CLASSTAG + " [" + getLogLabel() + "] ";
         return TAG;
     }

     //endregion
}
