package com.portableehr.patientsdk.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.portableehr.sdk.network.gson.GSONexcludeOutbound;

import static com.portableehr.patientsdk.utils.PatientRuntimeConstants.kDefaultClassCountable;
import static com.portableehr.sdk.EHRLibRuntime.kModulePrefix;


public class ScreenReceiver extends BroadcastReceiver {

    static {
        setClassCountable(false);
    }
    public static boolean wasScreenOn = true;

    public ScreenReceiver() {
        onNew();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null) {
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                Log.d(getLogTAG(), "Received ACTION_SCREEN_OFF intent action.");
                // DO WHATEVER YOU NEED TO DO HERE
                // TODO[Rahul]: check this
//                ApplicationExt.getInstance().setAppInForeground(false);
                wasScreenOn = false;
            } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                Log.d(getLogTAG(), "Received ACTION_SCREEN_ON  intent action.");
                // AND DO WHATEVER YOU NEED TO DO HERE
                // TODO[Rahul]: check this
//                ApplicationExt.getInstance().setAppInForeground(true);
                wasScreenOn = true;
            } else {
                Log.e(getLogTAG(), "Received unregistered intent action " + intent.getAction());
            }
        } else {
            Log.e(getLogTAG(), "Received intent with null action ");
        }
    }


    //region Countable

    private final static String  CLASSTAG       = kModulePrefix + "." + ScreenReceiver.class.getSimpleName();
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

}
