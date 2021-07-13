package com.portableehr.sdk.network.NAO.form;

import androidx.annotation.Nullable;
import android.util.Log;

import com.portableehr.sdk.network.NAO.inbound.IBText;
import com.portableehr.sdk.network.gson.GSONexcludeOutbound;

import java.net.URL;
import java.util.HashMap;

import static com.portableehr.sdk.EHRLibRuntime.kModulePrefix;

/**
 * Created by : yvesleborg
 * Date       : 2020-05-31
 * <p>
 * Copyright Portable Ehr Inc, 2020
 */
public class Section {
    String                    alias;
    HashMap<String, Question> questions;
    @Nullable
    URL    infoUrl;
    @Nullable
    IBText description;
    IBText subject;

//region get/set

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public HashMap<String, Question> getQuestions() {
        return questions;
    }

    public void setQuestions(HashMap<String, Question> questions) {
        this.questions = questions;
    }

    @Nullable
    public URL getInfoUrl() {
        return infoUrl;
    }

    public void setInfoUrl(@Nullable URL infoUrl) {
        this.infoUrl = infoUrl;
    }

    @Nullable
    public IBText getDescription() {
        return description;
    }

    public void setDescription(@Nullable IBText description) {
        this.description = description;
    }

    public IBText getSubject() {
        return subject;
    }

    public void setSubject(IBText subject) {
        this.subject = subject;
    }


    //endregion


    //region Countable
    static {
        setClassCountable(false);
    }

    protected boolean getVerbose() {
        return classCountable;
    }

    private final static String  CLASSTAG = kModulePrefix + "." + Section.class.getSimpleName();
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
