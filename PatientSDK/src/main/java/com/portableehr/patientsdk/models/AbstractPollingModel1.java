package com.portableehr.patientsdk.models;

import android.annotation.SuppressLint;
import android.util.Log;

import com.portableehr.patientsdk.state.AppState;
import com.portableehr.sdk.models.ModelRefreshPolicyEnum;
import com.portableehr.sdk.network.gson.GSONexcludeOutbound;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public abstract class AbstractPollingModel1 {

    @GSONexcludeOutbound
    protected ModelRefreshPolicyEnum pollingPolicy = ModelRefreshPolicyEnum.NONE;
    @GSONexcludeOutbound
    private   boolean                isPerformingPollAction;
    @GSONexcludeOutbound
    private   boolean                isPerformingPollActionCancel;

    @GSONexcludeOutbound
    private long pollActionNumber;

    @GSONexcludeOutbound
    private              Date    mStartTime;
    @GSONexcludeOutbound
    private              Date    mTimeOfPreviousPoll;
    @GSONexcludeOutbound
    private              Date    mTimeOfNextPoll;
    @GSONexcludeOutbound
    private              boolean pollImmediate;
    @GSONexcludeOutbound
    private              long[]  intervalDurationsInSeconds;
    @GSONexcludeOutbound
    private              int     currentIntervalDurationIndex;
    @GSONexcludeOutbound
    private static final int     MAX_INTERVAL_DURATION_INDEX = 8;

    protected AbstractPollingModel1() {
        pollingPolicy = ModelRefreshPolicyEnum.NONE;
        intervalDurationsInSeconds = new long[MAX_INTERVAL_DURATION_INDEX + 1];
        for (int index = 0; index <= MAX_INTERVAL_DURATION_INDEX; index++) {
            intervalDurationsInSeconds[index] = getIntervalInSeconds(index);
        }
        currentIntervalDurationIndex = 0;

        // if(getVerbose()) Log.d(getLogTAG(), "AbstractPollingModel() called " + Arrays.toString(intervalDurationsInSeconds));

    }

    /**
     * Spreads poll interval from smallest to largest. After MAX_INTERVAL_DURATION_INDEX polls,
     * we should be ge polling slowly.  If any keep alive event is triggered, the index is set to 0
     * (smallest) to keep display responsive.
     *
     * @param intervalNumber from 0 t0 MAX_INTERVAL_DURATION_INDEX
     * @return the actual interval duration in seconds
     */
    private long getIntervalInSeconds(int intervalNumber) {
        long result = getSmallestPollIntervalInSeconds();
        long steps  = (getLargestPollingIntervalInSeconds() - getSmallestPollIntervalInSeconds()) / 32;
        switch (intervalNumber) {
            case 0:
            case 1:
            case 2:
                result = getSmallestPollIntervalInSeconds();
                break;
            case 3:
                result = getSmallestPollIntervalInSeconds() + steps;
                break;
            case 4:
                result = getSmallestPollIntervalInSeconds() + 2 * steps;
                break;
            case 5:
                result = getSmallestPollIntervalInSeconds() + 4 * steps;
                break;
            case 6:
                result = getSmallestPollIntervalInSeconds() + 8 * steps;
                break;
            case 7:
                result = getSmallestPollIntervalInSeconds() + 16 * steps;
                break;
            case 8:
                result = getLargestPollingIntervalInSeconds();
                break;
        }
        return Math.min(getLargestPollingIntervalInSeconds(), result);
    }

    public ModelRefreshPolicyEnum getPollingPolicy() {
        return pollingPolicy;
    }

    public void setPollingPolicy(ModelRefreshPolicyEnum rp) {
        implementPolicy(rp);
    }

    //region private getters/setters


    public boolean isPollImmediate() {
        return pollImmediate;
    }

    public boolean isPollScheduled() {
        return !isPollImmediate();
    }

    public void setPollImmediate(boolean mPollImmediate) {
        this.pollImmediate = mPollImmediate;
    }

    //endregion

    //region public business
    public void pollNow() {
        if (isPerformingPollAction) {
            // if(getVerbose()) Log.d(getLogTAG(), "pollNow() invoked while polling, will ignore the request");
            return;
        }
        cancelPoll(); // immediate cancel
        // if(getVerbose()) Log.d(getLogTAG(), "pollNow() called, scheduling in 100 ms.");
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                currentIntervalDurationIndex = 0;
                setPollImmediate(true);
                slingshotAfter(0);
            }
        }, 100);
    }
    //endregion

    public final boolean isPolling() {
        return getPollingPolicy() == ModelRefreshPolicyEnum.NONE;
    }

    public void keepPollingAlive() {
        if (getVerbose()) {
            Log.d(getLogTAG(), "keepPollingAlive() called");
        }
        if (getPollingPolicy() == ModelRefreshPolicyEnum.NONE) {
            return;
        }
        if (isPerformingPollAction) {
            if (isPerformingPollAction) {
                if (getVerbose()) {
                    Log.d(getLogTAG(), "keepPollingAlive() invoked while polling, will ignore the request");
                }
                return;
            }
        }
        currentIntervalDurationIndex = 0;
        if (secondsUntilNextPoll() > getSmallestPollIntervalInSeconds()) {
            if (getVerbose()) {
                Log.d(getLogTAG(), "keepPollingAlive() invoked while polling, will ignore the request");
            }
            cancelPoll();
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    currentIntervalDurationIndex = 0;
                    setPollImmediate(true);
                    slingshotAfter(0);
                }
            }, 100);
        } else {
            if (getVerbose()) {
                Log.d(getLogTAG(), "keepPollingAlive() invoked too close to next poll, ignored");
            }
        }
    }


    private void performOnePoll() {

        if (getVerbose()) {
            Log.d(getLogTAG(), "performOnePoll() :  called at " + at());
        }

        if (isPerformingPollAction) {
            if (getVerbose()) {
                Log.d(getLogTAG(), "performOnePoll() : invoked during a pollAction , skipped. " + at());
            }
            return;
        }
        pollActionNumber++;
        if (getVerbose()) {
            Log.d(getLogTAG(), "performOnePoll() : poll number " + pollActionNumber + at());
        }
        setPerformingPollAction(true);
        currentIntervalDurationIndex = Math.min(MAX_INTERVAL_DURATION_INDEX, currentIntervalDurationIndex + 1);
        onPollActionStart();
        implementPollAction();
    }

    public final void cancelPoll() {
        if (getVerbose()) {
            Log.d(getLogTAG(), "cancelPoll() called : poll number " + pollActionNumber + at());
        }
        stopPolling();
    }

    protected final void signalPollActionComplete() {
        if (getVerbose()) {
            Log.d(getLogTAG(), "signalPollActionComplete() called, poll number " + pollActionNumber + at());
        }
        setPerformingPollAction(false);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                slingshot();
            }
        }, 100);
        mTimeOfPreviousPoll = new Date();
    }

    protected final void signalPollActionCancelComplete() {
        if (getVerbose()) {
            Log.d(getLogTAG(), "signalPollActionCancelComplete() called, poll number " + pollActionNumber + at());
        }
        setPerformingPollAction(false);
        isPerformingPollActionCancel = false;
        nukeCurrentSlingshot();
    }

    protected abstract void implementPollAction();

    protected abstract void implementPollActionCancel();

    protected abstract long getSmallestPollIntervalInSeconds();

    protected abstract long getLargestPollingIntervalInSeconds();

    protected abstract void onPollActionStart();

    protected abstract void onPollActionCancelStart();

    protected abstract String getLogTAG();

    protected abstract boolean getVerbose();

    //region private getters/setters, mostly for debugging

    public boolean isPerformingPollAction() {
        return isPerformingPollAction;
    }

    public void setPerformingPollAction(boolean performingPollAction) {
        if (performingPollAction) {
            if (getVerbose()) {
                Log.d(getLogTAG(), "setPerformingPollAction() called with: performingPollAction = [" + performingPollAction + "], poll no " + pollActionNumber + at());
            }
        } else {
            if (getVerbose()) {
                Log.d(getLogTAG(), "setPerformingPollAction() called with: performingPollAction = [" + performingPollAction + "], poll no " + pollActionNumber + at());
            }
        }
        isPerformingPollAction = performingPollAction;
    }

    //endregion

    //region Polling control, private

    private void implementPolicy(ModelRefreshPolicyEnum pollingPolicy) {
        String TAG = this.getClass().getSimpleName();
        if (getVerbose()) {
            Log.d(getLogTAG(), "implementPolicy : from " + getPollingPolicy() + " to " + pollingPolicy + at());
        }

        switch (pollingPolicy) {
            case ADAPTATIVE:
                implementAdaptivePollingPolicy();
                break;
            case LONG_POLL:
                implementLongPollPolicy();
                break;
            case NONE:
                implementNoPollingPolicy();
                break;
        }

    }

    private void implementNoPollingPolicy() {
        stopPolling();
        this.currentIntervalDurationIndex = MAX_INTERVAL_DURATION_INDEX;
        this.pollingPolicy = ModelRefreshPolicyEnum.NONE;
    }


    private void implementLongPollPolicy() {


        switch (getPollingPolicy()) {
            case ADAPTATIVE:
                this.pollingPolicy = ModelRefreshPolicyEnum.LONG_POLL;
                currentIntervalDurationIndex = MAX_INTERVAL_DURATION_INDEX;
                if (!isPerformingPollAction) {
                    stopPolling(); // this may take a while, say 100 ms
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            slingshot();
                        }
                    }, 100);
                }
                break;
            case LONG_POLL:
                this.pollingPolicy = ModelRefreshPolicyEnum.LONG_POLL;
                break;
            case NONE:
                this.pollingPolicy = ModelRefreshPolicyEnum.LONG_POLL;
                currentIntervalDurationIndex = MAX_INTERVAL_DURATION_INDEX;
                slingshot();
                break;
        }
    }

    private void implementAdaptivePollingPolicy() {

        switch (getPollingPolicy()) {
            case ADAPTATIVE:
                if (!isPolling()) {
                    if (getVerbose()) {
                        Log.d(getLogTAG(), "implementAdaptivePolingPolicy : Resuming ADAPTIVE polling, reseting to SHORT before decay." + at());
                    }
                    keepPollingAlive();
                }
                break;

            case LONG_POLL:
                currentIntervalDurationIndex = 0;
                this.pollingPolicy = ModelRefreshPolicyEnum.ADAPTATIVE;
                if (!isPerformingPollAction) {
                    stopPolling();
                }
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        slingshot();
                    }
                }, 100);

            case NONE:
                if (getVerbose()) {
                    Log.d(getLogTAG(), "implementAdaptivePolingPolicy : Terminating NONE policy, starting ADAPTIVE" + at());
                }
                this.pollingPolicy = ModelRefreshPolicyEnum.ADAPTATIVE;
                currentIntervalDurationIndex = 0;
                slingshotAfter(0);
                break;
        }
        this.pollingPolicy = ModelRefreshPolicyEnum.ADAPTATIVE;
    }

    private void stopPolling() {
        if (isPerformingPollActionCancel) {
            if (getVerbose()) {
                Log.d(getLogTAG(), "stopPolling() : directive received while a stop order is already in progress, skipped." + at());
            }
        } else if (isPerformingPollAction) {
            isPerformingPollActionCancel = true;
            onPollActionCancelStart();
            implementPollActionCancel();
        }
    }

    @GSONexcludeOutbound
    Timer     mSlingshotTimer;
    @GSONexcludeOutbound
    TimerTask mSlingshotTimerTask;

    private void slingshot() {

        if (getPollingPolicy() == ModelRefreshPolicyEnum.NONE) {
            if (getVerbose()) {
                Log.d(getLogTAG(), "slingshot() : invoked when polling policy is NONE, skipped." + at());
            }
            return;
        }

        if (isPerformingPollAction) {
            if (getVerbose()) {
                Log.d(getLogTAG(), "slingshot() : invoked when performing a polling action, skipped." + at());
            }
            return;
        }

        if (isPerformingPollActionCancel) {
            if (getVerbose()) {
                Log.d(getLogTAG(), "slingshot() : invoked when cancelling a polling action, skipped." + at());
            }
            return;
        }

        long delay = intervalDurationsInSeconds[currentIntervalDurationIndex];
        slingshotAfter(delay);

    }

    private void nukeCurrentSlingshot() {
        if (mSlingshotTimerTask != null) {
            mSlingshotTimerTask.cancel();
            mSlingshotTimerTask = null;
        }
        if (mSlingshotTimer != null) {
            mSlingshotTimer.cancel();
            mSlingshotTimer = null;
        }
    }

    private boolean haveAslingshot() {
        return mSlingshotTimer != null || mSlingshotTimerTask != null;
    }

    private void slingshotAfter(long delayInSeconds) {

        if (getVerbose()) {
            Log.d(getLogTAG(), "slingshotAfter() called with: delayInSeconds = [" + delayInSeconds + "]" + at());
        }

        if (haveAslingshot()) {
            nukeCurrentSlingshot();
        }

        mSlingshotTimer = new Timer();
        final Runnable theRunner = new Runnable() {
            @Override
            public void run() {
                if (getVerbose()) {
                    Log.d(getLogTAG(), "slingShotAfter()->run  called at " + at());
                }
                performOnePoll();
            }
        };
        mSlingshotTimerTask = new TimerTask() {
            @Override
            public void run() {
                theRunner.run();
            }
        };
        mTimeOfNextPoll = new Date(new Date().getTime() + delayInSeconds * 1000);
        if (getVerbose()) {
            Log.d(getLogTAG(), "Scheduling next poll at " + at(mTimeOfNextPoll));
        }
        mSlingshotTimer.schedule(mSlingshotTimerTask, delayInSeconds * 1000);
    }

    private long secondsUntilNextPoll() {
        if (mTimeOfNextPoll == null) {
            return Long.MAX_VALUE;
        }
        Date now = new Date();
        if (now.getTime() > mTimeOfNextPoll.getTime()) {
            return 0;
        }
        return (mTimeOfNextPoll.getTime() - now.getTime()) / 1000;
    }

    private long secondsSinceLastPoll() {
        Date now = new Date();
        if (null == mTimeOfPreviousPoll) {
            return Long.MAX_VALUE;
        }
        if (now.getTime() < mTimeOfPreviousPoll.getTime()) {
            return 0;
        }
        return (now.getTime() - mTimeOfPreviousPoll.getTime()) / 1000;
    }

    @SuppressLint("DefaultLocale")
    protected String at() {
        if (null == mStartTime) {
            mStartTime = AppState.getInstance().getStartTime();
        }
        float delta = (float) (new Date().getTime() - mStartTime.getTime()) / 1000.0f;
        return " at " + String.format("%.3f", delta);
    }

    @SuppressLint("DefaultLocale")
    protected String at(Date future) {
        if (null == mStartTime) {
            mStartTime = AppState.getInstance().getStartTime();
        }
        float delta = (float) (future.getTime() - mStartTime.getTime()) / 1000.0f;
        return " at " + String.format("%.3f", delta);
    }

}
