package com.archidni.archidni.UiUtils;

import com.archidni.archidni.Model.Coordinate;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class ArchidniClusterItem implements ClusterItem, net.sharewire.googlemapsclustering.ClusterItem {

    private Coordinate coordinate;
    private int drawable;
    private Object tag;

    public ArchidniClusterItem(Coordinate coordinate, int drawable, Object tag) {
        this.coordinate = coordinate;
        this.drawable = drawable;
        this.tag = tag;
    }

    public int getDrawable() {
        return drawable;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public Object getTag() {
        return tag;
    }

    @Override
    public LatLng getPosition() {
        return new LatLng(coordinate.getLatitude(),coordinate.getLongitude());
    }

    @Override
    public double getLatitude() {
        return this.getCoordinate().getLatitude();
    }

    @Override
    public double getLongitude() {
        return this.getCoordinate().getLongitude();
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
