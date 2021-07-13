package com.portableehr.sdk;

/**
 * Created by : yvesleborg
 * Date       : 2020-07-22
 * <p>
 * Copyright Portable Ehr Inc, 2020
 */
public class RestCallOptions {
    public boolean onNewThread;
    public boolean verbose;
    public int     maxAttempts;
    public float   timeOutInSeconds;

    public RestCallOptions(boolean onNewThread, boolean verbose, int maxAttempts, float timeOutInSeconds) {
        this.onNewThread = onNewThread;
        this.verbose = verbose;
        this.maxAttempts = maxAttempts;
        this.timeOutInSeconds = timeOutInSeconds;
    }

    public static RestCallOptions defaults() {
        return new RestCallOptions(true, false, 1, 10.0f);
    }
}
