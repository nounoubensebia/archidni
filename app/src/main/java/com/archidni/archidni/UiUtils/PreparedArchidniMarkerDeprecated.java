package com.archidni.archidni.UiUtils;

import com.mapbox.mapboxsdk.annotations.BaseMarkerOptions;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.MarkerViewOptions;

/**
 * Created by noure on 07/02/2018.
 */

public class PreparedArchidniMarkerDeprecated {
    private MarkerViewOptions markerViewOptions;
    private Object tag;

    public PreparedArchidniMarkerDeprecated(MarkerViewOptions markerViewOptions, Object tag) {
        this.markerViewOptions = markerViewOptions;
        this.tag = tag;
    }

    public PreparedArchidniMarkerDeprecated(MarkerViewOptions markerViewOptions) {
        this.markerViewOptions = markerViewOptions;
    }

    public MarkerViewOptions getMarkerViewOptions() {
        return markerViewOptions;
    }

    public Object getTag() {
        return tag;
    }
}
