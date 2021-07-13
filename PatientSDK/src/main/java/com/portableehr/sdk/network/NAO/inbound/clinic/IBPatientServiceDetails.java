package com.portableehr.sdk.network.NAO.inbound.clinic;

import android.util.Log;

import com.portableehr.sdk.EHRLibRuntime;
import com.portableehr.sdk.network.NAO.inbound.IBDeviceInfo;
import com.portableehr.sdk.network.NAO.inbound.IBPatientInfo;
import com.portableehr.sdk.network.NAO.inbound.IBUserCapability;
import com.portableehr.sdk.network.NAO.inbound.IBUserService;
import com.portableehr.sdk.network.gson.GSONexcludeOutbound;

import java.util.HashMap;

/**
 * Created by : yvesleborg
 * Date       : 2018-07-15
 * <p>
 * Copyright Portable Ehr Inc, 2019
 */
public class IBPatientServiceDetails {

    IBPatientInfo                     info;
    IBDeviceInfo[]                    devices;
    IBUserService[]                   services;
    IBUserCapability[]                capabilities;
    IBUserService[]                   items;
    IBAppInfo[]                       apps;
    HashMap<String, IBDispensaryInfo> dispensaries;

    public IBPatientInfo getInfo() {
        return info;
    }

    public void setInfo(IBPatientInfo info) {
        this.info = info;
    }

    public IBDeviceInfo[] getDevices() {
        return devices;
    }

    public void setDevices(IBDeviceInfo[] devices) {
        this.devices = devices;
    }

    public IBUserService[] getServices() {
        return services;
    }

    public void setServices(IBUserService[] services) {
        this.services = services;
    }

    public IBUserCapability[] getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(IBUserCapability[] capabilities) {
        this.capabilities = capabilities;
    }

    public IBUserService[] getItems() {
        return items;
    }

    public void setItems(IBUserService[] items) {
        this.items = items;
    }

    public IBAppInfo[] getApps() {
        return apps;
    }

    public void setApps(IBAppInfo[] apps) {
        this.apps = apps;
    }

    public HashMap<String, IBDispensaryInfo> getDispensaries() {
        return dispensaries;
    }

    public void setDispensaries(HashMap<String, IBDispensaryInfo> dispensaries) {
        this.dispensaries = dispensaries;
    }

    public IBPatientServiceDetails() {
        super();
        onNew();
    }

    //region Countable

    private final static String  CLASSTAG       = EHRLibRuntime.kModulePrefix + "." + IBPatientServiceDetails.class.getSimpleName();
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
}
