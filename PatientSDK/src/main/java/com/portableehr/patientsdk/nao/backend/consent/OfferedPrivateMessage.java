package com.portableehr.patientsdk.nao.backend.consent;

import android.util.Log;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.portableehr.sdk.network.NAO.inbound.IBPrivateMessage;
import com.portableehr.sdk.network.NAO.inbound.IBPrivateMessageInfo;
import com.portableehr.sdk.network.gson.GSONexcludeOutbound;
import com.portableehr.sdk.network.gson.GsonFactory;

import static com.portableehr.sdk.EHRLibRuntime.kModulePrefix;


/**
 * Created by : yvesleborg
 * Date       : 2020-01-22
 * <p>
 * Copyright Portable Ehr Inc, 2020
 */
public class OfferedPrivateMessage {

    @Nullable
    IBPrivateMessage     privateMessage;
    @Nullable
    IBPrivateMessageInfo privateMessageInfo;

    OfferedPrivateMessage() {
        super();
        onNew();
    }

    @Nullable
    public IBPrivateMessage getPrivateMessage() {
        return privateMessage;
    }

    public void setPrivateMessage(@Nullable IBPrivateMessage privateMessage) {
        this.privateMessage = privateMessage;
    }

    @Nullable
    public IBPrivateMessageInfo getPrivateMessageInfo() {
        return privateMessageInfo;
    }

    public void setPrivateMessageInfo(@Nullable IBPrivateMessageInfo privateMessageInfo) {
        this.privateMessageInfo = privateMessageInfo;
    }


    //region GSON helpers


    public String asJson() {
        GsonBuilder builder        = GsonFactory.standardBuilder();
        Gson        jsonSerializer = builder.create();
        String      theJson        = jsonSerializer.toJson(this, this.getClass());
        return theJson;
    }

    public static OfferedPrivateMessage fromJson(String json) {
        GsonBuilder           builder          = GsonFactory.standardBuilder();
        Gson                  jsonDeserializer = builder.create();
        OfferedPrivateMessage theObject        = jsonDeserializer.fromJson(json, OfferedPrivateMessage.class);
        return theObject;
    }

    //endregion

    //region Countable
    static {
        setClassCountable(true);
    }

    private final static String  CLASSTAG = kModulePrefix + "." + OfferedPrivateMessage.class.getSimpleName();
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
