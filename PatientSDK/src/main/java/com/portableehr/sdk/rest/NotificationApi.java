package com.portableehr.sdk.rest;

import androidx.annotation.Nullable;

import com.portableehr.sdk.RestCallOptions;
import com.portableehr.sdk.EHRLibRuntime;
import com.portableehr.sdk.network.NAO.calls.NoResponseContentCall;
import com.portableehr.sdk.network.NAO.calls.NotificationsListCall;
import com.portableehr.sdk.network.NAO.inbound.IBNotification;
import com.portableehr.sdk.network.ehrApi.EHRServerRequest;
import com.portableehr.sdk.network.protocols.ICaller;
import com.portableehr.sdk.network.protocols.ICompletionHandler;

import java.util.Date;

/**
 * Created by : yvesleborg
 * Date       : 2020-08-13
 * <p>
 * Copyright Portable Ehr Inc, 2020
 */
public class NotificationApi {
    NotificationApi() {
    }

    //region Notification state changes
    public void setSeen(final IBNotification notification, final ICompletionHandler handler, @Nullable RestCallOptions options) {
        if (null == options) {
            options = RestCallOptions.defaults();
        }
        EHRServerRequest request = EHRLibRuntime.getInstance().getRequest("/app/notification", "seen");
        final String[]   guids   = new String[]{notification.getGuid()};
        request.setParameter("guids", guids);
        ICompletionHandler handlerWrapper = new ICompletionHandler() {
            @Override
            public void handleSuccess(ICaller theCall) {
//                    Hashtable<String, IBNotification> nots = Runtime.getInstance().getNotificationModel().getAllNotifications();
                notification.setSeenOn(new Date());
                notification.setLastSeen(new Date());
                notification.setProgress("seen");
                handler.handleSuccess(theCall);
            }

            @Override
            public void handleError(ICaller theCall) {
                handler.handleError(theCall);
            }

            @Override
            public void handleCancel(ICaller theCall) {
                handler.handleCancel(theCall);
            }
        };
        NoResponseContentCall call = new NoResponseContentCall(request, handlerWrapper);
        call.applyOptions(options);
        call.call();
    }

    @SuppressWarnings("unused")
    public void setSeen(IBNotification notification, ICompletionHandler handler) {
        setSeen(notification, handler, RestCallOptions.defaults());
    }

    public void setDeleted(final IBNotification notification, final ICompletionHandler handler, @Nullable RestCallOptions options) {
        if (null == options) {
            options = RestCallOptions.defaults();
        }
        EHRServerRequest request = EHRLibRuntime.getInstance().getRequest("/app/notification", "deleteNotification");
        final String[]   guids   = new String[]{notification.getGuid()};
        request.setParameter("guids", guids);
        ICompletionHandler handlerWrapper = new ICompletionHandler() {
            @Override
            public void handleSuccess(ICaller theCall) {
//                    Hashtable<String, IBNotification> nots = Runtime.getInstance().getNotificationModel().getAllNotifications();
                if (null == notification.getArchivedOn()) {
                    notification.setArchivedOn(new Date());
                }
                notification.setDeletedOn(new Date());
                notification.setLastSeen(new Date());
                notification.setProgress("deleted");
                handler.handleSuccess(theCall);
            }

            @Override
            public void handleError(ICaller theCall) {
                handler.handleError(theCall);
            }

            @Override
            public void handleCancel(ICaller theCall) {
                handler.handleCancel(theCall);
            }
        };
        NoResponseContentCall call = new NoResponseContentCall(request, handlerWrapper);
        call.applyOptions(options);
        call.call();
    }

    @SuppressWarnings("unused")
    public void setDeleted(IBNotification notification, ICompletionHandler handler) {
        setDeleted(notification, handler, RestCallOptions.defaults());
    }

    public void setArchived(final IBNotification notification, final ICompletionHandler handler, @Nullable RestCallOptions options) {
        if (null == options) {
            options = RestCallOptions.defaults();
        }
        EHRServerRequest request = EHRLibRuntime.getInstance().getRequest("/app/notification", "archive");
        request.setParameter("guid", notification.getGuid());
        // todo : resolve with backend and Patient app, this use the 'single' guid approach
        // is the 'guids' approach really valid ?
        ICompletionHandler handlerWrapper = new ICompletionHandler() {
            @Override
            public void handleSuccess(ICaller theCall) {
//                    Hashtable<String, IBNotification> nots = Runtime.getInstance().getNotificationModel().getAllNotifications();
                if (null == notification.getArchivedOn()) {
                    notification.setArchivedOn(new Date());
                }
                notification.setDeletedOn(new Date());
                notification.setLastSeen(new Date());
                notification.setProgress("deleted");
                handler.handleSuccess(theCall);
            }

            @Override
            public void handleError(ICaller theCall) {
                handler.handleError(theCall);
            }

            @Override
            public void handleCancel(ICaller theCall) {
                handler.handleCancel(theCall);
            }
        };
        NoResponseContentCall call = new NoResponseContentCall(request, handlerWrapper);
        call.applyOptions(options);
        call.call();
    }

    @SuppressWarnings("unused")
    public void setArchived(IBNotification notification, ICompletionHandler handler) {
        setArchived(notification, handler, RestCallOptions.defaults());
    }

    public void setUnarchived(final IBNotification notification, final ICompletionHandler handler, @Nullable RestCallOptions options) {
        if (null == options) {
            options = RestCallOptions.defaults();
        }
        EHRServerRequest request = EHRLibRuntime.getInstance().getRequest("/app/notification", "unarchive");
        request.setParameter("guid", notification.getGuid());
        // todo : resolve with backend and Patient app, this use the 'single' guid approach
        // is the 'guids' approach really valid ?
        ICompletionHandler handlerWrapper = new ICompletionHandler() {
            @Override
            public void handleSuccess(ICaller theCall) {
//                    Hashtable<String, IBNotification> nots = Runtime.getInstance().getNotificationModel().getAllNotifications();
                if (null == notification.getArchivedOn()) {
                    notification.setArchivedOn(new Date());
                }
                if (notification.isSeen()) {
                    notification.setProgress("seen");
                } else {
                    notification.setProgress("delivered");
                }
                notification.setLastSeen(new Date());
                handler.handleSuccess(theCall);
            }

            @Override
            public void handleError(ICaller theCall) {
                handler.handleError(theCall);
            }

            @Override
            public void handleCancel(ICaller theCall) {
                handler.handleCancel(theCall);
            }
        };
        NoResponseContentCall call = new NoResponseContentCall(request, handlerWrapper);
        call.applyOptions(options);
        call.call();
    }

    @SuppressWarnings("unused")
    public void setUnarchived(IBNotification notification, ICompletionHandler handler) {
        setUnarchived(notification, handler, RestCallOptions.defaults());
    }
    //endregion


    //region List notifications
    public void list(Date since, ICompletionHandler handler, @Nullable RestCallOptions options) {
        if (null == options) {
            options = RestCallOptions.defaults();
        }
        EHRServerRequest      request = EHRLibRuntime.getInstance().getRequest("/app/notification", "list");
        NotificationsListCall call    = new NotificationsListCall(request, handler);
        call.getServerRequest().setParameter("since", since);
        call.getServerRequest().setParameter("type", "all");
        call.getServerRequest().setParameter("status", "all");
        call.applyOptions(options);
        call.call();
    }

    public void list(Date since, ICompletionHandler handler) {
        list(since, handler, RestCallOptions.defaults());
    }
    //endregion

}
