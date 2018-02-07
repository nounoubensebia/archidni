package com.archidni.archidni.UiUtils;

import com.mapbox.mapboxsdk.annotations.BaseMarkerOptions;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.MarkerViewOptions;

/**
 * Created by noure on 07/02/2018.
 */

public class PreparedArchidniMarker {
    private MarkerOptions markerViewOptions;
    private Object tag;

    public PreparedArchidniMarker(MarkerOptions markerViewOptions, Object tag) {
        this.markerViewOptions = markerViewOptions;
        this.tag = tag;
    }

    public PreparedArchidniMarker(MarkerOptions markerViewOptions) {
        this.markerViewOptions = markerViewOptions;
    }

    public MarkerOptions getMarkerViewOptions() {
        return markerViewOptions;
    }

    public Object getTag() {
        return tag;
    }
}
