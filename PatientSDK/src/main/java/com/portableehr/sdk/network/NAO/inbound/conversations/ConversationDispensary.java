package com.portableehr.sdk.network.NAO.inbound.conversations;

import static com.portableehr.sdk.EHRLibRuntime.kModulePrefix;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.portableehr.sdk.network.gson.GSONexcludeOutbound;
import com.portableehr.sdk.network.gson.GsonFactory;

import java.util.List;

public class ConversationDispensary {
    private String dispensaryId;
    private String name;
    private List<ConversationEntryPoint> entryPoints;

    public String getDispensaryId() {
        return dispensaryId;
    }

    public void setDispensaryId(String dispensaryId) {
        this.dispensaryId = dispensaryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ConversationEntryPoint> getEntryPoints() {
        return entryPoints;
    }

    public void setEntryPoints(List<ConversationEntryPoint> entryPoints) {
        this.entryPoints = entryPoints;
    }

    public ConversationDispensary() {
        super();
        onNew();
    }

    //region GSON helpers

    public String asJson() {
        GsonBuilder builder = GsonFactory.standardBuilder();
        Gson jsonSerializer = builder.create();
        String theJson = jsonSerializer.toJson(this, this.getClass());
        return theJson;
    }

    public static ConversationDispensary fromJson(String json) {
        GsonBuilder builder = GsonFactory.standardBuilder();
        Gson jsonDeserializer = builder.create();
        ConversationDispensary theObject = jsonDeserializer.fromJson(json, ConversationDispensary.class);
        return theObject;
    }

    //endregion

    //region Countable

    private final static String CLASSTAG = kModulePrefix + "." + ConversationDispensary.class.getSimpleName();
    @GSONexcludeOutbound
    private String TAG;
    private static int lifeTimeInstances;
    private static int numberOfInstances;
    @GSONexcludeOutbound
    private int instanceNumber;
    @GSONexcludeOutbound
    private static boolean classCountable = false;

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
