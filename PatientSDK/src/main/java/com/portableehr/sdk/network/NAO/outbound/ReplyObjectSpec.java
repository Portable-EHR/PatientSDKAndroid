package com.portableehr.sdk.network.NAO.outbound;

import static com.portableehr.sdk.EHRLibRuntime.kModulePrefix;
import static com.portableehr.sdk.network.gson.GsonFactory.standardBuilder;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.portableehr.sdk.network.gson.GSONexcludeOutbound;

import java.util.HashMap;

public class ReplyObjectSpec {

    {
        setClassCountable(false);
    }

    private String id;

    public ReplyObjectSpec() {
        onNew();
    }

    public static ReplyObjectSpec getDefault() {
        ReplyObjectSpec omas = new ReplyObjectSpec();
        omas.id = "";
        return omas;
    }

    public HashMap<String, Object> asCallParameters() {
        HashMap<String, Object> map = new HashMap<>();
        if (!TextUtils.isEmpty(id)) {
            map.put("id", this.id);
        }
        return map;
    }

    //region Getters/Setters

    //*********************************************************************************************/
    //** Get/Set                                                                                 **/
    //*********************************************************************************************/

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
//endregion

    //region Countable

    private final static String CLASSTAG = kModulePrefix + "." + ReplyObjectSpec.class.getSimpleName();
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

    public static ReplyObjectSpec fromJson(String json) {
        GsonBuilder builder = standardBuilder();
        Gson jsonDeserializer = builder.create();
        ReplyObjectSpec theObject = jsonDeserializer.fromJson(json, ReplyObjectSpec.class);
        return theObject;
    }

    //endregion
}
