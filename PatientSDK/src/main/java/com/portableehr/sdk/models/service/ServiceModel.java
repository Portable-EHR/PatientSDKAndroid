package com.portableehr.sdk.models.service;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.portableehr.sdk.models.AbstractPollingModel;
import com.portableehr.sdk.models.ModelRefreshPolicyEnum;
import com.portableehr.sdk.network.NAO.inbound.IBService;
import com.portableehr.sdk.network.gson.GSONexcludeOutbound;
import com.portableehr.sdk.network.gson.GsonFactory;
import com.portableehr.sdk.util.FileUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static com.portableehr.sdk.EHRLibRuntime.kModulePrefix;


/**
 * Created by : yvesleborg
 * Date       : 2018-01-24
 * <p>
 * Copyright Portable Ehr Inc, 2018
 */

@SuppressWarnings("unused")
public class ServiceModel extends AbstractPollingModel {

    {
        setClassCountable(false);
    }

    private static final long mMinimalPollingInterval = 15;
    private static final long mMaximumPollingInterval = 900;

    private Date                       lastRefreshed;
    private HashMap<String, IBService> allServices;
    private ServiceModelFilter         allServicesFilter;
    private ServiceModelFilter         subscribedServicesFilter;
    private ServiceModelFilter         availableServicesFilter;


    private static ServiceModel ourInstance = null;

    public static ServiceModel getInstance() {
        if (null == ourInstance) {
            if (existsOnDevice()) {
                ourInstance = readFromDevice();
                ourInstance.refreshFromServer();
            } else {
                ourInstance = initOnDevice();
            }
            if (ourInstance == null) {
                throw new RuntimeException("Unable to create a serviceModel instance");
            }


        }
        return ourInstance;
    }

    private ServiceModel() {
        setClassCountable(false);
        onNew();
        setPollingPolicy(ModelRefreshPolicyEnum.NONE);
        allServices = new HashMap<>();
        allServicesFilter = new ServiceModelFilter(ServiceModelFilterTypeEnum.ALL);
        availableServicesFilter = new ServiceModelFilter(ServiceModelFilterTypeEnum.AVAILABLE);
        subscribedServicesFilter = new ServiceModelFilter(ServiceModelFilterTypeEnum.SUBSCRIBED);
        lastRefreshed = null;
    }

    //region set/get


    public Date getLastRefreshed() {
        return lastRefreshed;
    }

    public void setLastRefreshed(Date lastRefreshed) {
        this.lastRefreshed = lastRefreshed;
    }

    public void setAllServices(HashMap<String, IBService> allServices) {
        this.allServices = allServices;
    }

    public ServiceModelFilter getAllServicesFilter() {
        return allServicesFilter;
    }

    public void setAllServicesFilter(ServiceModelFilter allServicesFilter) {
        this.allServicesFilter = allServicesFilter;
    }

    public ServiceModelFilter getSubscribedServicesFilter() {
        return subscribedServicesFilter;
    }

    public void setSubscribedServicesFilter(ServiceModelFilter filter) {
        this.subscribedServicesFilter = filter;
    }

    public ServiceModelFilter getAvailableServicesFilter() {
        return availableServicesFilter;
    }

    public void setAvailableServicesFilter(ServiceModelFilter availableServicesFilter) {
        this.availableServicesFilter = availableServicesFilter;
    }

    //endregion

    //region Abstract service model implementatin


    @Override
    public long getSmallestPollIntervalInSeconds() {
        return mMinimalPollingInterval;
    }

    @Override
    public long getLargestPollingIntervalInSeconds() {
        return mMaximumPollingInterval;
    }

    @Override
    protected void implementPollAction() {
        // Log.v(TAG, "implementPollAction() called");
        signalPollActionComplete();
    }

    @Override
    protected void implementPollActionCancel() {
        // Log.v(TAG, "implementPollActionCancel() called");
        signalPollActionCancelComplete();
    }

    @Override
    protected void onPollActionStart() {
        // Log.v(TAG, "onPollActionStart() called");
    }

    @Override
    protected void onPollActionCancelStart() {
        // Log.v(TAG, "onPollActionCancelStart() called");
    }

    //endregion

    //region Persistence
    public static boolean saveOnDevice() {
        return FileUtils.saveServiceModelOnDevice(ourInstance);
    }

    public static void resetOnDevice() {
        FileUtils.deleteServiceModelFromDevice();
        ourInstance.allServices = new HashMap<>();
        ourInstance.resetFilters();
        FileUtils.saveServiceModelOnDevice(ourInstance);
    }


    static ServiceModel initOnDevice() {
        ServiceModel serviceModel = new ServiceModel();
        FileUtils.saveServiceModelOnDevice(serviceModel);
        return serviceModel;
    }

    public static ServiceModel readFromDevice() {
        String json = FileUtils.reasServiceModelJson();
        if (null == json) {
            return new ServiceModel();
        }
        return fromJson(json);
    }

    public static boolean existsOnDevice() {
        return FileUtils.serviceModelExistsOnDevice();
    }
    //endregion

    //region Server acquisition

    void refreshFromServer() {

        // this is a single instance, that feeds from the services info
        // in AppInfo

    }

    void refreshFilters() {
        this.getAllServicesFilter().refreshFilter();
        this.getAvailableServicesFilter().refreshFilter();
        this.getSubscribedServicesFilter().refreshFilter();
    }

    public void resetFilters() {
    }

    public void populateWithServcies(HashMap<String, IBService> services) {

        // first remove those that have disappeared from serviceArray
        // ths incoming hashmap is structured on guid, our internal structure is on
        // service creation order

        ArrayList<String> toRemove = new ArrayList<>();
        for (IBService service : this.allServices.values()) {
            if (null == services.get(service.getGuid())) {
                toRemove.add(service.getCreationOrder());
            }
        }

        // now remove the stuff from our userCredentials list

        for (String key : toRemove) {
            allServices.remove(key);
        }

        // ok, so we dont have any extraneous stuff , now examine the incoming
        // stuff to see if we need to update

        for (IBService service : services.values()) {
            if (!this.allServices.containsKey(service.getCreationOrder())) {
                this.allServices.put(service.getCreationOrder(), service);
                service.setWasSeen(false);
            } else {
                IBService oldVersion = this.allServices.get(service.getCreationOrder());

                if (null != oldVersion) {
                    if (!oldVersion.getVersion().toString().contentEquals(service.getVersion().toString())) {
                        this.allServices.remove(oldVersion.getCreationOrder());
                        this.allServices.put(service.getCreationOrder(), service);
                        continue;
                    }

                    if (!oldVersion.getLastUpdated().equals(service.getLastUpdated())) {
                        this.allServices.remove(oldVersion.getCreationOrder());
                        this.allServices.put(service.getCreationOrder(), service);
                    }
                }
            }
        }


        this.refreshFilters();
        if (!saveOnDevice()) {
            Log.d(getLogTAG(), "Some occured when saving service model on device.");
        }

    }
    //endregion


    public IBService serviceWithGuid(String guid) {
        IBService service = null;
        if (guid != null) {
            service = allServices.get(guid);
        }
        return service;
    }

    public int numberOfUnseen() {
        return 0;
    }

    public HashMap<String, IBService> getAllServices() {
        return this.allServices;
    }


    //region Countable

    private final static String  CLASSTAG       = kModulePrefix + "." + ServiceModel.class.getSimpleName();
    @GSONexcludeOutbound
    private              String  TAG;
    private static       int     lifeTimeInstances;
    private static       int     numberOfInstances;
    @GSONexcludeOutbound
    private              int     instanceNumber;
    @GSONexcludeOutbound
    private static       boolean classCountable = false;

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

    protected String getLogTAG() {
        TAG = CLASSTAG + " [" + getLogLabel() + "] ";
        return TAG;
    }

    //endregion

    //region GSON helpers


    public String asJson() {
        GsonBuilder builder        = GsonFactory.standardBuilder();
        Gson        jsonSerializer = builder.create();
        String      theJson        = jsonSerializer.toJson(this, this.getClass());
        return theJson;
    }

    public static ServiceModel fromJson(String json) {
        GsonBuilder  builder          = GsonFactory.standardBuilder();
        Gson         jsonDeserializer = builder.create();
        ServiceModel theObject        = jsonDeserializer.fromJson(json, ServiceModel.class);
        return theObject;
    }

    //endregion


}
