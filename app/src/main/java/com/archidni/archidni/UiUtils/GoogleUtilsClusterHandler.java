package com.archidni.archidni.UiUtils;

import android.content.Context;

import com.archidni.archidni.Model.Coordinate;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;

public class GoogleUtilsClusterHandler implements ClusterHandler {
    private ArchidniGoogleMap archidniGoogleMap;
    private Context context;
    private ArrayList<ClusterManager<ArchidniClusterItem>> clusterManagers;

    public GoogleUtilsClusterHandler(ArchidniGoogleMap archidniGoogleMap, Context context) {
        this.archidniGoogleMap = archidniGoogleMap;
        this.context = context;
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
                for (ClusterManager<ArchidniClusterItem> clusterManager:clusterManagers)
                {
                    clusterManager.onCameraIdle();
                }
            }
        });
        archidniGoogleMap.getMap().setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                for (ClusterManager<ArchidniClusterItem> clusterManager:clusterManagers)
                {
                    clusterManager.onMarkerClick(marker);
                }
                return true;
            }
        });
    }

    @Override
    public void addCluster(Context context, final OnClusterItemClickListener onClusterItemClickListener) {
        final ClusterManager<ArchidniClusterItem> clusterManager = new ClusterManager<>(context,archidniGoogleMap.getMap());
        clusterManager.setRenderer(new ArchidniClusterRenderer(context,archidniGoogleMap.getMap(),clusterManager));
        clusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<ArchidniClusterItem>() {
            @Override
            public boolean onClusterItemClick(ArchidniClusterItem archidniClusterItem) {
                ArchidniClusterRenderer archidniClusterRenderer = (ArchidniClusterRenderer)
                        clusterManager.getRenderer();
                onClusterItemClickListener.onClusterItemClick(archidniClusterItem,
                        archidniClusterRenderer.getMarker(archidniClusterItem));
                return true;
            }
        });
        clusterManagers.add(clusterManager);
    }

    @Override
    public void renderClusters() {
        for (ClusterManager<ArchidniClusterItem> clusterManager:clusterManagers)
        {
            clusterManager.cluster();
        }
    }

    @Override
    public void prepareClusterItem(Coordinate coordinate, int drawable, int clusterId, Object tag) {
        ArchidniClusterItem archidniClusterItem = new ArchidniClusterItem(coordinate,drawable,tag);
        clusterManagers.get(clusterId).addItem(archidniClusterItem);
        ArchidniClusterRenderer archidniClusterRenderer = (ArchidniClusterRenderer)
                clusterManagers.get(clusterId).getRenderer();
        archidniClusterRenderer.getArchidniClusterItems().add(archidniClusterItem);
    }

    @Override
    public void removeAllClusterItems(int clusterId) {
        ClusterManager<ArchidniClusterItem> clusterManager = clusterManagers.get(clusterId);
        ArchidniClusterRenderer archidniClusterRenderer = (ArchidniClusterRenderer) clusterManager.getRenderer();
        archidniClusterRenderer.getMarkerOptionsHashMap().clear();
        archidniClusterRenderer.getArchidniClusterItems().clear();
        clusterManager.clearItems();
    }
}
