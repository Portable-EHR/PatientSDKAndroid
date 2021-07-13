package com.portableehr.sdk.network.protocols;

import androidx.annotation.Nullable;

/**
 * Created by : yvesleborg
 * Date       : 2017-04-04
 * <p>
 * Copyright Portable Ehr Inc, 2019
 */

public interface ICompletionHandler {

    void handleSuccess(@Nullable ICaller theCall);

    void handleError(@Nullable ICaller theCall);

    void handleCancel(@Nullable ICaller theCall);

}
