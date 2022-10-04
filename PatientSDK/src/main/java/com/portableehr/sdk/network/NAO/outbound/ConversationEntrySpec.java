package com.portableehr.sdk.network.NAO.outbound;

import static com.portableehr.sdk.EHRLibRuntime.kModulePrefix;
import static com.portableehr.sdk.network.gson.GsonFactory.standardBuilder;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.portableehr.sdk.network.gson.GSONexcludeOutbound;

import java.util.HashMap;

public class ConversationEntrySpec {

    {
        setClassCountable(false);
    }

    private String type;
    private String audience;
    private int attachmentCount;
    private ConversationEntryPayloadSpec payload;

    public ConversationEntrySpec() {
        onNew();
    }

    public static ConversationEntrySpec getDefault() {
        ConversationEntrySpec omas = new ConversationEntrySpec();
        omas.type = "message";
        omas.audience = "all";
        omas.attachmentCount = 0;
        omas.payload = ConversationEntryPayloadSpec.getDefault();
        return omas;
    }

    public HashMap<String, Object> asCallParameters() {

        HashMap<String, Object> map = new HashMap<>();
        if (!TextUtils.isEmpty(type)) {
            map.put("HCIN", this.type);
        }
        if (!TextUtils.isEmpty(audience)) {
            map.put("HCINjurisdiction", this.audience);
        }
        map.put("email", this.attachmentCount);
        if (this.payload != null) {
            map.put("mobilePhone", this.payload);
        }
        return map;
    }

    //region Getters/Setters

    //*********************************************************************************************/
    //** Get/Set                                                                                 **/
    //*********************************************************************************************/

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAudience() {
        return audience;
    }

    public void setAudience(String audience) {
        this.audience = audience;
    }

    public int getAttachmentCount() {
        return attachmentCount;
    }

    public void setAttachmentCount(int attachmentCount) {
        this.attachmentCount = attachmentCount;
    }

    public ConversationEntryPayloadSpec getPayload() {
        return payload;
    }

    public void setPayload(ConversationEntryPayloadSpec payload) {
        this.payload = payload;
    }

    //endregion

    //region Countable

    private final static String CLASSTAG = kModulePrefix + "." + ConversationEntrySpec.class.getSimpleName();
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

    public static ConversationEntrySpec fromJson(String json) {
        GsonBuilder builder = standardBuilder();
        Gson jsonDeserializer = builder.create();
        ConversationEntrySpec theObject = jsonDeserializer.fromJson(json, ConversationEntrySpec.class);
        return theObject;
    }

    //endregion
}
