package com.portableehr.sdk.network.NAO.outbound;

import static com.portableehr.sdk.EHRLibRuntime.kModulePrefix;
import static com.portableehr.sdk.network.gson.GsonFactory.standardBuilder;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.portableehr.sdk.network.gson.GSONexcludeOutbound;

import java.util.HashMap;

/**
 * Created by : yvesleborg
 * Date       : 2017-12-31
 * <p>
 * Copyright Portable Ehr Inc, 2019
 */

public class OBManualActivationSpec {

    {
        setClassCountable(false);
    }

    private String HCINjurisdiction;
    private String HCIN;
    private String email;
    private String mobile;
    //    private String dispensaryGuid;
    private String patientFileNumber;

    public OBManualActivationSpec() {
        onNew();
    }

    public static OBManualActivationSpec getDefault() {
        OBManualActivationSpec omas = new OBManualActivationSpec();
        omas.HCINjurisdiction = "RAMQ";
        omas.HCIN = "LEBY55041510";
//        omas.dispensaryGuid = "43d081ad-5459-46e7-a49f-976f72b8b0bb";
        omas.email = "yves@leborgne.org";
        omas.mobile = "yves@leborgne.org";
        omas.patientFileNumber = "A47";
        return omas;
    }

    public HashMap<String, Object> asCallParameters() {

        HashMap<String, Object> map = new HashMap<>();
        if (this.HCIN != null) {
            map.put("HCIN", this.HCIN);
        }
        if (this.HCINjurisdiction != null) {
            map.put("HCINjurisdiction", this.HCINjurisdiction);
        }
//        if (this.dispensaryGuid != null) {
//            map.put("dispensaryGuid", this.dispensaryGuid);
//        }
        if (this.email != null) {
            map.put("email", this.email);
        }
        if (this.mobile != null) {
            map.put("mobilePhone", this.mobile);
        }
        if (this.patientFileNumber != null) {
            map.put("patientFileNumber", this.patientFileNumber);
        }
        return map;
    }

    //region Getters/Setters

    //*********************************************************************************************/
    //** Get/Set                                                                                 **/
    //*********************************************************************************************/

    public String getHCINjurisdiction() {
        return HCINjurisdiction;
    }

    public void setHCINjurisdiction(String HCINjurisdiction) {
        this.HCINjurisdiction = HCINjurisdiction;
    }

    public String getHCIN() {
        return HCIN;
    }

    public void setHCIN(String HCIN) {
        this.HCIN = HCIN;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

//    public String getDispensaryGuid() {
//        return dispensaryGuid;
//    }
//
//    public void setDispensaryGuid(String dispensaryGuid) {
//        this.dispensaryGuid = dispensaryGuid;
//    }

    public String getPatientFileNumber() {
        return patientFileNumber;
    }

    public void setPatientFileNumber(String patientFileNumber) {
        this.patientFileNumber = patientFileNumber;
    }
    //endregion

    //region Countable

    private final static String CLASSTAG = kModulePrefix + "." + OBManualActivationSpec.class.getSimpleName();
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

    public static OBManualActivationSpec fromJson(String json) {
        GsonBuilder builder = standardBuilder();
        Gson jsonDeserializer = builder.create();
        OBManualActivationSpec theObject = jsonDeserializer.fromJson(json, OBManualActivationSpec.class);
        return theObject;
    }

    //endregion
}
