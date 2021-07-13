package com.portableehr.patientsdk.utils;

import java.util.HashSet;
import java.util.Set;

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
