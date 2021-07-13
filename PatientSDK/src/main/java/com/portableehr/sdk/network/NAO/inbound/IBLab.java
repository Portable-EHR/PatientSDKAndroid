package com.portableehr.sdk.network.NAO.inbound;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.portableehr.sdk.network.gson.GsonFactory;

import java.net.URL;

/**
 * Created by : yvesleborg
 * Date       : 2017-04-03
 *
 * Copyright Portable Ehr Inc, 2019
 */

public class IBLab {
    private IBAddress address;
    private String name;
    private URL url;
    private String dayPhone;
    private String guid;

    public IBAddress getAddress() {
        return address;
    }

    public void setAddress(IBAddress address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public String getDayPhone() {
        return dayPhone;
    }

    public void setDayPhone(String dayPhone) {
        this.dayPhone = dayPhone;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    //*********************************************************************************************/
    //** GSON helper                                                                             **/
    //*********************************************************************************************/

    public String asJson() {
        GsonBuilder builder        = GsonFactory.standardBuilder();
        Gson        jsonSerializer = builder.create();
        String      theJson        = jsonSerializer.toJson(this, this.getClass());
        return theJson;
    }

    public static IBLab fromJson(String json) {
        GsonBuilder builder          = GsonFactory.standardBuilder();
        Gson        jsonDeserializer = builder.create();
        IBLab       theObject        = jsonDeserializer.fromJson(json, IBLab.class);
        return theObject;
    }
}
