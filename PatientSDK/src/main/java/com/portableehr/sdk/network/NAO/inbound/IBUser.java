package com.portableehr.sdk.network.NAO.inbound;

import static com.portableehr.sdk.EHRLibRuntime.kModulePrefix;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.portableehr.sdk.EHRLibRuntime;
import com.portableehr.sdk.network.NAO.inbound.user.IBDsuPrefs;
import com.portableehr.sdk.network.gson.GSONexcludeInbound;
import com.portableehr.sdk.network.gson.GSONexcludeOutbound;
import com.portableehr.sdk.network.gson.GsonFactory;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by : yvesleborg
 * Date       : 2017-03-31
 * <p>
 * Copyright Portable Ehr Inc, 2019
 */

public class IBUser implements Serializable {
//    private static final long serialVersionUID = 1L;

    private String guid;
    private String apiKey;
    private String status;
    private String role;
    private String dispensaryRole;
    private boolean emailVerified;
    private boolean mobileVerified;
    private boolean identityVerified;
    private IBContact contact;
    private Date lastUpdated;
    private Date createdOn;
    private IBPatient patient;
    private boolean forcePasswordChange;
    private boolean deviceEmailVerified;
    private boolean deviceMobileVerified;
    private int userUnreadNotificationCount;

    public int getUserUnreadNotificationCount() {
        return userUnreadNotificationCount;
    }

    public void setUserUnreadNotificationCount(int userUnreadNotificationCount) {
        this.userUnreadNotificationCount = userUnreadNotificationCount;
    }

    public List<IBPatient> getDependants() {
        return dependants;
    }

    public void setDependants(List<IBPatient> dependants) {
        this.dependants = dependants;
    }

    private List<IBPatient> dependants;

    public IBDsuPrefs getDsuPrefs() {
        return dsuPrefs;
    }

    public void setDsuPrefs(IBDsuPrefs dsuPrefs) {
        this.dsuPrefs = dsuPrefs;
    }

    private IBDsuPrefs dsuPrefs;

    @GSONexcludeInbound
    private HashMap<String, IBPatient> visits;

    @GSONexcludeInbound
    private HashMap<String, IBPatient> proxies;

    @GSONexcludeInbound
    private HashMap<String, IBPractitioner> practitioners;


    @GSONexcludeInbound
    @GSONexcludeOutbound
    private boolean isPractitioner;


    public IBUser() {
        onNew();
        this.proxies = new HashMap<>();
        this.visits = new HashMap<>();
        this.practitioners = new HashMap<>();
        this.isPractitioner = false;
    }

    public static IBUser guest() {
        IBUser g = new IBUser();
        g.apiKey = EHRLibRuntime.kGuestApiKey;
        g.guid = EHRLibRuntime.kGuestUserGuid;
        g.status = "active";
        g.role = "guest";
        g.emailVerified = true;
        g.createdOn = new Date();
        g.lastUpdated = new Date();
        g.contact = new IBContact();
        g.contact.setName("Visitor");
        g.contact.setFirstName("Distinguished");
        g.isPractitioner = false;
        g.forcePasswordChange = false;
        g.deviceEmailVerified = true;
        g.deviceMobileVerified = true;
        return g;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDispensaryRole() {
        return dispensaryRole;
    }

    public void setDispensaryRole(String dispensaryRole) {
        this.dispensaryRole = dispensaryRole;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public boolean isMobileVerified() {
        return mobileVerified;
    }

    public void setMobileVerified(boolean mobileVerified) {
        this.mobileVerified = mobileVerified;
    }

    public boolean isIdentityVerified() {
        return identityVerified;
    }

    public void setIdentityVerified(boolean identityVerified) {
        this.identityVerified = identityVerified;
    }

    public IBContact getContact() {
        return contact;
    }

    public void setContact(IBContact contact) {
        this.contact = contact;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public IBPatient getPatient() {
        return patient;
    }

    public void setPatient(IBPatient patient) {
        this.patient = patient;
    }

    public HashMap<String, IBPatient> getVisits() {
        return visits;
    }

    public void setVisits(HashMap<String, IBPatient> visits) {
        this.visits = visits;
    }

    public HashMap<String, IBPatient> getProxies() {
        return proxies;
    }

    public void setProxies(HashMap<String, IBPatient> proxies) {
        this.proxies = proxies;
    }

    public HashMap<String, IBPractitioner> getPractitioners() {
        return practitioners;
    }

    public void setPractitioners(HashMap<String, IBPractitioner> practitioners) {
        this.practitioners = practitioners;
    }

    public boolean isPractitioner() {
        return isPractitioner;
    }

    public boolean isGuest() {
        return this.role.equals("guest");
    }

    public void setPractitioner(boolean practitioner) {
        isPractitioner = practitioner;
    }

    public boolean isForcePasswordChange() {
        return this.forcePasswordChange;
    }

    public void setForcePasswordChange(boolean pwdChange) {
        forcePasswordChange = pwdChange;
    }

    public boolean isDeviceEmailVerified() {
        return this.deviceEmailVerified;
    }

    public void setDeviceEmailVerified(boolean verified) {
        deviceEmailVerified = verified;
    }

    public boolean isDeviceMobileVerified() {
        return this.deviceMobileVerified;
    }

    public void setDeviceMobileVerified(boolean verified) {
        deviceMobileVerified = verified;
    }


    //region Countable

    private final static String CLASSTAG = kModulePrefix + "." + IBUser.class.getSimpleName();
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
        GsonBuilder builder = GsonFactory.standardBuilder();
        Gson jsonSerializer = builder.create();
        String theJson = jsonSerializer.toJson(this, this.getClass());
        return theJson;
    }

    public static IBUser fromJson(String json) {
        GsonBuilder builder = GsonFactory.standardBuilder();
        Gson jsonDeserializer = builder.create();
        IBUser theObject = jsonDeserializer.fromJson(json, IBUser.class);
        return theObject;
    }

    //endregion
}
