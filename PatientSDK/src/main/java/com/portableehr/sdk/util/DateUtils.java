package com.portableehr.sdk.util;

import java.util.Date;

/**
 * Created by : yvesleborg
 * Date       : 2019-12-14
 * <p>
 * Copyright Portable Ehr Inc, 2019
 */
public class DateUtils {

    static int SECONDS_IN_A_MINUTE = 60;
    static int SECONDS_IN_A_HOUR   = 60 * 60;
    static int SECONDS_IN_A_DAY    = 24 * 60 * 60;
    static int SECONDS_IN_A_MONTH  = 30 * 24 * 60 * 60;
    static int SECONDS_IN_A_YEAR   = 365 * 24 * 60 * 60;

    static String en_abbrev_second = "s";
    static String en_abbrev_minute = "m";
    static String en_abbrev_hour   = "h";
    static String en_abbrev_day    = "d";
    static String en_abbrev_month  = "M";
    static String en_abbrev_year   = "y";

    static String fr_abbrev_second = "s";
    static String fr_abbrev_minute = "m";
    static String fr_abbrev_hour   = "h";
    static String fr_abbrev_day    = "j";
    static String fr_abbrev_month  = "M";
    static String fr_abbrev_year   = "a";


    public static String distanceFromDate(Date date, String lang, boolean decorate) {
        String  distance;
        Date    now           = new Date();
        long    nowmilli      = now.getTime();
        long    datemilli     = date.getTime();
        boolean inThePast     = (datemilli < nowmilli);
        long    deltamilli    = nowmilli - datemilli;
        long    deltaLong     = deltamilli / 1000;
        int     delta         = Math.abs((int) deltaLong);
        String  abbrev_second = en_abbrev_second;
        String  abbrev_minute = en_abbrev_minute;
        String  abbrev_hour   = en_abbrev_hour;
        String  abbrev_day    = en_abbrev_day;
        String  abbrev_month  = en_abbrev_month;
        String  abbrev_year   = en_abbrev_year;
        if (lang.contentEquals("fr")) {
            abbrev_second = fr_abbrev_second;
            abbrev_minute = fr_abbrev_minute;
            abbrev_hour = fr_abbrev_hour;
            abbrev_day = fr_abbrev_day;
            abbrev_month = fr_abbrev_month;
            abbrev_year = fr_abbrev_year;
        }

        if (delta < SECONDS_IN_A_MINUTE) {
            distance = delta + " " + abbrev_second;
        } else if (delta < SECONDS_IN_A_HOUR) {
            delta = delta / SECONDS_IN_A_MINUTE;
            distance = delta + " " + abbrev_minute;
        } else if (delta < SECONDS_IN_A_DAY) {
            delta = delta / SECONDS_IN_A_HOUR;
            distance = delta + " " + abbrev_hour;
        } else if (delta < SECONDS_IN_A_MONTH) {
            delta = delta / SECONDS_IN_A_DAY;
            distance = delta + " " + abbrev_day;
        } else if (delta < SECONDS_IN_A_YEAR) {
            delta = delta / SECONDS_IN_A_MONTH;
            distance = delta + " " + abbrev_month;
        } else {
            // years here
            delta = delta / SECONDS_IN_A_YEAR;
            distance = delta + " " + abbrev_year;
        }

        if (decorate) {
            if (inThePast) {
                if (lang.contentEquals("fr")) {
                    distance = "il y a " + distance;
                } else {
                    distance = distance + " ago";
                }
            } else {
                if (lang.contentEquals("fr")) {
                    distance = "dans " + distance;
                } else {
                    distance = "in " + distance;
                }
            }

        }

        return distance;
    }

    /**
     * lazy constructor, defaults en, and to decorate.
     * A decorated distance is "il y a 18 h"
     * A plain distance is "18 h"
     *
     * @param date The date from which we want distance (now - date)
     * @return String
     */
    public static String distanceFromDate(Date date) {
        return distanceFromDate(date, "en", true);
    }

}
