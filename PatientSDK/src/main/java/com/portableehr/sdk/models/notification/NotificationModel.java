package com.portableehr.sdk.models.notification;

import android.content.Intent;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.portableehr.sdk.RestCallOptions;
import com.portableehr.sdk.EHRLibRuntime;
import com.portableehr.sdk.models.AbstractPollingModel;
import com.portableehr.sdk.models.ModelRefreshPolicyEnum;
import com.portableehr.sdk.network.NAO.calls.NoResponseContentCall;
import com.portableehr.sdk.network.NAO.calls.NotificationsListCall;
import com.portableehr.sdk.network.NAO.inbound.IBNotification;
import com.portableehr.sdk.network.NAO.inbound.IBUser;
import com.portableehr.sdk.network.ehrApi.EHRApiServer;
import com.portableehr.sdk.network.ehrApi.EHRServerRequest;
import com.portableehr.sdk.network.gson.GSONexcludeInbound;
import com.portableehr.sdk.network.gson.GSONexcludeOutbound;
import com.portableehr.sdk.network.protocols.ICaller;
import com.portableehr.sdk.network.protocols.ICompletionHandler;
import com.portableehr.sdk.util.FileUtils;
import com.portableehr.sdk.util.SetsAndStuff;
import com.portableehr.sdk.util.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static com.portableehr.sdk.EHRLibRuntime.kEpochStart;
import static com.portableehr.sdk.EHRLibRuntime.kEventNotificationsUpdated;
import static com.portableehr.sdk.EHRLibRuntime.kModulePrefix;
import static com.portableehr.sdk.network.gson.GsonFactory.standardBuilder;

/**
 * Created by : yvesleborg
 * Date       : 2017-03-31
 * <p>
 * Copyright Portable Ehr Inc, 2017-2019
 */

@SuppressWarnings("unused")
public class NotificationModel extends AbstractPollingModel implements ICompletionHandler {

    private static long mShortPollInterval = 15; // seconds
    private static long mLongPollInterval  = 900; // seconds


    private Hashtable<String, IBNotification> allNotifications;
    private Date                              lastRefreshed;
    private NotificationsModelFilter          allNotificationsFilter;
    private NotificationsModelFilter          patientNotificationsFilter;
    private NotificationsModelFilter          infoNotificationsFilter;
    private NotificationsModelFilter          alertNotificationsFilter;
    private NotificationsModelFilter          practitionerNotificationsFilter;
    private NotificationsModelFilter          messageNotificationsFilter;
    private NotificationsModelFilter          privateMessageNotificationsFilter;


    @GSONexcludeOutbound
    @GSONexcludeInbound
    IBUser user;

    @GSONexcludeOutbound
    NotificationInbox notificationInbox;
    @GSONexcludeOutbound
    AppointmentInbox  appointmentInbox;


    // really private shit here, dont persist at all

    @GSONexcludeOutbound
    private NotificationsListCall notificationsListCall;
    @GSONexcludeOutbound
    private boolean               mIsCallInProgress;
//    @GSONexcludeOutbound
//    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);


    private static NotificationModel ourInstance = null;

    public static NotificationModel getInstance() {
        if (null == ourInstance) {

            if (FileUtils.notificationsModelExistsOnDevice()) {
                ourInstance = loadFromDevice();
                ourInstance.flushExpiredAndDeletedFromCurrent();
                ourInstance.refreshFilters();
            } else {
                ourInstance = initOnDevice();
                ourInstance.save();
            }
            if (ourInstance == null) {
                throw new RuntimeException("Unable to create a notificationModel instance");
            }
        }
        return ourInstance;
    }

    private NotificationModel() {
        super();
        onNew();
        this.notificationInbox = new NotificationInbox();
        this.appointmentInbox = new AppointmentInbox();

        this.allNotifications = new Hashtable<>();
        this.allNotificationsFilter = new NotificationsModelFilter(NotificationModelTypeEnum.ALL);
        this.alertNotificationsFilter = new NotificationsModelFilter(NotificationModelTypeEnum.ALERT);
        this.patientNotificationsFilter = new NotificationsModelFilter(NotificationModelTypeEnum.PATIENT);
        this.infoNotificationsFilter = new NotificationsModelFilter(NotificationModelTypeEnum.INFO);
        this.practitionerNotificationsFilter = new NotificationsModelFilter(NotificationModelTypeEnum.PRACTITIONER);
        this.messageNotificationsFilter = new NotificationsModelFilter(NotificationModelTypeEnum.MESSAGE);
        this.privateMessageNotificationsFilter = new NotificationsModelFilter(NotificationModelTypeEnum.PRIVATE_MESSAGE);
        this.lastRefreshed = kEpochStart;
        this.user = IBUser.guest();

    }

    private NotificationModel(IBUser user) {
        this();
        this.user = user;
    }


    public void setUser(IBUser user, @Nullable ICompletionHandler handler) {
        this.user = user;
        getAllNotifications().clear();
        refreshFilters();
        if (user == IBUser.guest()) {
            this.setPollingPolicy(ModelRefreshPolicyEnum.NONE);
            this.cancelPoll();
            if (handler != null) {
                handler.handleSuccess(null);
            }
        } else {
            cancelPoll();
            setPollingPolicy(ModelRefreshPolicyEnum.NONE);

            this.user = user;
            RestCallOptions options = new RestCallOptions(true, false, 3, 8);
            EHRLibRuntime.getInstance().api.app.notification.list(kEpochStart, handler, options);
        }

    }

    //region AbstractPollingModel implementation

    protected boolean getVerbose() {
        return NotificationModel.classCountable;
    }

    @Override
    protected void implementPollAction() {
        if (getVerbose()) {
            Log.d(getLogTAG(), "implementPollAction " + at());
        }
        refreshFromServer();
    }

    @Override
    protected void implementPollActionCancel() {
        if (getVerbose()) {
            Log.d(getLogTAG(), "implementPollActionCancel() called" + at());
        }
        cancelPoll();
    }

    @Override
    protected void onPollActionStart() {
        if (getVerbose()) {
            Log.d(getLogTAG(), "onPollActionStart() called");
        }
    }

    @Override
    protected void onPollActionCancelStart() {
        if (getVerbose()) {
            Log.d(getLogTAG(), "onPollActionCancelStart() called");
        }
    }

    @Override
    public long getSmallestPollIntervalInSeconds() {
        return mShortPollInterval;
    }

    @Override
    public long getLargestPollingIntervalInSeconds() {
        return mLongPollInterval;
    }

    //endregion

    //region persistence and stuff

    public void reset() {

        flushNotificationsListCall();

        this.allNotificationsFilter = new NotificationsModelFilter(NotificationModelTypeEnum.ALL);
        this.alertNotificationsFilter = new NotificationsModelFilter(NotificationModelTypeEnum.ALERT);
        this.patientNotificationsFilter = new NotificationsModelFilter(NotificationModelTypeEnum.PATIENT);
        this.infoNotificationsFilter = new NotificationsModelFilter(NotificationModelTypeEnum.INFO);
        this.practitionerNotificationsFilter = new NotificationsModelFilter(NotificationModelTypeEnum.PRACTITIONER);
        this.messageNotificationsFilter = new NotificationsModelFilter(NotificationModelTypeEnum.MESSAGE);
        this.privateMessageNotificationsFilter = new NotificationsModelFilter(NotificationModelTypeEnum.PRIVATE_MESSAGE);
        lastRefreshed = kEpochStart;
        allNotifications.clear();

    }

    public static void resetOnDevice() {
        FileUtils.deleteNotificationsModelFromDevice();
        ourInstance = new NotificationModel();
        ourInstance.save();
    }


    /**
     * @param user the user for which we are creating on device.
     * @return a user model
     */
    public static NotificationModel createOnDevice(IBUser user) {
        NotificationModel um = new NotificationModel(user);
        um.save();
        return um;
    }

    public static NotificationModel loadFromDevice() {

        NotificationModel nm;
        String            json = FileUtils.readNotificationsModelJson();
        if (json == null) {
            nm = new NotificationModel();
        } else {
            nm = NotificationModel.fromJson(json);
        }
        return nm;
    }

    public static NotificationModel loadFromDevice(String userGuid) {
        NotificationModel nm        = new NotificationModel();
        String            folderFqn = FileUtils.pathOfUserDirectory(userGuid);
        if (folderFqn != null) {
            String jsonFqn = getFQN(userGuid);
            String json    = FileUtils.readJsonFromFilePath(jsonFqn);
            if (json != null) {
                nm = NotificationModel.fromJson(json);
            } else {
                Log.e(CLASSTAG, "loadFromDevice : got null json for " + userGuid);
            }
        } else {
            Log.e(CLASSTAG, "loadFromDevice : null path for notification model !");
        }
        return nm;
    }

    public static NotificationModel loadFromDevice(IBUser user) {
        NotificationModel nm = NotificationModel.loadFromDevice(user.getGuid());
        nm.user = user;
        return nm;
    }


    private static NotificationModel initOnDevice() {
        NotificationModel nm      = new NotificationModel();
        boolean           success = nm.save();
        if (!success) {
            nm = null;
            Log.e(CLASSTAG, "Unable to write the notificationModel, device not inited properly.");
        }
        return nm;
    }

    public boolean save() {
        return FileUtils.saveNotificationsModelOnDevice(this);
    }

    //endregion

    //region business method , filters and stuff


    public Integer numberOfUnseen() {
        Integer unseen = 0;
//        Hashtable<String, IBNotification> all    = NotificationModel.getInstance().getAllNotifications();
        Hashtable<String, IBNotification> all = allNotifications;
        for (IBNotification not : all.values()) {

            if (not.isArchived()) {
                continue;
            }

            if (not.isDeleted()) {
                continue;
            }

            if (not.getSeenOn() == null) {
                unseen++;
            }

        }
        if (getVerbose()) {
            Log.d(getLogTAG(), "Number of unseen : " + unseen);
            Log.d(getLogTAG(), "allNotifications size " + allNotifications.size());
        }
        return unseen;
    }

    public Integer numberOfActionRequried(IBUser user) {
        Integer                           noRequiringAction = 0;
        Hashtable<String, IBNotification> all               = NotificationModel.getInstance().getAllNotifications();
        for (IBNotification not : all.values()) {

            if (not.isArchived()) {
                continue;
            }
            if (not.isDeleted()) {
                continue;
            }
            if (not.isActionRequired(user)) {
                noRequiringAction++;
            }

        }
        return noRequiringAction;
    }

    public Integer numberOfActive() {
        Integer                           noActive = 0;
        Hashtable<String, IBNotification> all      = NotificationModel.getInstance().getAllNotifications();
        for (IBNotification not : all.values()) {

            if (not.isDeleted()) {
                continue;
            }

            noActive++;
        }
        return noActive;
    }

    public void refreshFilters() {
        appointmentInbox.refreshContent();
        notificationInbox.refreshContent();

        allNotificationsFilter.refreshFilter();
        privateMessageNotificationsFilter.refreshFilter();
        tellListeners();
    }

    private void tellListeners() {
        if (getVerbose()) {
            Log.d(getLogTAG(), "Telling listeners about an update to notifications.");
        }
        Intent teller = new Intent(kEventNotificationsUpdated);
        teller.putExtra("message", kEventNotificationsUpdated);
        LocalBroadcastManager.getInstance(EHRLibRuntime.getInstance().getContext()).sendBroadcast(teller);

    }

    public NotificationsModelFilter getFilterOfType(NotificationModelTypeEnum type) {
        switch (type) {
            case ALL:
                return getAllNotificationsFilter();
            case INFO:
                return getInfoNotificationsFilter();
            case ALERT:
                return getAlertNotificationsFilter();
            case PATIENT:
            case MESSAGE:
                return getPatientNotificationsFilter();
            case PRIVATE_MESSAGE:
                return getPrivateMessageNotificationsFilter();
            default:
                Log.e(getLogTAG(), "getFilterOfType invoked with invalid type.");
        }
        return getAllNotificationsFilter();
    }

    public NotificationsModelFilter getAllNotificationsFilter() {
        return allNotificationsFilter;
    }

    public void setAllNotificationsFilter(NotificationsModelFilter allNotificationsFilter) {
        this.allNotificationsFilter = allNotificationsFilter;
    }

    public NotificationsModelFilter getAlertNotificationsFilter() {
        return alertNotificationsFilter;
    }

    public void setAlertNotificationsFilter(NotificationsModelFilter sponsorNotificationsFilter) {
        this.alertNotificationsFilter = sponsorNotificationsFilter;
    }

    public NotificationsModelFilter getPatientNotificationsFilter() {
        return patientNotificationsFilter;
    }

    public void setPatientNotificationsFilter(NotificationsModelFilter patientNotificationsFilter) {
        this.patientNotificationsFilter = patientNotificationsFilter;
    }

    public NotificationsModelFilter getInfoNotificationsFilter() {
        return infoNotificationsFilter;
    }

    public void setInfoNotificationsFilter(NotificationsModelFilter infoNotificationsFilter) {
        this.infoNotificationsFilter = infoNotificationsFilter;
    }

    public NotificationsModelFilter getPractitionerNotificationsFilter() {
        return practitionerNotificationsFilter;
    }

    public void setPractitionerNotificationsFilter(NotificationsModelFilter practitionerNotificationsFilter) {
        this.practitionerNotificationsFilter = practitionerNotificationsFilter;
    }

    public NotificationsModelFilter getMessageNotificationsFilter() {
        return messageNotificationsFilter;
    }

    public void setMessageNotificationsFilter(NotificationsModelFilter messageNotificationsFilter) {
        this.messageNotificationsFilter = messageNotificationsFilter;
    }

    public NotificationsModelFilter getPrivateMessageNotificationsFilter() {
        return privateMessageNotificationsFilter;
    }

    public void setPrivateMessageNotificationsFilter(NotificationsModelFilter privateMessageNotificationsFilter) {
        this.privateMessageNotificationsFilter = privateMessageNotificationsFilter;
    }

    public Hashtable<String, IBNotification> getAllNotifications() {
        return allNotifications;
    }

    public void setAllNotifications(Hashtable<String, IBNotification> allNotifications) {
        this.allNotifications = allNotifications;
    }

    //endregion

    //region Inboxes
    public NotificationInbox getNotificationsInbox() {
        return notificationInbox;
    }

    public AppointmentInbox getAppointmentsInbox() {
        return appointmentInbox;
    }

    //endregin

    //region Networking , refresh model

    public void refreshFromServer() {
        loadFromServer(this.lastRefreshed);
    }

    public void reloadFromServer() {
        loadFromServer(null);
    }

    public void loadFromServer(@Nullable Date since) {
        if (getVerbose()) {
            Log.d(getLogTAG(), "loadFromServer() called with: since = [" + since + "]");
        }

        if (EHRLibRuntime.getInstance().getUser().isGuest()) {
            Log.d(getLogTAG(), "loadFromServer : skipping for user guest.");
            return;
        }

        if (null == since) {
            since = kEpochStart;
        }

        ICompletionHandler handler = new ICompletionHandler() {
            @Override
            public void handleSuccess(@Nullable ICaller theCall) {
                try {
                    NotificationsListCall nlc = (NotificationsListCall) theCall;
                    if (getVerbose()) {
                        Log.d(getLogTAG(), "handle success : notification list got " + Objects.requireNonNull(nlc).getResponseContent().getNotifications().size() + " notifications.");
                    }
                    populateFrom(Objects.requireNonNull(nlc).getResponseContent().getNotifications());
                } catch (Exception e) {
                    Log.e(getLogTAG(), "handleSuccess : error while processing notifications list.\n" + StringUtils.getStackTrace(e));
                } finally {
                    mIsCallInProgress = false;
                    signalPollActionComplete();
                }
            }

            @Override
            public void handleError(@Nullable ICaller theCall) {
                try {
                    NotificationsListCall nlc = (NotificationsListCall) theCall;
                    Log.e(getLogTAG(), "handleError : error while getting list " + Objects.requireNonNull(nlc).toString());
                    if (notificationsListCall.getRequestStatus() != null) {
                        Log.e(getLogTAG(), "handleError : error while getting list " + theCall.toString());
                    }
                } catch (Exception e) {
                    Log.e(getLogTAG(), "handleError : error while processing error.\n" + StringUtils.getStackTrace(e));
                } finally {
                    mIsCallInProgress = false;
                    signalPollActionComplete();
                }
            }

            @Override
            public void handleCancel(@Nullable ICaller theCall) {
                try {
                    NotificationsListCall nlc = (NotificationsListCall) theCall;
                    Log.e(getLogTAG(), "handleCancel : notification list call cancelled " + Objects.requireNonNull(nlc).toString());
                    if (notificationsListCall.getRequestStatus() != null) {
                        Log.e(getLogTAG(), "handleCancel : error while getting list " + nlc.toString());
                    }
                } catch (Exception e) {
                    Log.e(getLogTAG(), "handleCancel : error while processing cancel.\n" + StringUtils.getStackTrace(e));
                } finally {
                    mIsCallInProgress = false;
                    signalPollActionComplete();
                }
            }
        };

        EHRLibRuntime.getInstance().api.app.notification.list(since, handler, RestCallOptions.defaults());
    }

    private void refreshFromServer(ICompletionHandler handler) {

        RestCallOptions options = new RestCallOptions(true, false, 3, 8);
        EHRLibRuntime.getInstance().api.app.notification.list(this.lastRefreshed, handler, options);
    }


    //region set Seen, repoll
    public void setNotificationSeen(final IBNotification notification, final ICompletionHandler handler) {
        final ModelRefreshPolicyEnum restorePolicy = getPollingPolicy();
        if (mIsCallInProgress) {
            flushNotificationsListCall();
        }
        setPollingPolicy(ModelRefreshPolicyEnum.NONE);

       ICompletionHandler innerHandler =  new ICompletionHandler() {
            @Override
            public void handleSuccess(ICaller theCall) {
                refreshFromServer(new ICompletionHandler() {
                    @Override
                    public void handleSuccess(ICaller theCall) {
                        handler.handleSuccess(theCall);
                        setPollingPolicy(restorePolicy);
                        notification.setProgress("seen");
                        notification.setLastSeen(new Date());
                    }

                    @Override
                    public void handleError(ICaller theCall) {
                        handler.handleError(theCall);
                        setPollingPolicy(restorePolicy);
                    }

                    @Override
                    public void handleCancel(ICaller thCall) {
                        handler.handleError(thCall);
                        setPollingPolicy(restorePolicy);
                    }
                });
            }

            @Override
            public void handleError(ICaller theCall) {
                handler.handleError(theCall);
                setPollingPolicy(restorePolicy);
            }

            @Override
            public void handleCancel(ICaller thCall) {
                handler.handleCancel(thCall);
                setPollingPolicy(restorePolicy);
            }
        };
       EHRLibRuntime.getInstance().api.app.notification.setSeen(notification,handler,RestCallOptions.defaults());
    }

    //endregion

    //region Delete then poll again

    public void setNotificationDeleted(IBNotification notification, final ICompletionHandler handler) {
        final ModelRefreshPolicyEnum restorePolicy = getPollingPolicy();
        if (mIsCallInProgress) {
            flushNotificationsListCall();
        }
        setPollingPolicy(ModelRefreshPolicyEnum.NONE);

        NoResponseContentCall archiveCall = this.createNotificationDeletedCall(notification, new ICompletionHandler() {
            @Override
            public void handleSuccess(ICaller theCall) {
                refreshFromServer(new ICompletionHandler() {
                    @Override
                    public void handleSuccess(ICaller theCall) {
                        handler.handleSuccess(theCall);
                        setPollingPolicy(restorePolicy);
                    }

                    @Override
                    public void handleError(ICaller theCall) {
                        handler.handleError(theCall);
                        setPollingPolicy(restorePolicy);
                    }

                    @Override
                    public void handleCancel(ICaller thCall) {
                        handler.handleError(thCall);
                        setPollingPolicy(restorePolicy);
                    }
                });
            }

            @Override
            public void handleError(ICaller theCall) {
                handler.handleError(theCall);
                setPollingPolicy(restorePolicy);
            }

            @Override
            public void handleCancel(ICaller thCall) {
                handler.handleCancel(thCall);
                setPollingPolicy(restorePolicy);
            }
        });
        notification.setProgress("deleted");
        notification.setArchivedOn(new Date());
        archiveCall.callOnNewThread();
    }

    NoResponseContentCall createNotificationDeletedCall(IBNotification notification, ICompletionHandler handler) {
        EHRServerRequest serverRequest = new EHRServerRequest(EHRLibRuntime.getInstance().getServer(),
                                                              EHRLibRuntime.getInstance().getUser(),
                                                              EHRLibRuntime.getInstance().getDeviceInfo(),
                                                              EHRLibRuntime.getInstance().getDeviceLanguage(),
                                                              "/app/notification",
                                                              "deleteNotification");
        String[] guids = new String[]{notification.getGuid()};
        serverRequest.setParameter("guids", guids);
        NoResponseContentCall call = new NoResponseContentCall(serverRequest, handler);
        return call;
    }
    //endregion

    //region Archive then poll again

    public void setNotificationArchived(IBNotification notification, final ICompletionHandler handler) {
        final ModelRefreshPolicyEnum restorePolicy = getPollingPolicy();
        if (mIsCallInProgress) {
            flushNotificationsListCall();
        }
        setPollingPolicy(ModelRefreshPolicyEnum.NONE);

        NoResponseContentCall archiveCall = this.createNotificationArchivedCall(notification, new ICompletionHandler() {
            @Override
            public void handleSuccess(ICaller theCall) {
                refreshFromServer(new ICompletionHandler() {
                    @Override
                    public void handleSuccess(ICaller theCall) {
                        handler.handleSuccess(theCall);
                        setPollingPolicy(restorePolicy);
                    }

                    @Override
                    public void handleError(ICaller theCall) {
                        handler.handleError(theCall);
                        setPollingPolicy(restorePolicy);
                    }

                    @Override
                    public void handleCancel(ICaller thCall) {
                        handler.handleError(thCall);
                        setPollingPolicy(restorePolicy);
                    }
                });
            }

            @Override
            public void handleError(ICaller theCall) {
                handler.handleError(theCall);
                setPollingPolicy(restorePolicy);
            }

            @Override
            public void handleCancel(ICaller thCall) {
                handler.handleCancel(thCall);
                setPollingPolicy(restorePolicy);
            }
        });
        notification.setProgress("archived");
        notification.setArchivedOn(new Date());
        archiveCall.callOnNewThread();
    }

    NoResponseContentCall createNotificationArchivedCall(IBNotification notification, ICompletionHandler handler) {
        EHRServerRequest serverRequest = new EHRServerRequest(EHRLibRuntime.getInstance().getServer(),
                                                              EHRLibRuntime.getInstance().getUser(),
                                                              EHRLibRuntime.getInstance().getDeviceInfo(),
                                                              EHRLibRuntime.getInstance().getDeviceLanguage(),
                                                              "/app/notification",
                                                              "archive");
        serverRequest.setParameter("guid", notification.getGuid());
        NoResponseContentCall call = new NoResponseContentCall(serverRequest, handler);
        return call;
    }
    //endregion

    //region Unarchive then poll again
    public void setNotificationUnarchived(IBNotification notification, final ICompletionHandler handler) {
        final ModelRefreshPolicyEnum restorePolicy = getPollingPolicy();
        if (mIsCallInProgress) {
            flushNotificationsListCall();
        }
        setPollingPolicy(ModelRefreshPolicyEnum.NONE);

        NoResponseContentCall unarchiveCall = this.createNotificationUnarchivedCall(notification, new ICompletionHandler() {
            @Override
            public void handleSuccess(ICaller theCall) {
                refreshFromServer(new ICompletionHandler() {
                    @Override
                    public void handleSuccess(ICaller theCall) {
                        handler.handleSuccess(theCall);
                        setPollingPolicy(restorePolicy);
                    }

                    @Override
                    public void handleError(ICaller theCall) {
                        handler.handleError(theCall);
                        setPollingPolicy(restorePolicy);
                    }

                    @Override
                    public void handleCancel(ICaller thCall) {
                        handler.handleError(thCall);
                        setPollingPolicy(restorePolicy);
                    }
                });
            }

            @Override
            public void handleError(ICaller theCall) {
                handler.handleError(theCall);
                setPollingPolicy(restorePolicy);
            }

            @Override
            public void handleCancel(ICaller thCall) {
                handler.handleCancel(thCall);
                setPollingPolicy(restorePolicy);
            }
        });
        if (notification.isSeen()) {
            notification.setProgress("seen");
        } else {
            notification.setProgress("delivered");
        }
        notification.setArchivedOn(null);
        unarchiveCall.callOnNewThread();
    }

    NoResponseContentCall createNotificationUnarchivedCall(IBNotification notification, ICompletionHandler handler) {
        EHRServerRequest serverRequest = new EHRServerRequest(EHRLibRuntime.getInstance().getServer(),
                                                              EHRLibRuntime.getInstance().getUser(),
                                                              EHRLibRuntime.getInstance().getDeviceInfo(),
                                                              EHRLibRuntime.getInstance().getDeviceLanguage(),
                                                              "/app/notification",
                                                              "unarchive");
        serverRequest.setParameter("guid", notification.getGuid());
        NoResponseContentCall call = new NoResponseContentCall(serverRequest, handler);
        return call;
    }
    //endregion

    private void createNotificationsListCall() {
        flushNotificationsListCall();
        EHRServerRequest listRequest = new EHRServerRequest(
                EHRApiServer.defaultApiServer(),
                EHRLibRuntime.getInstance().getUser(),
                EHRLibRuntime.getInstance().getDeviceInfo(),
                EHRLibRuntime.getInstance().getDeviceLanguage(),
                "/app/notification",
                "list"
        );
        notificationsListCall = new NotificationsListCall(listRequest, this);
    }

    private void flushNotificationsListCall() {
        if (null != notificationsListCall) {
            notificationsListCall.cancel();
            notificationsListCall = null;
            mIsCallInProgress = false;
        }
    }

    @Override
    public void handleSuccess(ICaller theCall) {
        mIsCallInProgress = false;
        if (theCall == notificationsListCall) {
            if (getVerbose()) {
                Log.d(getLogTAG(), "handle success : notification list got " + notificationsListCall.getResponseContent().getNotifications().size() + " notifications.");
            }
            populateFrom(notificationsListCall.getResponseContent().getNotifications());
        } else {
            Log.w(getLogTAG(), "handleSuccess : dont know caller " + theCall.toString());
            try {
                NotificationsListCall someCall = (NotificationsListCall) theCall;
                populateFrom(someCall.getResponseContent().getNotifications());
            } catch (Exception e) {
                Log.e(getLogTAG(), "Exception when unpacking call", e);
            }

        }
        flushNotificationsListCall();
        signalPollActionComplete();
    }

    @Override
    public void handleError(ICaller theCall) {
        mIsCallInProgress = false;
        if (theCall == notificationsListCall) {
            Log.e(getLogTAG(), "handleError : error while getting list " + theCall.toString());
            if (notificationsListCall.getRequestStatus() != null) {
                Log.e(getLogTAG(), "handleError : error while getting list " + theCall.toString());
            }
        } else {
            Log.e(getLogTAG(), "handleError : dont know caller " + theCall.toString());
        }
        flushNotificationsListCall();
        signalPollActionComplete();
    }

    public void handleCancel(ICaller theCall) {
        if (getVerbose()) {
            Log.d(getLogTAG(), "handleCancel() called with: theCall = [" + theCall + "]" + at());
        }
        mIsCallInProgress = false;
        signalPollActionCancelComplete();
    }

    //endregion

    /**
     * Note : we keep our notifications keyed on seq, which is an ordered (ascending),
     * time-based list we are getting from the server.
     */
    public void populateFrom(HashMap<String, IBNotification> source) {

        if (null == source) {
            Log.e(getLogTAG(), "populateFrom: *** eceived null set from network , bailing out!");
            return;
        }

        flushExpiredAndDeletedFromSource(source);
        Date lastUpdated = this.lastRefreshed;
        if (lastUpdated == null) {
            lastUpdated = kEpochStart;
        }

        for (IBNotification theNot : source.values()) {

            Date notUpdate = theNot.getLastUpdated();
            if (notUpdate != null) {
                if (lastUpdated != null) {
                    if (notUpdate.after(lastUpdated)) {
                        lastUpdated = notUpdate;
                    }
                } else {
                    lastUpdated = notUpdate;
                }
            } else {
                Log.wtf(getLogTAG(), "Got notification with no update time !  skipping");
                continue;
            }

            String guid = theNot.getGuid();
            String seq  = theNot.getSeq();
            if (seq == null) {
                Log.e(getLogTAG(), "Received a notification with null seq, ignored.");
                continue;
            }
            if (guid == null) {
                Log.e(getLogTAG(), "Received a notification with null guid, ignored.");
                continue;
            }

            IBNotification oldie = allNotifications.get(seq);

            if (null == oldie) {
                // add a new element that was not there before !
                allNotifications.put(theNot.getSeq(), theNot);
            } else {
                // replace ours with latest received from server , who knows
                oldie.updateWith(theNot);
            }

        }

        // ok, almost done here , add 1 second to last updated.

        wasRefreshedAt(lastUpdated);

    }

    private void flushExpiredAndDeletedFromSource(HashMap<String, IBNotification> source) {

        if (null == source) {
            Log.e(TAG, "flushExpiredAndDeletedFromSource: Received null source set!");
            return;
        }
        ArrayList<String> gonners = new ArrayList<>();
        for (Map.Entry<String, IBNotification> innerEntry : source.entrySet()) {
            IBNotification innerNot = innerEntry.getValue();
            if (innerNot.isDeleted()) {
                if (getVerbose()) {
                    Log.d(getLogTAG(), "NOT keeping deleted notification " + innerNot.getSeq());
                }
                gonners.add(innerNot.getSeq());
                continue;
            }

            if (innerNot.isExpired()) {
                if (getVerbose()) {
                    Log.d(getLogTAG(), "NOT keeping expired notification " + innerNot.getSeq());
                }
                gonners.add(innerNot.getSeq());
                //noinspection UnnecessaryContinue
                continue;
            }
        }

        // now purge all notifications properly

        for (String key : gonners) {
            source.remove(key);
        }

    }

    private void flushExpiredAndDeletedFromCurrent() {

        ArrayList<String> gonners = new ArrayList<>();
        for (Map.Entry<String, IBNotification> innerEntry : allNotifications.entrySet()) {
            IBNotification innerNot = innerEntry.getValue();
            if (innerNot.isDeleted()) {
                if (getVerbose()) {
                    Log.d(getLogTAG(), "NOT keeping persisted/deleted notification " + innerNot.getSeq());
                }
                gonners.add(innerNot.getSeq());
                continue;
            }

            if (innerNot.isExpired()) {
                if (getVerbose()) {
                    Log.d(getLogTAG(), "NOT keeping persisted/expired notification " + innerNot.getSeq());
                }
                gonners.add(innerNot.getSeq());
                //noinspection UnnecessaryContinue
                continue;
            }
        }

        // now purge all notifications properly

        if (gonners.size() > 0) {
            for (String key : gonners) {
                allNotifications.remove(key);
            }
            save();
        }

    }

    public void wasRefreshedAt(Date lastRefreshed) {
        // if(getVerbose()) Log.d(getLogTAG(), "wasRefreshedAt() called with: lastRefreshed = [" + lastRefreshed + "]");
        lastRefreshed.setTime(lastRefreshed.getTime());
        this.lastRefreshed = lastRefreshed;
        save();
        refreshFilters();
    }


    public Hashtable<String, IBNotification> getInboxContent(AbstractInbox inboxFilter) {

        String                            capability = inboxFilter.getCapabilityAlias();
        Hashtable<String, IBNotification> content    = new Hashtable<>();

        // guard against update during scan
        // use a clone to iterate
        // clone is atomic & synchronized

        Set<String> keySet = SetsAndStuff.cloneKeySet(getAllNotifications().keySet());

        for (String key : keySet) {
            IBNotification not = getAllNotifications().get(key);
            if (not == null) {
                Log.e(getLogTAG(), "getInboxContent: Notifications changed while scanning keyset, skipping key " + key);
                continue;
            }

            if (not.getCapabilityAlias() != null) {
                if (not.getCapabilityAlias().contentEquals(capability)) {
                    if (not.isDeleted()) {
//                        Log.d(getLogTAG(), "getInboxContent: skipping deleted " + capability + " not with seq + " + key);
                        continue;
                    }
                    content.put(key, not);
                }
            } else {
                Log.e(getLogTAG(), "getInboxContent: Skipping notification with no capabilityAlias" + not.getSeq());
            }
        }
        return content;
    }

    @Nullable
    public IBNotification getNotificationWithGuid(String guid) {
        for (Map.Entry<String, IBNotification> entry : allNotifications.entrySet()) {
            if (entry.getValue().getGuid().equals(guid)) {
                return entry.getValue();
            }
        }
        return null;
    }

    //region Countable

    {
        setClassCountable(false);
    }

    private final static String  CLASSTAG = kModulePrefix + "." + NotificationModel.class.getSimpleName();
    @GSONexcludeOutbound
    private              String  TAG;
    private static       int     lifeTimeInstances;
    private static       int     numberOfInstances;
    @GSONexcludeOutbound
    private              int     instanceNumber;
    @GSONexcludeOutbound
    private static       boolean classCountable;

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

    @SuppressWarnings("unused")
    public static boolean existsOnDevice(String userGuid) {
        String fileName = getFQN(userGuid);
        if (fileName != null) {
            File f = new File(fileName);
            return f.exists();
        }
        Log.e(CLASSTAG, "existsOnDevice : error for userGuid " + userGuid);
        return false;
    }

    private static String getFQN(IBUser user) {
        if (null == user) {
            Log.e(CLASSTAG, "getFQN : received null user , bailing out.");
            return null;
        }
        return getFQN(user.getGuid());
    }

    private static String getFQN(String userGuid) {
        String folderPath = getFolderFQP(userGuid);
        if (null == folderPath) {
            return null;
        }

        String filePath = folderPath + File.separator + FileUtils.notificationsModelFileName;
        return filePath;
    }

    private static String getFolderFQP(String userGuid) {
        if (null == userGuid) {
            Log.e(CLASSTAG, "getFolderFQP : received null userGuid , bailing out.");
            return null;
        }

        String filePath = null;
        File   fd       = EHRLibRuntime.getInstance().getContext().getFilesDir();
        if (null == fd) {
            Log.e(CLASSTAG, "getFolderFQP : Unable to get files dir from ApplicationExt, bailing out.");
        } else {
            filePath = fd.getPath() + File.separator + userGuid;
        }
        return filePath;
    }

    @SuppressWarnings("unused")
    private String getFQN() {
        return getFQN(this.user);
    }

    //endregion

    //region GSON helpers


    public String asJson() {
        GsonBuilder builder        = standardBuilder();
        Gson        jsonSerializer = builder.create();
        String      theJson        = jsonSerializer.toJson(this, this.getClass());
        return theJson;
    }

    public static NotificationModel fromJson(String json) {
        GsonBuilder       builder          = standardBuilder();
        Gson              jsonDeserializer = builder.create();
        NotificationModel theObject        = jsonDeserializer.fromJson(json, NotificationModel.class);
        return theObject;
    }

    //endregion

}
