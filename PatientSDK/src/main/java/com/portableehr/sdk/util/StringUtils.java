package com.portableehr.sdk.util;

import androidx.annotation.Nullable;

import android.text.TextUtils;
import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;

/**
 * Created by : yvesleborg
 * Date       : 2019-11-02
 * <p>
 * Copyright Portable Ehr Inc, 2019
 */
public class StringUtils {
    public static String padLeft(String inputString, int length, @Nullable String padding) {
        if (null == padding) {
            padding = " ";
        }
        if (inputString.length() >= length) {
            return inputString;
        }
        StringBuilder sb = new StringBuilder();
        while (sb.length() < length - inputString.length()) {
            sb.append(padding);
        }
        sb.append(inputString);

        return sb.toString();
    }

    public static String padLeftSpaces(String inputString, int length) {
        return StringUtils.padLeft(inputString, length, " ");
    }

    public static String padLeftSpaces(int inputNumber, int length) {
        String inputString = Integer.toString(inputNumber);
        return StringUtils.padLeftSpaces(inputString, length);
    }

    public static String padLeft(int inputNumber, int length) {
        String inputString = Integer.toString(inputNumber);
        return StringUtils.padLeft(inputString, length, " ");
    }

    public static String obfuscateEmail(String email) {
        String result = "email@example.com";
        if (TextUtils.isEmpty(email)) {
            return "**";
        }
        try {
            int domainIdx = email.lastIndexOf("@");
            String domain;
            String tld;
            domain = email.substring(domainIdx + 1);
            int tldIdx = domain.lastIndexOf(".");
            tld = domain.substring(tldIdx + 1);

            result = "." + tld;
            String obDomain = domain.substring(0, tldIdx);

            if (obDomain.length() < 5) {
                obDomain = StringUtils.fillerString(obDomain.length(), "*");
            } else {
                obDomain = StringUtils.obfuscate(obDomain, 2, 2);
            }
            result = "@" + obDomain + result;
            String addressee = email.substring(0, domainIdx);
            String obAdressee;
            int adresseeLen = addressee.length();
            if (adresseeLen <= 4) {
                obAdressee = StringUtils.fillerString(adresseeLen, "*");
            } else {
                int keeper = 2;
                if (adresseeLen > 8) {
                    keeper = 3;
                }
                obAdressee = StringUtils.obfuscate(addressee, keeper, keeper);
            }

            result = obAdressee + result;

        } catch (Exception e) {
            Log.e("StringUtils", "obfuscateEmail: ", e);
        }
        return result;
    }

    public static String obfuscatePhome(String phone) {
//        int len = phone.length();
        return phone;
    }

    public static String lastDigits(String strawman, int howMany) {
        if (strawman.length() < howMany) {
            throw new IllegalArgumentException("Cant extract the required number of digits, string too short.");
        }
        return strawman.substring(strawman.length() - howMany);
    }

    public static String firstDigits(String strawman, int howMany) {
        if (strawman.length() < howMany) {
            throw new IllegalArgumentException("Cant extract the required number of digits, string too short.");
        }
        return strawman.substring(0, howMany);
    }

    public static String fillerString(int size, String filler) {
        if (size < 0 || filler.length() > 1) {
            throw new IllegalArgumentException();
        }
        char[] chars = new char[size];
        Arrays.fill(chars, filler.charAt(0));
        String s = new String(chars);
        return s;
    }


    private static String obfuscate(String strawman, int keepFirst, int keepLast) {
        if (keepFirst < 0 || keepLast < 0 || keepFirst > strawman.length() || keepLast > strawman.length()) {
            throw new IllegalArgumentException("Unable to obfuscate string with given spec");
        }

        String filler = "";
        int size = strawman.length() - (keepFirst + keepLast);
        if (size > 0) {
            filler = StringUtils.fillerString(size, "*");
        }
        return StringUtils.firstDigits(strawman, keepFirst) + filler + StringUtils.lastDigits(strawman, keepLast);


    }

    /**
     * This assumes that we have leading zeros, and nothing else (for ex: a seq coming from backend)
     *
     * @param source 000...0SMNELSE
     * @param keep   min return string length
     * @return the trimmed source, upto keep length
     */
    public static String trimLeadingZeros(String source, int keep) {
        if (source.length() <= keep) {
            return source;
        }
        String ret = source;
        for (int i = 0; i < source.length(); ++i) {
            char c = source.charAt(i);
            if (c == '0') {
                ret = source.substring(i);
                if (ret.length() <= keep) {
                    break;
                }
            } else {
                break;
            }
        }
        return ret;
    }


    public static String getStackTrace(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return pw.toString();
    }

}
