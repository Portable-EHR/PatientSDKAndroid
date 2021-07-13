package com.portableehr.sdk.network.NAO.form;

import androidx.annotation.Nullable;
import android.util.Log;

import com.portableehr.sdk.network.NAO.inbound.IBText;
import com.portableehr.sdk.network.gson.GSONexcludeOutbound;

import java.net.URL;

import static com.portableehr.sdk.EHRLibRuntime.kModulePrefix;

/**
 * Created by : yvesleborg
 * Date       : 2020-05-31
 * <p>
 * Copyright Portable Ehr Inc, 2020
 */
public class Question {
    IBText subject;
    @Nullable
    IBText description;
    @Nullable
    IBText help;
    @Nullable
    URL    infoUrl;
    InputImportanceEnum importance;
    InputGuardEnum      guard;
    ValueKindEnum       kind;
    @Nullable
    String defaultStringValue;

    //region get/set

    public IBText getSubject() {
        return subject;
    }

    public void setSubject(IBText subject) {
        this.subject = subject;
    }

    @Nullable
    public IBText getDescription() {
        return description;
    }

    public void setDescription(@Nullable IBText description) {
        this.description = description;
    }

    @Nullable
    public IBText getHelp() {
        return help;
    }

    public void setHelp(@Nullable IBText help) {
        this.help = help;
    }

    @Nullable
    public URL getInfoUrl() {
        return infoUrl;
    }

    public void setInfoUrl(@Nullable URL infoUrl) {
        this.infoUrl = infoUrl;
    }

    public InputImportanceEnum getImportance() {
        return importance;
    }

    public void setImportance(InputImportanceEnum importance) {
        this.importance = importance;
    }

    public InputGuardEnum getGuard() {
        return guard;
    }

    public void setGuard(InputGuardEnum guard) {
        this.guard = guard;
    }

    public ValueKindEnum getKind() {
        return kind;
    }

    public void setKind(ValueKindEnum kind) {
        this.kind = kind;
    }

    @Nullable
    public String getDefaultStringValue() {
        return defaultStringValue;
    }

    public void setDefaultStringValue(@Nullable String defaultStringValue) {
        this.defaultStringValue = defaultStringValue;
    }


    //endregion


    //region Countable
    static {
        setClassCountable(false);
    }

    protected boolean getVerbose() {
        return classCountable;
    }

    private final static String  CLASSTAG = kModulePrefix + "." + Question.class.getSimpleName();
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
