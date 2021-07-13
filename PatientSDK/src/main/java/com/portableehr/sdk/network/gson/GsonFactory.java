package com.portableehr.sdk.network.gson;

import com.google.gson.GsonBuilder;
import com.portableehr.sdk.network.NAO.inbound.IBVersion;

import java.lang.reflect.Modifier;

/**
 * Created by : yvesleborg
 * Date       : 2017-04-03
 * <p>
 * Copyright Portable Ehr Inc, 2019
 */

public class GsonFactory {

    public static GsonBuilder standardBuilder() {
        return new GsonBuilder()
                .setExclusionStrategies(
                        new GSONannotationInboundExclusionStrategy(),
                        new GSONannotationOutboundExclusionStrategy()
                )
                .excludeFieldsWithModifiers(
                        Modifier.STATIC,
                        Modifier.TRANSIENT,
                        Modifier.VOLATILE
                )
                .registerTypeAdapter(IBVersion.class, new IBVersionAdapter())
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .setPrettyPrinting();
    }
}
