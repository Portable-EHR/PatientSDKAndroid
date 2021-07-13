package com.portableehr.sdk.util;

import java.util.Random;

/**
 * Created by : yvesleborg
 * Date       : 2019-11-02
 * <p>
 * Copyright Portable Ehr Inc, 2019
 */
public class MathUtils {

    public static int getRandomNumberInRange(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

}
