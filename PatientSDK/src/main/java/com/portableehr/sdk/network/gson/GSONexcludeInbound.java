package com.portableehr.sdk.network.gson;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by : yvesleborg
 * Date       : 2017-03-31
 *
 * Copyright Portable Ehr Inc, 2019
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface GSONexcludeInbound {
}
