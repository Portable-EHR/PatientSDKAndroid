package com.portableehr.sdk.network;

import java.util.UUID;

/**
 * Created by : yvesleborg
 * Date       : 2017-12-28
 *
 * Copyright Portable Ehr Inc, 2019
 */


public class EHRGuid {
    public static String guid(){
        return UUID.randomUUID().toString();
    }
    public static String uuid(){
        return UUID.randomUUID().toString();
    }
}
