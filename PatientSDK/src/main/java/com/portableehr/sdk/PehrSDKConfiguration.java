package com.portableehr.sdk;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.portableehr.patientsdk.state.AppState;
import com.portableehr.sdk.network.NAO.calls.UpdatePushTokenCall;
import com.portableehr.sdk.network.NAO.inbound.IBVersion;
import com.portableehr.sdk.network.ehrApi.EHRApiServer;
import com.portableehr.sdk.network.ehrApi.EHRServerRequest;
import com.portableehr.sdk.network.protocols.ICaller;
import com.portableehr.sdk.network.protocols.ICompletionHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PehrSDKConfiguration {

    private static PehrSDKConfiguration instance = null;

    private Properties properties = null;
    private SharedPreferences sharedPreferences;

    public static final String PROPERTIES_FILE_NAME = "pehr-sdk.properties";
    public static final String APP_GUID_KEY = "pehr.sdk.appGuid";
    public static final String APP_ALIAS_KEY = "pehr.sdk.appAlias";
    public static final String APP_VERSION_KEY = "pehr.sdk.appVersion";
    public static final String APP_STACK_KEY_KEY = "pehr.sdk.stackKey";
    private static final String kDevicePushToken = "kDevicePushToken";

    private PehrSDKConfiguration(Properties properties) {
        this.properties = properties;

        sharedPreferences = EHRLibRuntime.getInstance().getContext().getSharedPreferences("userConfig", Context.MODE_PRIVATE);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("PehrSDKConfiguration", "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        String token = task.getResult();
                        updatePushToken(token);
                        Log.d("ZEUS", "FCM Token: " + token);
                    }
                });
    }

    public static PehrSDKConfiguration getInstance() {
        if (instance == null) {
            try {
                AssetManager assetManager = EHRLibRuntime.getInstance().getContext().getAssets();
                InputStream inputStream = assetManager.open(PROPERTIES_FILE_NAME);
                Properties properties = new Properties();
                properties.load(inputStream);

                instance = new PehrSDKConfiguration(properties);
            } catch (IOException e) {
                Log.e(PehrSDKConfiguration.class.getName(), "Cannot get instance of PehrSDKConfiguration");
                e.printStackTrace();
            }
        }

        return instance;
    }

    public static PehrSDKConfiguration getInstance(Properties properties) {
        if (instance == null) {
            instance = new PehrSDKConfiguration(properties);
        }

        return instance;
    }

    public static PehrSDKConfiguration getInstance(String appGuid, String appAlias, String appVersion, String stackKey) {
        if (instance == null) {
            Properties properties = new Properties();
            properties.setProperty(APP_GUID_KEY, appGuid);
            properties.setProperty(APP_ALIAS_KEY, appAlias);
            properties.setProperty(APP_VERSION_KEY, appVersion);
            properties.setProperty(APP_STACK_KEY_KEY, stackKey);
            instance = new PehrSDKConfiguration(properties);
        }

        return instance;
    }

    public String getAppGuid() {
        return properties.getProperty(APP_GUID_KEY);
    }

    public String getAppAlias() {
        return properties.getProperty(APP_ALIAS_KEY);
    }

    public IBVersion getAppVersion() {
        return new IBVersion(properties.getProperty(APP_VERSION_KEY));
    }

    public String getAppStackKey() {
        return properties.getProperty(APP_STACK_KEY_KEY);
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    private void savePushToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(kDevicePushToken, token);
        editor.commit();
    }

    private String getPushToken() {
        return sharedPreferences.getString(kDevicePushToken, "");
    }


    private void updatePushToken(String token) {
        if (token == null || token.equals(getPushToken())) {
            return; // no need to update when token is null or same as saved earlier
        }
        EHRApiServer server = EHRLibRuntime.getCurrentServer();
        EHRServerRequest request = new EHRServerRequest(server,
                AppState.getInstance().getUser(),
                AppState.getInstance().getDeviceInfo(),
                AppState.getInstance().getDeviceLanguage(),
                "/app/user/device",
                "setNotificationToken");
        request.setParameter("token", token);

        ICompletionHandler completionHandler = new ICompletionHandler() {
            @Override
            public void handleSuccess(ICaller theCall) {
                savePushToken(token);
            }

            @Override
            public void handleError(ICaller theCall) {
                Log.e("PehrSDKConfiguration", "handleError: for " + theCall);
            }

            @Override
            public void handleCancel(ICaller thCall) {
                Log.e("PehrSDKConfiguration", "handleCancel: " + thCall + " will pass to handle errlr.");
                handleError(thCall);
            }
        };

        UpdatePushTokenCall call = new UpdatePushTokenCall(request, completionHandler);
        call.callOnNewThread();
    }

}
