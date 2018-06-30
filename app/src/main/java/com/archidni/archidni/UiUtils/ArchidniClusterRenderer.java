package com.archidni.archidni.UiUtils;

import android.content.Context;
import android.widget.ImageView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

import java.util.ArrayList;
import java.util.HashMap;

import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

public class ArchidniClusterRenderer extends DefaultClusterRenderer<ArchidniClusterItem> {

    private HashMap<ArchidniClusterItem,Marker> markerOptionsHashMap;

    public ArchidniClusterRenderer(Context context, GoogleMap map, ClusterManager<ArchidniClusterItem> clusterManager) {
        super(context, map, clusterManager);
        markerOptionsHashMap = new HashMap<>();
    }

    @Override
    protected void onBeforeClusterItemRendered(ArchidniClusterItem item, MarkerOptions markerOptions) {
        markerOptions.icon(ArchidniGoogleMap.getBitmapDescriptor(item.getDrawable()));
    }

    @Override
    protected void onClusterItemRendered(ArchidniClusterItem clusterItem, Marker marker) {
        super.onClusterItemRendered(clusterItem, marker);
        markerOptionsHashMap.put(clusterItem,marker);
    }

    public HashMap<ArchidniClusterItem, Marker> getMarkerOptionsHashMap() {
        return markerOptionsHashMap;
    }
}
