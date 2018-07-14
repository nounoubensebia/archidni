package com.archidni.archidni.UiUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.archidni.archidni.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

import java.util.ArrayList;
import java.util.HashMap;



public class ArchidniClusterRenderer extends DefaultClusterRenderer<ArchidniClusterItem> {

    private HashMap<ArchidniClusterItem,Marker> markerOptionsHashMap;
    private ArrayList<ArchidniClusterItem> archidniClusterItems;
    private BitmapDescriptor clusterIcon;

    public ArchidniClusterRenderer(Context context, GoogleMap map, ClusterManager<ArchidniClusterItem> clusterManager) {
        super(context, map, clusterManager);
        markerOptionsHashMap = new HashMap<>();
        archidniClusterItems = new ArrayList<>();
    }

    public ArrayList<ArchidniClusterItem> getArchidniClusterItems() {
        return archidniClusterItems;
    }

    @Override
    protected void onBeforeClusterItemRendered(ArchidniClusterItem item, MarkerOptions markerOptions) {
        markerOptions.icon(ArchidniGoogleMap.getBitmapDescriptor(item.getDrawable()));
    }

    @Override
    protected void onBeforeClusterRendered(Cluster<ArchidniClusterItem> cluster, MarkerOptions markerOptions) {
        super.onBeforeClusterRendered(cluster, markerOptions);
        /*final Drawable clusterIcon = getApplicationContext().getResources().getDrawable(R.drawable.marker_selected);
        //clusterIcon.setColorFilter(getApplicationContext().getResources().getColor(android.R.color.holo_orange_light), PorterDuff.Mode.SRC_ATOP);

        mClusterIconGenerator.setBackground(clusterIcon);
        Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));*/
        //iconGenerator.setTextAppearance(android.R.style.TextAppearance_DeviceDefault);
        /*if (cluster.getSize() < 10) {
            iconGenerator.setContentPadding(40, 20, 0, 0);
        }
        else {
            iconGenerator.setContentPadding(30, 20, 0, 0);
        }
        Bitmap icon = iconGenerator.makeIcon(String.valueOf(cluster.getSize()));
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));*/
    }

    @Override
    protected void onClusterItemRendered(ArchidniClusterItem clusterItem, Marker marker) {
        super.onClusterItemRendered(clusterItem, marker);
        marker.setTag(clusterItem.getTag());
        markerOptionsHashMap.put(clusterItem,marker);
    }



    public HashMap<ArchidniClusterItem, Marker> getMarkerOptionsHashMap() {
        return markerOptionsHashMap;
    }
}
