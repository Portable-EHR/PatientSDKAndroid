package com.portableehr.sdk;

import android.content.res.AssetManager;
import android.util.Log;

import com.portableehr.sdk.network.NAO.inbound.IBVersion;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PehrSDKConfiguration {

    private static PehrSDKConfiguration instance = null;

    private Properties properties = null;

    public static final String PROPERTIES_FILE_NAME = "pehr-sdk.properties";
    public static final String APP_GUID_KEY = "pehr.sdk.appGuid";
    public static final String APP_ALIAS_KEY = "pehr.sdk.appAlias";
    public static final String APP_VERSION_KEY = "pehr.sdk.appVersion";
    public static final String APP_STACK_KEY_KEY = "pehr.sdk.stackKey";

    private PehrSDKConfiguration(Properties properties){
        this.properties = properties;
    }

    public static PehrSDKConfiguration getInstance(){
        if(instance == null){
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

    public static PehrSDKConfiguration getInstance(Properties properties){
        if(instance == null){
            instance = new PehrSDKConfiguration(properties);
        }

        return instance;
    }

    public static PehrSDKConfiguration getInstance(String appGuid, String appAlias, String appVersion, String stackKey){
        if(instance == null){
            Properties properties = new Properties();
            properties.setProperty(APP_GUID_KEY, appGuid);
            properties.setProperty(APP_ALIAS_KEY, appAlias);
            properties.setProperty(APP_VERSION_KEY, appVersion);
            properties.setProperty(APP_STACK_KEY_KEY, stackKey);
            instance = new PehrSDKConfiguration(properties);
        }

        return instance;
    }

    public String getAppGuid(){
        return properties.getProperty(APP_GUID_KEY);
    }

    public String getAppAlias(){
        return properties.getProperty(APP_ALIAS_KEY);
    }

    public IBVersion getAppVersion(){
        return new IBVersion(properties.getProperty(APP_VERSION_KEY));
    }

    public String getAppStackKey(){
        return properties.getProperty(APP_STACK_KEY_KEY);
    }
}
