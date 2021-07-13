package com.portableehr.sdk.network.enums;

import androidx.annotation.NonNull;

/**
 * Created by : yvesleborg
 * Date       : 2019-12-23
 * <p>
 * Copyright Portable Ehr Inc, 2019
 */

@SuppressWarnings("unused")
public enum IdentificationFactorEnum {
    EMAIL("email"),
    MOBILE("mobile"),
    VISUAL("visual");

    private final String text;


    IdentificationFactorEnum(String textValue) {
        this.text = textValue;
    }

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    @NonNull
    public String toString() {
        return text;
    }

}
