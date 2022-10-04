package com.portableehr.sdk.network.NAO.inbound.conversations;

import static com.portableehr.sdk.EHRLibRuntime.kModulePrefix;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.portableehr.sdk.network.gson.GSONexcludeOutbound;
import com.portableehr.sdk.network.gson.GsonFactory;

import java.util.List;

public class ConversationEntry {
    private String id;
    private String from;
    private String type;
    private String audience;
    private int attachmentCount;
    private List<ConversationEntryStatus> status;
    private ConversationEntryPayload payload;
    private String createdOn;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

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

    public List<ConversationEntryStatus> getStatus() {
        return status;
    }

    public void setStatus(List<ConversationEntryStatus> status) {
        this.status = status;
    }

    public ConversationEntryPayload getPayload() {
        return payload;
    }

    public void setPayload(ConversationEntryPayload payload) {
        this.payload = payload;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public ConversationEntry() {
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

    public static ConversationEntry fromJson(String json) {
        GsonBuilder builder = GsonFactory.standardBuilder();
        Gson jsonDeserializer = builder.create();
        ConversationEntry theObject = jsonDeserializer.fromJson(json, ConversationEntry.class);
        return theObject;
    }

    //endregion

    //region Countable

    private final static String CLASSTAG = kModulePrefix + "." + ConversationEntry.class.getSimpleName();
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
