package com.portableehr.sdk.network.NAO.inbound.clinic;

import android.util.Log;

import com.portableehr.sdk.EHRLibRuntime;
import com.portableehr.sdk.network.NAO.inbound.IBUserApiCredentials;
import com.portableehr.sdk.network.gson.GSONexcludeOutbound;

/**
 * Created by : yvesleborg
 * Date       : 2018-06-23
 * <p>
 * Copyright Portable Ehr Inc, 2019
 */
public class IBActivateDevice {
    String               deviceGuid;
    IBUserApiCredentials admin;

    public IBActivateDevice() {
        onNew();
    }

    public String getDeviceGuid() {
        return deviceGuid;
    }

    public void setDeviceGuid(String deviceGuid) {
        this.deviceGuid = deviceGuid;
    }

    public IBUserApiCredentials getAdmin() {
        return admin;
    }

    public void setAdmin(IBUserApiCredentials admin) {
        this.admin = admin;
    }


     //region Countable

    private final static String  CLASSTAG       = EHRLibRuntime.kModulePrefix + "." + IBActivateDevice.class.getSimpleName();
    @GSONexcludeOutbound
     private             String  TAG;
    private static       int     lifeTimeInstances;
    private static       int     numberOfInstances;
    @GSONexcludeOutbound
     private             int     instanceNumber;
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

     public static  void setClassCountable( boolean isIt) {
         classCountable = isIt;
     }

     private String getLogTAG() {
         TAG = CLASSTAG + " [" + getLogLabel() + "] ";
         return TAG;
     }

     //endregion
}
