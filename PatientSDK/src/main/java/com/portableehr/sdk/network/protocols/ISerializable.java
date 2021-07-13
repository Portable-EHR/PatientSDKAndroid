package com.portableehr.sdk.network.protocols;

/**
 * Created by : yvesleborg
 * Date       : 2017-01-30
 * <p>
 * Copyright Portable Ehr Inc, 2019
 */

public interface ISerializable<T> {

    String asJson();

    T fromJson(String json);

}
