package com.archidni.archidni.UiUtils;

import com.mapbox.mapboxsdk.annotations.Marker;

/**
 * Created by nouno on 05/02/2018.
 */

public class ArchidniMarker {
    
    private Marker marker;
    private Object tag;

    public ArchidniMarker(Marker marker) {
        this.marker = marker;
    }

    public ArchidniMarker(Marker marker, Object tag) {
        this.marker = marker;
        this.tag = tag;
    }

    public Object getTag() {
        return tag;
    }

    public Marker getMarker() {
        return marker;
    }
}
