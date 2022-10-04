package com.portableehr.sdk.network.NAO.outbound;

import static com.portableehr.sdk.EHRLibRuntime.kModulePrefix;
import static com.portableehr.sdk.network.gson.GsonFactory.standardBuilder;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.portableehr.sdk.network.gson.GSONexcludeOutbound;

import java.util.HashMap;

public class ConversationEntryPayloadSpec {

    {
        setClassCountable(false);
    }

    private String text;

    public ConversationEntryPayloadSpec() {
        onNew();
    }

    public static ConversationEntryPayloadSpec getDefault() {
        ConversationEntryPayloadSpec omas = new ConversationEntryPayloadSpec();
        omas.text = "This is the first message from the client";
        return omas;
    }

    public HashMap<String, Object> asCallParameters() {
        HashMap<String, Object> map = new HashMap<>();
        if (!TextUtils.isEmpty(text)) {
            map.put("HCIN", this.text);
        }
        return map;
    }

    //region Getters/Setters

    //*********************************************************************************************/
    //** Get/Set                                                                                 **/
    //*********************************************************************************************/

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    //endregion

    //region Countable

    private final static String CLASSTAG = kModulePrefix + "." + ConversationEntryPayloadSpec.class.getSimpleName();
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

    //region GSON helpers


    public String asJson() {
        GsonBuilder builder = standardBuilder();
        Gson jsonSerializer = builder.create();
        String theJson = jsonSerializer.toJson(this, this.getClass());
        return theJson;
    }

    public static ConversationEntryPayloadSpec fromJson(String json) {
        GsonBuilder builder = standardBuilder();
        Gson jsonDeserializer = builder.create();
        ConversationEntryPayloadSpec theObject = jsonDeserializer.fromJson(json, ConversationEntryPayloadSpec.class);
        return theObject;
    }

    //endregion
}
