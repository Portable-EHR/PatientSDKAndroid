package com.portableehr.sdk.network.ehrApi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.portableehr.sdk.EHRLibRuntime;
import com.portableehr.sdk.network.gson.GSONexcludeOutbound;

import java.net.URL;

import static com.portableehr.sdk.EHRLibRuntime.kModulePrefix;
import static com.portableehr.sdk.network.gson.GsonFactory.standardBuilder;

/**
 * Created by : yvesleborg
 * Date       : 2017-03-31
 *
 * Copyright Portable Ehr Inc, 2021
 */

public class EHRApiServer {

    private String scheme;
    private int port;
    private String serverDNSname;

    public EHRApiServer() {
        onNew();
        this.scheme = "http";
        this.serverDNSname = "api.crea-med.local";
        this.port = 80;
    }

    public static EHRApiServer create(String scheme, String serverDNSname, int port) {
        EHRApiServer s = new EHRApiServer();
        s.setScheme(scheme);
        s.setPort(port);
        s.setServerDNSname(serverDNSname);
        return s;
    }

    public static EHRApiServer defaultApiServer() {
        return EHRLibRuntime.getCurrentServer();
    }

    @SuppressWarnings("unused")
    public static EHRApiServer debugApiServer() {
        return EHRApiServer.create("http", "192.168.2.13", 8080);
    }

    public static EHRApiServer productionApiServer() {
        return EHRApiServer.create("https", "api.portableehr.ca", 443);
    }

    public static EHRApiServer partnerApiServer(){
        return EHRApiServer.create("https", "api.portableehr.io", 443);
    }

    public static EHRApiServer stagingApiServer() {
        return EHRApiServer.create("https", "api.portableehr.net", 443);
    }


    public String getScheme() {
        return this.scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getServerDNSname() {
        return serverDNSname;
    }

    public void setServerDNSname(String serverDNSname) {
        this.serverDNSname = serverDNSname;
    }

    public URL urlForRoute(String routePrefix) {
        String urlString = this.toString();
        if (null != routePrefix) urlString = urlString + routePrefix + "/";
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (Exception e) {
            Log.e(TAG, "Caught exception when creating URL : " + e.getMessage());
        }
        return url;
    }

    public String serverUrl(){
        return this.toString();
    }

    @SuppressWarnings("unused")
    public String fullyQualifiedURL(){
        return this.fullyQualifiedUrl(null,null);
    }
    @SuppressWarnings("unused")
    public String fullyQualifiedUrl(@Nullable String route){
        return this.fullyQualifiedUrl(route,null);
    }
    public String fullyQualifiedUrl(@Nullable String folder, @Nullable String querystring){

        String suffix = "/";
        String fqu = this.toString();
        if(null!=folder) {
            while (folder.startsWith("/")){folder=folder.substring(1);}
            if(folder.length()==-0) folder="/";
            suffix+=folder;
        }

        if(null!=querystring){
            while (querystring.startsWith("?")) querystring=querystring.substring(1);
            if(querystring.length()!=0) querystring="?"+querystring;
            suffix=suffix+querystring;
        }

        return fqu+suffix;
    }

    @NonNull
    @Override
    public String toString() {
        String noport = this.getScheme() + "://" + this.getServerDNSname();
        if(this.getPort()!=0) noport = noport +  ":" + this.getPort();
        return noport;
    }


    //region GSON helpers


    public String asJson() {
        GsonBuilder builder        = standardBuilder();
        Gson        jsonSerializer = builder.create();
        String      theJson        = jsonSerializer.toJson(this, this.getClass());
        return theJson;
    }

    public static EHRApiServer fromJson(String json) {
        GsonBuilder  builder          = standardBuilder();
        Gson         jsonDeserializer = builder.create();
        EHRApiServer theObject        = jsonDeserializer.fromJson(json, EHRApiServer.class);
        return theObject;
    }

    //endregion


    //region Countable

    private final static String  CLASSTAG       = kModulePrefix + "." + EHRApiServer.class.getSimpleName();
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

    public static  void setClassCountable( boolean isIt) {
        classCountable = isIt;
    }

    private String getLogTAG() {
        TAG = CLASSTAG + " [" + getLogLabel() + "] ";
        return TAG;
    }

    //endregion

}


