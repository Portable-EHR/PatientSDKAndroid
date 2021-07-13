package com.portableehr.patientsdk.utils;

import java.util.UUID;

public class EHRGuid {

    public static String guid(){
        return UUID.randomUUID().toString();
    }
    public static String uuid(){
        return UUID.randomUUID().toString();
    }

}
