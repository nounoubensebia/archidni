package com.archidni.archidni.UiUtils;

import com.archidni.archidni.Model.Coordinate;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class ArchidniClusterItem implements ClusterItem {

    private Coordinate coordinate;
    private int drawable;

    public ArchidniClusterItem(Coordinate coordinate, int drawable) {
        this.coordinate = coordinate;
        this.drawable = drawable;
    }

    public int getDrawable() {
        return drawable;
    }

    @Override
    public LatLng getPosition() {
        return new LatLng(coordinate.getLatitude(),coordinate.getLongitude());
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getSnippet() {
        return null;
    }
}
