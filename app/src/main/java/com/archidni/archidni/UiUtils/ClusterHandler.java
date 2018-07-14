package com.archidni.archidni.UiUtils;

import android.content.Context;

import com.archidni.archidni.Model.Coordinate;
import com.google.android.gms.maps.model.Marker;

public interface ClusterHandler {

    public void initClusters ();

    public void addCluster (Context context,OnClusterItemClickListener onClusterItemClickListener);

    public void renderClusters ();

    public void prepareClusterItem (Coordinate coordinate,int drawable,int clusterId,Object tag);


    public interface OnClusterItemClickListener {
        void onClusterItemClick (ArchidniClusterItem archidniClusterItem,Marker marker);
    }
}


