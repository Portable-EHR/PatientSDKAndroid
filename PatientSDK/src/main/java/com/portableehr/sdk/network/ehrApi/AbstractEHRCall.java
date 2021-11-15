package com.portableehr.sdk.network.ehrApi;

import android.content.Intent;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.portableehr.sdk.RestCallOptions;
import com.portableehr.sdk.EHRLibRuntime;
import com.portableehr.sdk.network.gson.GsonFactory;
import com.portableehr.sdk.network.protocols.ICaller;
import com.portableehr.sdk.network.protocols.ICompletionHandler;
import com.portableehr.sdk.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by : yvesleborg
 * Date       : 2017-04-13
 * <p>
 * Copyright Portable Ehr Inc, 2019
 */

public abstract class AbstractEHRCall implements ICaller {

    interface OnCallComplete extends ICompletionHandler {
        void handleSuccess(ICaller theCaller);

        void handleError(ICaller theCaller);

        void handleCancel(ICaller theCaller);
    }

    private       float              timeOut;
    private       Integer            maximumAttempts;
    private       Integer            attemptNumber;
    private final EHRServerRequest   serverRequest;
    private       EHRRequestStatus   requestStatus;
    @SuppressWarnings("FieldCanBeLocal")
    private       URL                url;
    private       boolean            isCallingServer;
    private       boolean            isResponseReceived;
    private       ICompletionHandler completionHandler;
    private       HttpsURLConnection connection;
    private       boolean            verbose;
    private       boolean            callOnNewThread;

    private OnCallComplete handler;

    /*
     * ctors
     */

    public AbstractEHRCall(EHRServerRequest serverRequest, ICompletionHandler completionHandler) {

        this.serverRequest = serverRequest;
        this.completionHandler = completionHandler;
        this.applyOptions(RestCallOptions.defaults());
    }

    //region Getters/Setters
    public EHRRequestStatus getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(EHRRequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }

    public EHRServerRequest getServerRequest() {
        return this.serverRequest;
    }

    public ICompletionHandler getCompletionHandler() {
        return completionHandler;
    }

    public void setCompletionHandler(ICompletionHandler completionHandler) {
        this.completionHandler = completionHandler;
    }

    public float getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(@SuppressWarnings("SameParameterValue") float timeOut) {
        this.timeOut = timeOut;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public Integer getMaximumAttempts() {
        return maximumAttempts;
    }

    public void setMaximumAttempts(@SuppressWarnings("SameParameterValue") Integer maximumAttempts) {
        this.maximumAttempts = maximumAttempts;
    }
    //endregion


    //region Public methods : call, callOnNewThread, cancel
    public void applyOptions(RestCallOptions callOptions) {
        // default call options to protect against calls with null options
        if(null==callOptions) callOptions=RestCallOptions.defaults();
        this.maximumAttempts = callOptions.maxAttempts;
        this.timeOut = callOptions.timeOutInSeconds;
        this.verbose = callOptions.verbose;
        this.callOnNewThread = callOptions.onNewThread;
    }

    private boolean performCall() {

        if (isVerbose()) {
            Log.d(TAG, "performCall() : invoked");
        }
        if (isCallingServer) {
            if (this.isVerbose()) {
                Log.e(TAG, "Call in progress , ignoring.");
            }
            this.getCompletionHandler().handleError(this);
            return false;
        }

        isCallingServer = true;


        InputStream       stream     = null;
        HttpURLConnection connection = null;
        url = this.serverRequest.getServer().urlForRoute(this.serverRequest.getRoute());
        try {
            // https://stackoverflow.com/questions/7610790/add-custom-headers-to-webview-resource-requests-android
            // todo : investigate the OkHttp client
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout((int) this.timeOut * 1000);
            connection.setConnectTimeout((int) this.timeOut * 1000);
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            // Open communications link (network traffic occurs here).

            OutputStream os      = connection.getOutputStream();
            GsonBuilder  builder = GsonFactory.standardBuilder();
            Gson         gson    = builder.create();
            String       json    = gson.toJson(this.serverRequest);
            os.write(json.getBytes(StandardCharsets.UTF_8));
            os.close();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_UNAUTHORIZED) {
                Log.e(TAG, "*****************************************************");
                Log.e(TAG, "** Telling listeners about an authorization failed **");
                Log.e(TAG, "*****************************************************");
                Intent teller = new Intent(EHRLibRuntime.kEventUnauthorized);
                teller.putExtra("auth", EHRLibRuntime.kEventUnauthorized);
                LocalBroadcastManager.getInstance(EHRLibRuntime.getInstance().getContext()).sendBroadcast(teller);
            } else if (responseCode != HttpsURLConnection.HTTP_OK) {
                if (requestStatus != null) {
                    requestStatus.setMessage("Failed to connect with server.");
                    requestStatus.setStatus(Integer.toString(responseCode));
                }
                Log.e(TAG, "Host is " + this.serverRequest.getServer().getServerDNSname());
                Log.e(TAG, "Got status " + responseCode);
                this.getCompletionHandler().handleError(this);
            } else {
                // Retrieve the response body as an InputStream.
                if (this.isVerbose()) {
                    Log.d(TAG, "Got status " + responseCode);
                }
                stream = connection.getInputStream();

                ByteArrayOutputStream resultBytes = new ByteArrayOutputStream();
                byte[]                buffer      = new byte[1024];
                int                   length;
                while ((length = stream.read(buffer)) != -1) {
                    resultBytes.write(buffer, 0, length);
                }
                // StandardCharsets.UTF_8.name() > JDK 7
                String jsonResponse = resultBytes.toString("UTF-8");
                stream.close();

                if (this.verbose) {
                    Log.v(TAG, "Got response : \n" + jsonResponse);
                }
                this.setRequestStatus(parse(jsonResponse));

                if (null != requestStatus) {
                    if (this.isVerbose()) {
                        Log.d(TAG, "Got OK status from server for Call " + this.getCompletionHandler().toString());
                    }
                    if (requestStatus.getStatus().equals("OK")) {
                        this.getCompletionHandler().handleSuccess(this);
                    } else if (requestStatus.getStatus().contentEquals("MAINTENANCE")) {
                        Log.e(TAG, "*****************************************************");
                        Log.e(TAG, "** Telling listeners about an MAINTENANCE period   **");
                        Log.e(TAG, "*****************************************************");
                        Intent teller = new Intent(EHRLibRuntime.kEventMaintenance);
                        teller.putExtra("auth", EHRLibRuntime.kEventMaintenance);
                        LocalBroadcastManager.getInstance(EHRLibRuntime.getInstance().getContext()).sendBroadcast(teller);
                    } else if (requestStatus.getStatus().contentEquals("AUTH_FAILED")) {
                        Log.e(TAG, "*****************************************************");
                        Log.e(TAG, "** Telling listeners about an AUTH_FAILED event    **");
                        Log.e(TAG, "*****************************************************");
                        Intent teller = new Intent(EHRLibRuntime.kEventUnauthorized);
                        teller.putExtra("auth", EHRLibRuntime.kEventUnauthorized);
                        LocalBroadcastManager.getInstance(EHRLibRuntime.getInstance().getContext()).sendBroadcast(teller);
                        this.getCompletionHandler().handleError(this);
                    } else if (requestStatus.getStatus().contentEquals("APP_VERSION")) {
                        Log.e(TAG, "Server requires a application update from the appstore.");
//                        cancel();
//                        this.getCompletionHandler().handleCancel(this);
                        Log.e(TAG, "*****************************************************");
                        Log.e(TAG, "** Telling listeners about APP_VERSION event    **");
                        Log.e(TAG, "*****************************************************");
                        Intent teller = new Intent(EHRLibRuntime.kEventAppUpdateAvailable);
                        teller.putExtra("auth", EHRLibRuntime.kEventAppUpdateAvailable);
                        LocalBroadcastManager.getInstance(EHRLibRuntime.getInstance().getContext()).sendBroadcast(teller);
                        this.getCompletionHandler().handleError(this);
                    } else {
                        Log.e(TAG, "host is " + this.serverRequest.getServer().getServerDNSname());
                        Log.d(TAG, "Got [" + requestStatus.getStatus() + "] status from server.");
                        String status = "(null)";
                        if (requestStatus != null) {
                            status = requestStatus.asJson();
                        }
                        Log.d(TAG, "Request status \n " + status);
                        this.getCompletionHandler().handleError(this);
                    }
                }
            }
            isCallingServer = false;
        } catch (InterruptedIOException cancelException) {
            isCallingServer = false;
            Log.d(TAG, "Cancel on host " + this.serverRequest.getServer().getServerDNSname());
            this.getCompletionHandler().handleCancel(this);
        } catch (Throwable e) {
            isCallingServer = false;
            Log.e(TAG, "Host is " + this.serverRequest.getServer().getServerDNSname());
            Log.wtf(TAG, "Caught exception when calling Server " + e.getMessage());
            Log.wtf(TAG, "Stack trace is \n", e);
            this.getCompletionHandler().handleError(this);
        } finally {
            // Close Stream and disconnect HTTPS connection.
            isCallingServer = false;
            if (stream != null) {
                try {
                    stream.close();
                } catch (Exception e) {
                    Log.e(TAG, "Caught exception when closing stream");
                    Log.e(TAG, StringUtils.getStackTrace(e));
                }
            }
            if (connection != null) {
                connection.disconnect();
            }

        }

        return true;

    }

    public boolean call() {
        if (!this.callOnNewThread) {
            return performCall();
        } else {
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    performCall();
                }
            };
            Thread T = new Thread(r);
            T.start();
            return true;
        }
    }

    public void callOnNewThread() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                performCall();
            }
        };
        Thread T = new Thread(r);
        T.start();
    }

    public boolean isCallingServer() {
        return isCallingServer;
    }

    @SuppressWarnings({"SameReturnValue", "UnusedReturnValue"})
    public boolean cancel() {
        if (isVerbose()) {
            Log.d(TAG, "cancel() : invoked ");
        }
        if (isCallingServer && connection != null) {
            if (isVerbose()) {
                Log.d(TAG, "candel() : forcing disconnect");
            }
            isCallingServer = false;
            connection.disconnect();
        }
        return true;
    }
    //endregion

    private <T> T convertInstanceOfObject(Object o, Class<T> clazz) {
        try {
            return clazz.cast(o);
        } catch (ClassCastException e) {
            return null;
        }
    }


    public static final String TAG = AbstractEHRCall.class.getName();

    public abstract EHRRequestStatus parse(String json);


}
