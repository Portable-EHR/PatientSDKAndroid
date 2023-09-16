package com.portableehr.sdk.network.NAO.outbound;

import static com.portableehr.sdk.EHRLibRuntime.kModulePrefix;
import static com.portableehr.sdk.network.gson.GsonFactory.standardBuilder;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.portableehr.sdk.network.NAO.inbound.conversations.EntryAttachment;
import com.portableehr.sdk.network.gson.GSONexcludeOutbound;

import java.util.HashMap;
import java.util.List;

public class ConversationLeaveEntryPayloadSpec extends ConversationEntryPayloadSpec {

    {
        setClassCountable(false);
    }

    private String action;
    private String targetParticipantGuid;

    public ConversationLeaveEntryPayloadSpec() {
        onNew();
    }

    public HashMap<String, Object> asCallParameters() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("action", action);
        map.put("targetParticipantGuid", targetParticipantGuid);
        return map;
    }

    //region Getters/Setters

    //*********************************************************************************************/
    //** Get/Set                                                                                 **/
    //*********************************************************************************************/

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getTargetParticipantGuid() {
        return targetParticipantGuid;
    }

    public void setTargetParticipantGuid(String targetParticipantGuid) {
        this.targetParticipantGuid = targetParticipantGuid;
    }

    //endregion

    //region Countable

    private final static String CLASSTAG = kModulePrefix + "." + ConversationLeaveEntryPayloadSpec.class.getSimpleName();
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

    public static ConversationLeaveEntryPayloadSpec fromJson(String json) {
        GsonBuilder builder = standardBuilder();
        Gson jsonDeserializer = builder.create();
        ConversationLeaveEntryPayloadSpec theObject = jsonDeserializer.fromJson(json, ConversationLeaveEntryPayloadSpec.class);
        return theObject;
    }

    //endregion
}
