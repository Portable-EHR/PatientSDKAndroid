package com.portableehr.sdk.util;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by : yvesleborg
 * Date       : 2019-09-05
 * <p>
 * Copyright Portable Ehr Inc, 2019
 */
public class SetsAndStuff {

    /**
     * Utilisty method, clones a Set
     *
     * @param original the source Set
     * @param <T>      The type of set
     * @return cloned set
     */
    public synchronized static <T> Set<T> cloneKeySet(Set<T> original) {
        Set<T> copy = new HashSet<>(original);
        return copy;
    }

}
