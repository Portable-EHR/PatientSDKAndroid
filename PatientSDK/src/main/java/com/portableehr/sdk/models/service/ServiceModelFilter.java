package com.portableehr.sdk.models.service;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.portableehr.sdk.network.NAO.inbound.IBService;
import com.portableehr.sdk.network.gson.GSONexcludeOutbound;
import com.portableehr.sdk.network.gson.GsonFactory;

import java.util.ArrayList;
import java.util.Collections;

import static com.portableehr.sdk.EHRLibRuntime.kModulePrefix;


/**
 * Created by : yvesleborg
 * Date       : 2018-01-24
 * <p>
 * Copyright Portable Ehr Inc, 2018
 */

public class ServiceModelFilter {


    static int EMPTY_CURSOR = -1;

    @SuppressWarnings("FieldCanBeLocal")
    private ServiceModelFilterTypeEnum serviceModelType;
    private ArrayList<String>          sortedKeys;
    //    private ArrayList<String>          patientSelector;
    private boolean                    showAllServices;
    private boolean                    showSubscribedServices;
    private boolean                    showAvailableServices;
    private boolean                    showRequiredEulaServices;
    private int                        cursorIndex;


    //region Constructors

    public ServiceModelFilter() {
        setClassCountable(false);
        onNew();
        this.sortedKeys = new ArrayList<>();
        this.cursorIndex = EMPTY_CURSOR;
    }

    public ServiceModelFilter(ServiceModelFilterTypeEnum type) {

        this();
        this.serviceModelType = type;
        switch (this.serviceModelType) {
            case ALL:
                this.showAllServices = true;
                break;
            case AVAILABLE:
                this.showAvailableServices = true;
                break;
            case SUBSCRIBED:
                this.showSubscribedServices = true;
                break;
            case REQUIRED_EULA:
                this.showRequiredEulaServices = true;
            default:
        }
    }

    public static ServiceModelFilter subscribedServicesFilter() {
        ServiceModelFilter filter = new ServiceModelFilter(ServiceModelFilterTypeEnum.SUBSCRIBED);
        return filter;
    }

    public static ServiceModelFilter availableServicesFilter() {
        ServiceModelFilter filter = new ServiceModelFilter(ServiceModelFilterTypeEnum.AVAILABLE);
        return filter;
    }

    public static ServiceModelFilter allServicesFilter() {
        ServiceModelFilter filter = new ServiceModelFilter(ServiceModelFilterTypeEnum.ALL);
        return filter;
    }

    public static ServiceModelFilter requiredEulaServicesFilter() {
        ServiceModelFilter filter = new ServiceModelFilter(ServiceModelFilterTypeEnum.REQUIRED_EULA);
        return filter;
    }

    //endregion

    //region useful getters in support of adapters

    public int getCount() {
        return this.getLength();
    }

    public ArrayList<String> getSortedKeys() {
        return sortedKeys;
    }

    public String[] getSortedKeysArray() {
        String[] ar = sortedKeys.toArray(new String[0]);
        return ar;
    }
    //endregion


    //region Cursor control, content

    public boolean isAtTop() {
        if (getLength() == 0) {
            return true;
        }
        return cursorIndex == 0;
    }

    public boolean isAtBottom() {
        if (getLength() == 0) {
            return true;
        }
        return cursorIndex == getLength() - 1;
    }

    public void setCursorAtTop() {
        if (getLength() == 0) {
            cursorIndex = EMPTY_CURSOR;
        }
        cursorIndex = 0;
    }

    public void setCursorAtBottom() {
        if (getLength() == 0) {
            cursorIndex = EMPTY_CURSOR;
        }
        cursorIndex = getLength() - 1;
    }

    public void setCursorAtService(IBService service) {
        if (isEmpty()) {
            return;
        }

        cursorIndex = EMPTY_CURSOR;
        for (int i = 0; i < sortedKeys.size(); i++) {
            String key = sortedKeys.get(i);
            if (key.contentEquals(service.getCreationOrder())) {
                // we have arrived !
                cursorIndex = i;
                break;
            }
        }

        if (cursorIndex == EMPTY_CURSOR) {
            // the old cursor was removed by this refrewh, move
            // cursor to top.
            setCursorAtTop();
        }

    }


    public void moveToPrevious() {

        if (isAtTop()) {
            return;
        }

        if (cursorIndex == 0) {
            return;
        }

        cursorIndex = cursorIndex - 1;
    }

    public void moveToNext() {
        if (isAtBottom()) {
            return;
        }
        if (getLength() == 0) {
            return;
        }

        if (getLength() == 1) {
            cursorIndex = 0;
            return;
        }
        cursorIndex = cursorIndex + 1;
    }

    public boolean isEmpty() {
        return cursorIndex == EMPTY_CURSOR;
    }

    public int getLength() {
        if (cursorIndex == EMPTY_CURSOR) {
            return 0;
        }
        if (sortedKeys.size() == 0) {
            Log.e(getLogTAG(), "*** Internal consistency : cursorIndex  not empty but has  noelements.");
        }
        return sortedKeys.size();
    }

    public IBService getNext() {
        if (isAtBottom()) {
            return null;
        }
        if (cursorIndex == getLength() - 1) {
            return null;
        }
        String nextKey = sortedKeys.get(cursorIndex + 1);
        return getServiceWithKey(nextKey);

    }

    public IBService getPrevious() {
        if (isAtTop()) {
            return null;
        }
        if (cursorIndex > 1) {
            String prevKey = sortedKeys.get(cursorIndex - 1);
            return getServiceWithKey(prevKey);
        }
        return null;
    }

    public IBService getCursor() {
        if (isEmpty()) {
            return null;
        }
        if (cursorIndex > sortedKeys.size() - 1) {
            Log.e(getLogTAG(), "Internal consistency : cursorIndex greater than array size allows []");
            return null;
        }
        String    key     = sortedKeys.get(cursorIndex);
        IBService service = ServiceModel.getInstance().getAllServices().get(key);
        return service;
    }

    public int getNumberOfUnseen() {
        int unseen = 0;
        for (String key : sortedKeys) {
            IBService service = getServiceWithKey(key);
            if (service != null) {
                if (!service.getWasSeen()) {
                    unseen++;
                }
            }
        }
        return unseen;
    }
    //endregion

    public void refreshFilter() {

        sortedKeys = new ArrayList<>();
        cursorIndex = EMPTY_CURSOR;
        ServiceModel serviceModel = ServiceModel.getInstance();

        if (serviceModel.getAllServices().size() > 0) {

            IBService currentCursor = getCursor();
            for (IBService service : serviceModel.getAllServices().values()) {
                if (shouldKeep(service)) {
                    sortedKeys.add(service.getCreationOrder());
                }
            }

            if (sortedKeys.size() > 0) {
                Collections.sort(sortedKeys);
                if (null != currentCursor) {
                    if (sortedKeys.contains(currentCursor.getCreationOrder())) {
                        setCursorAtService(currentCursor);
                    } else {
                        setCursorAtTop();
                    }

                }
            }
        }
    }

    public void resetFilter() {
    }

    private boolean shouldKeep(@SuppressWarnings("unused") IBService service) {

        if (showAllServices) {
            return true;
        }
        if (showSubscribedServices) {
            // todo : this depends on a UserServiceModel (not implemented yet)
            return true;
        }
        if (showAvailableServices) {
            // todo : this depends on a UserServiceModel (not implemented yet)
            // this is the mirror image of subscribed services..
            return false;
        }

        if (showRequiredEulaServices) {
            // !!! how to determine if we have seen this EULA
            // todo : this depends on implementing EULA model
            return false; // assume all EULAs are read.
        }
        return false;

    }

    private IBService getServiceWithKey(String key) {
        if (null == key) {
            return null;
        }
        ServiceModel serviceModel = ServiceModel.getInstance();
        IBService    service      = serviceModel.getAllServices().get(key);
        if (service != null) {
            return service;
        }
        Log.e(getLogTAG(), "*** There are no services with key[" + key + "]");
        return null;
    }


    //region Countable

    private final static String  CLASSTAG       = kModulePrefix + "." + ServiceModelFilter.class.getSimpleName();
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

    private String getLogTAG() {
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

    public static ServiceModelFilter fromJson(String json) {
        GsonBuilder        builder          = GsonFactory.standardBuilder();
        Gson               jsonDeserializer = builder.create();
        ServiceModelFilter theObject        = jsonDeserializer.fromJson(json, ServiceModelFilter.class);
        return theObject;
    }

    //endregion

}
