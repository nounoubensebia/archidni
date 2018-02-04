package com.archidni.archidni;

/**
 * Created by noure on 03/02/2018.
 */

public class IntentUtils {
    public static final String LOCATION = "LOCATION";
    public static final String ORIGIN = "ORIGIN";
    public static final String DESTINATION = "DESTINATION";

    public static class SearchIntents {
        public static final String EXTRA_REQUEST_TYPE = "REQUEST_TYPE";
        public static final int TYPE_LOOK_ONLY_FOR_DESTINATION = 1;
        public static final int TYPE_LOOK_ONLY_FOR_ORIGIN = 2;
        public static final int TYPE_LOOK_FOR_DEST = 3;
        public static final int TYPE_LOOK_FOR_OR = 4;
    }
}
