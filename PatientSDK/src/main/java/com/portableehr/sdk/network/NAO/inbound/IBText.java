package com.portableehr.sdk.network.NAO.inbound;

import android.util.Log;

import com.portableehr.sdk.EHRLibRuntime;
import com.portableehr.sdk.network.gson.GSONexcludeOutbound;

/**
 * Created by : yvesleborg
 * Date       : 2020-05-31
 * <p>
 * Copyright Portable Ehr Inc, 2020
 */
public class IBText {
    TextRendererEnum renderer;
    String           text;


    public IBText() {
        onNew();
    }

    public TextRendererEnum getRenderer() {
        return renderer;
    }

    public void setRenderer(TextRendererEnum renderer) {
        this.renderer = renderer;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    //region Countable
    static {
        setClassCountable(false);
    }

    protected boolean getVerbose() {
        return classCountable;
    }

    private final static String  CLASSTAG = EHRLibRuntime.kModulePrefix + "." + IBText.class.getSimpleName();
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

    public static void setClassCountable(boolean isIt) {
        classCountable = isIt;
    }

    protected String getLogTAG() {
        TAG = CLASSTAG + " [" + getLogLabel() + "] ";
        return TAG;
    }

    //endregion

}
