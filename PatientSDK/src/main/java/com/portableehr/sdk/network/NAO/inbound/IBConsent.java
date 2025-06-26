package com.portableehr.sdk.network.NAO.inbound;

import static com.portableehr.sdk.EHRLibRuntime.kModulePrefix;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.portableehr.sdk.network.gson.GSONexcludeOutbound;
import com.portableehr.sdk.network.gson.GsonFactory;

import java.util.List;

public class IBConsent {

    private String guid;
    private String alias;
    private IBConsentInfo title;
    private IBConsentInfo description;
    private String consentableElementType;
    private Boolean active;
    private String activeFrom;
    private IBConsentGranted consent;


    public List<IBConsentGranted> getConsents() {
        return consents;
    }

    public void setConsents(List<IBConsentGranted> consents) {
        this.consents = consents;
    }

    private List<IBConsentGranted> consents;
    public IBConsent() {
        onNew();
    }


    public String asJson() {
        GsonBuilder builder = GsonFactory.standardBuilder();
        Gson jsonSerializer = builder.create();
        String theJson = jsonSerializer.toJson(this, this.getClass());
        return theJson;
    }

    public static IBConsent fromJson(String json) {
        GsonBuilder builder = GsonFactory.standardBuilder();
        Gson jsonDeserializer = builder.create();
        IBConsent theObject = jsonDeserializer.fromJson(json, IBConsent.class);
        return theObject;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public IBConsentInfo getTitle() {
        return title;
    }

    public void setTitle(IBConsentInfo title) {
        this.title = title;
    }

    public IBConsentInfo getDescription() {
        return description;
    }

    public void setDescription(IBConsentInfo description) {
        this.description = description;
    }

    public String getConsentableElementType() {
        return consentableElementType;
    }

    public void setConsentableElementType(String consentableElementType) {
        this.consentableElementType = consentableElementType;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getActiveFrom() {
        return activeFrom;
    }

    public void setActiveFrom(String activeFrom) {
        this.activeFrom = activeFrom;
    }

    public IBConsentGranted getConsent() {
        return consent;
    }

    public void setConsent(IBConsentGranted consent) {
        this.consent = consent;
    }

    //endregion


    //region Countable

    private final static String CLASSTAG = kModulePrefix + "." + IBConsent.class.getSimpleName();
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
