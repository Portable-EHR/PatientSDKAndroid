package com.portableehr.sdk.network.NAO.inbound;

import android.annotation.SuppressLint;
import androidx.annotation.NonNull;
import android.util.Log;

import com.portableehr.sdk.network.gson.GSONexcludeOutbound;

import static com.portableehr.sdk.EHRLibRuntime.kModulePrefix;

/**
 * Created by : yvesleborg
 * Date       : 2017-04-11
 * <p>
 * Copyright Portable Ehr Inc, 2019
 */

public class IBVersion implements Comparable<IBVersion> {


    private int major;
    private int minor;
    private int build;

    @SuppressWarnings("access")
    public IBVersion() {
        onNew();
        major = 0;
        minor = 0;
        build = 0;
    }

    @SuppressWarnings("unused")
    public IBVersion(int major, int minor, int build) {
        this();
        this.setMajor(major);
        this.setMinor(minor);
        this.setBuild(build);
    }

    public IBVersion(String token) {
        this();
        String[] tokens = token.split("\\.");
        if (tokens.length == 3) {
            this.major = Integer.parseInt(tokens[0]);
            this.minor = Integer.parseInt(tokens[1]);
            this.build = Integer.parseInt(tokens[2]);
        } else if (tokens.length == 2) {
            this.major = Integer.parseInt(tokens[0]);
            this.minor = Integer.parseInt(tokens[1]);
            this.build = 0;
        } else if (tokens.length == 1) {
            this.major = Integer.parseInt(tokens[0]);
            this.minor = 0;
            this.build = 0;
        }
    }

    public int getMajor() {
        return major;
    }

    public void setMajor(int major) {
        this.major = major;
    }

    public int getMinor() {
        return minor;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }

    public int getBuild() {
        return build;
    }

    public void setBuild(int build) {
        this.build = build;
    }

    @NonNull
    @SuppressLint("DefaultLocale")
    public String toString() {
        return String.format("%d.%d.%03d", this.getMajor(), this.getMinor(), this.getBuild());
    }

    public int toInt() {
        return 1000 * 1000 * major + 1000 * minor + build;
    }

    @Override
    public int compareTo(@NonNull IBVersion o) {
        return this.toInt() - o.toInt();
    }



    //region Countable

    private final static String  CLASSTAG       = kModulePrefix + "." + IBVersion.class.getSimpleName();
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

    public static  void setClassCountable( boolean isIt) {
        classCountable = isIt;
    }

    private String getLogTAG() {
        TAG = CLASSTAG + " [" + getLogLabel() + "] ";
        return TAG;
    }

    //endregion

}
