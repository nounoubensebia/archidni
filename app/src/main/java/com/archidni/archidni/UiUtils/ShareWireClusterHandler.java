package com.archidni.archidni.UiUtils;

import android.content.Context;
import android.support.annotation.NonNull;

import com.archidni.archidni.Model.Coordinate;
import com.archidni.clusteringlibrary.googlemapsclustering.Cluster;
import com.archidni.clusteringlibrary.googlemapsclustering.ClusterItem;
import com.archidni.clusteringlibrary.googlemapsclustering.ClusterManager;
import com.archidni.clusteringlibrary.googlemapsclustering.DefaultIconGenerator;
import com.archidni.clusteringlibrary.googlemapsclustering.IconGenerator;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.Marker;




import java.util.ArrayList;

public class ShareWireClusterHandler implements ClusterHandler  {
    private Context context;
    private ArchidniGoogleMap archidniGoogleMap;
    private ArrayList<ClusterManager> clusterManagers;
    private ArrayList<ArrayList<ClusterItem>> clusterItems;

    public ShareWireClusterHandler(Context context, ArchidniGoogleMap archidniGoogleMap) {
        this.context = context;
        this.archidniGoogleMap = archidniGoogleMap;
        this.clusterItems = new ArrayList<>();
        this.clusterManagers = new ArrayList<>();
    }

    @Override
    public void initClusters() {
        archidniGoogleMap.getMap().setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                ArchidniGoogleMap.OnCameraIdle onCameraIdle = archidniGoogleMap.getOnCameraIdle();
                onCameraIdle.onCameraIdle(archidniGoogleMap.getCenter(),
                        archidniGoogleMap.getMap().getProjection().getVisibleRegion().latLngBounds,
                        archidniGoogleMap.getMap().getCameraPosition().zoom);
                for (ClusterManager clusterManager:clusterManagers)
                {
                    clusterManager.onCameraIdle();
                }
            }
        });
    }

    @Override
    public void addCluster(Context context, final OnClusterItemClickListener onClusterItemClickListener) {
        ClusterManager clusterManager = new ClusterManager(context,archidniGoogleMap.getMap());
        clusterManager.setCallbacks(new ClusterManager.Callbacks() {
            @Override
            public boolean onClusterClick(@NonNull Cluster cluster, Marker marker) {
                return false;
            }

            @Override
            public boolean onClusterItemClick(@NonNull ClusterItem clusterItem, Marker marker) {
                onClusterItemClickListener.onClusterItemClick((ArchidniClusterItem)clusterItem,marker);
                return true;
            }
        });
        clusterManagers.add(clusterManager);
        clusterItems.add(new ArrayList<ClusterItem>());
    }

    @Override
    public void renderClusters() {
        int i = 0;
        for (ClusterManager clusterManager:clusterManagers)
        {
            clusterManager.setItems(clusterItems.get(i));
            i++;
        }
    }

    @Override
    public void prepareClusterItem(Coordinate coordinate, final int drawable, int clusterId, Object tag) {
        ArchidniClusterItem archidniClusterItem = new ArchidniClusterItem(coordinate,drawable,tag);
        clusterItems.get(clusterId).add(archidniClusterItem);
        clusterManagers.get(clusterId).setIconGenerator(new IconGenerator() {
            @NonNull
            @Override
            public BitmapDescriptor getClusterIcon(@NonNull Cluster cluster) {
                return new DefaultIconGenerator<>(context).getClusterIcon(cluster);
            }

            @NonNull
            @Override
            public BitmapDescriptor getClusterItemIcon(@NonNull ClusterItem clusterItem) {
                return ArchidniGoogleMap.getBitmapDescriptor(drawable);
            }
        });
    }

    @Override
    public void removeAllClusterItems(int clusterId) {
        clusterItems.get(clusterId).clear();
        clusterManagers.get(clusterId).setItems(clusterItems.get(clusterId));
    }
}
