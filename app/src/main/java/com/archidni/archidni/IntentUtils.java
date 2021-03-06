package com.archidni.archidni;

/**
 * Created by noure on 03/02/2018.
 */

public class IntentUtils {
    public static final String LOCATION = "LOCATION";
    public static final String PATH_SEARCH_ORIGIN = "PATH_SEARCH_ORIGIN";
    public static final String PATH_SEARCH_DESTINATION = "PATH_SEARCH_DESTINATION";
    public static final String SET_LOCATION_COORDINATES = "SET_LOCATION_COORDINATES";
    public static final int RESULT_OK = 1;
    public static final String STATION_STATION = "STATION_STATION";
    public static final String LINE_LINE = "LINE_LINE";
    public static final String PATH = "INTENT_PATH";
    public static final String PARKING = "PARKING";
    public static final String INSTRUCTION_INDEX = "INSTRUCTION_INDEX";
    public static final String SCHEDULE = "SCHEDULE";
    public static final String DISRUPTION_SUBJECT = "DISRUPTION_SUBJECT";
    public static final String IS_PATH_GOOD = "IS_GOOD";
    public static final String USER_EMAIL = "USER_EMAIL";
    public static final String USER_PASSWORD = "USER_PASSWORD";

    public static class PathSearchIntents {

    }

    public static class SearchIntents {
        public static final String EXTRA_REQUEST_TYPE = "REQUEST_TYPE";
        public static final int TYPE_LOOK_ONLY_FOR_DESTINATION = 1;
        public static final int TYPE_LOOK_ONLY_FOR_ORIGIN = 2;
        public static final int TYPE_LOOK_FOR_DEST = 3;
        public static final int TYPE_LOOK_FOR_OR = 4;
    }
}
