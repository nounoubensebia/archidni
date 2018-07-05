package com.archidni.archidni.UiUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;

import com.archidni.archidni.App;
import com.archidni.archidni.Model.BoundingBox;
import com.archidni.archidni.Model.Coordinate;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

import java.util.ArrayList;

public class ArchidniGoogleMap {
    private GoogleMap map;
    private MapFragment mapFragment;
    private boolean mapLoaded = false;
    private ArrayList<ClusterManager<ArchidniClusterItem>> clusterManagers;
    private ArchidniClusterRenderer archidniClusterRenderer;
    private OnCameraIdle onCameraIdle;
    private ArrayList<ArchidniClusterItem> archidniClusterItems;

    public ArchidniGoogleMap(final MapFragment mapFragment, final OnMapReadyCallback onMapReadyCallback) {
        clusterManagers = new ArrayList<>();
        archidniClusterItems = new ArrayList<>();
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                map.getUiSettings().setCompassEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                    @Override
                    public void onMapLoaded() {
                        mapLoaded = true;
                    }
                });
                onMapReadyCallback.onMapReady(googleMap);
                map.setTrafficEnabled(true);
            }
        });
    }

    public void setOnCameraIdle(OnCameraIdle onCameraIdle) {
        this.onCameraIdle = onCameraIdle;
    }

    public void addCluster (Context context, IconGenerator iconGenerator)
    {
        ClusterManager<ArchidniClusterItem> clusterManager = new ClusterManager<>(context,map);
        clusterManager.setRenderer(new ArchidniClusterRenderer(context,map,clusterManager,iconGenerator));
        clusterManagers.add(clusterManager);
    }

    public void addCluster (Context context, IconGenerator iconGenerator, final OnClusterItemClickListener onClusterItemClickListener)
    {
        final ClusterManager<ArchidniClusterItem> clusterManager = new ClusterManager<>(context,map);
        clusterManager.setRenderer(new ArchidniClusterRenderer(context,map,clusterManager,iconGenerator));
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

    public void setOnCameraMoveListener (final OnCameraMoveListener onCameraMoveListener)
    {
        map.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                onCameraMoveListener.onCameraMove(getCenter(),getBoundingBox(),map.getCameraPosition().zoom);
            }
        });
    }

    /*public void createClusters (Context context, final OnClusterItemClickListener onClusterItemClickListener)
    {
        clusterManager = new ClusterManager<>(context,map);
        archidniClusterRenderer = new ArchidniClusterRenderer(context,map,clusterManager);
        clusterManager.setRenderer(archidniClusterRenderer);
        clusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<ArchidniClusterItem>() {
            @Override
            public boolean onClusterItemClick(ArchidniClusterItem archidniClusterItem) {
                Marker marker = archidniClusterRenderer.getMarkerOptionsHashMap().get(archidniClusterItem);
                onClusterItemClickListener.onClusterItemClick(archidniClusterItem,marker);
                return true;
            }
        });
    }*/

    public Coordinate getCenter ()
    {
        return new Coordinate(map.getCameraPosition().target.latitude,map.getCameraPosition().target.longitude);
    }

    public void moveCamera (Coordinate coordinate)
    {
        CameraUpdate cameraUpdateFactory = CameraUpdateFactory.newLatLng(coordinate.toGoogleMapLatLng());
        map.moveCamera(cameraUpdateFactory);
    }

    public void moveCamera (Coordinate coordinate,int zoom)
    {
        CameraUpdate cameraUpdateFactory = CameraUpdateFactory.newLatLngZoom(coordinate.toGoogleMapLatLng(),zoom);
        map.moveCamera(cameraUpdateFactory);
    }

    public void animateCamera (Coordinate coordinate,int zoom,int dutation)
    {
        CameraUpdate cameraUpdateFactory = CameraUpdateFactory.newLatLngZoom(coordinate.toGoogleMapLatLng(),zoom);
        map.animateCamera(cameraUpdateFactory, dutation, new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {

            }

            @Override
            public void onCancel() {

            }
        });
    }

    public void changeMarkerIcon (Marker marker,int drawableRes)
    {
        marker.setIcon(getBitmapDescriptor(drawableRes));
    }


    public void setMyLocationEnabled (boolean enabled)
    {
        map.setMyLocationEnabled(enabled);
    }

    public Coordinate getUserLocation ()
    {
        map.setMyLocationEnabled(true);
        if (map.getMyLocation()!=null)
        return new Coordinate(map.getMyLocation().getLatitude(),map.getMyLocation().getLongitude());
        else
            return null;
    }

    public void trackUser ()
    {
       //TODO implement
    }


    public void setOnMapLongClickListener (final OnMapLongClickListener onMapLongClickListener)
    {
        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                onMapLongClickListener.onMapLongClick(new Coordinate(latLng.latitude,latLng.longitude));
            }
        });
    }

    public void setOnMapShortClickListener (final OnMapShortClickListener onMapShortClickListener)
    {
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                onMapShortClickListener.onMapShortClick(new Coordinate(latLng.latitude,latLng.longitude));
            }
        });
    }

    public void addMarker (Coordinate coordinate,int markerDrawableResource)
    {
        //ArchidniMarker archidniMarker = new ArchidniMarker();

        Marker marker = map.addMarker(new MarkerOptions().icon(getBitmapDescriptor(markerDrawableResource))
                .position(coordinate.toGoogleMapLatLng()));
    }

    public void prepareMarker (Coordinate coordinate,int markerDrawableResource,float anchorX,float anchorY)
    {
        map.addMarker(new MarkerOptions().anchor(anchorX,anchorY)
                .icon(getBitmapDescriptor(markerDrawableResource))
                .position(coordinate.toGoogleMapLatLng()));
    }

    public void preparePolyline (Context context, ArrayList<Coordinate> coordinates,int colorResourceId)
    {
        ArrayList<LatLng> points = new ArrayList<>();
        for (Coordinate c:coordinates)
        {
            points.add(new LatLng(c.getLatitude(),c.getLongitude()));
        }
        map.addPolyline(new PolylineOptions().addAll(points).color(ContextCompat.getColor(context,colorResourceId)).width(6));
    }

    public void preparePolyline (Context context, ArrayList<Coordinate> coordinates,int colorResourceId,int width)
    {
        ArrayList<LatLng> points = new ArrayList<>();
        for (Coordinate c:coordinates)
        {
            points.add(new LatLng(c.getLatitude(),c.getLongitude()));
        }
        map.addPolyline(new PolylineOptions().addAll(points).color(ContextCompat.getColor(context,colorResourceId)).width(width));
    }

    public void animateCameraToBounds (ArrayList<Coordinate> bounds, final int padding, final int duration)
    {
        final LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Coordinate c:bounds)
        {
            builder.include(c.toGoogleMapLatLng());
        }
        if (mapLoaded)
        {
            map.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), padding),
                    duration, new GoogleMap.CancelableCallback() {
                @Override
                public void onFinish() {

                }

                @Override
                public void onCancel() {

                }
            });
        }
        else
        {
            map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    mapLoaded = true;
                    map.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), padding),
                            duration, new GoogleMap.CancelableCallback() {
                                @Override
                                public void onFinish() {

                                }

                                @Override
                                public void onCancel() {

                                }
                            });
                }
            });
        }
    }

    public void prepareClusterItem (Coordinate coordinate,int drawable,int clusterId,Object tag)
    {
        ArchidniClusterItem archidniClusterItem = new ArchidniClusterItem(coordinate,drawable,tag);
        for (ArchidniClusterItem archidniClusterItem1:archidniClusterItems)
        {
            if (archidniClusterItem1.getCoordinate().equals(archidniClusterItem.getCoordinate()))
            {
                return;
            }
        }
        clusterManagers.get(clusterId).addItem(archidniClusterItem);
    }

    /*public void removeClusterItemWithTag (Object tag)
    {
        for (ArchidniClusterItem archidniClusterItem:archidniClusterItems)
        {
            if (archidniClusterItem.getTag().equals(ta))
        }
    }*/

    public void renderClusters ()
    {
        map.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                onCameraIdle.onCameraIdle(getCenter(),getBoundingBox(),map.getCameraPosition().zoom);
                for (ClusterManager<ArchidniClusterItem> clusterManager:clusterManagers)
                {
                    clusterManager.onCameraIdle();
                }
            }
        });
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                for (ClusterManager<ArchidniClusterItem> clusterManager:clusterManagers)
                {
                    clusterManager.onMarkerClick(marker);
                }
                return true;
            }
        });
        for (ClusterManager<ArchidniClusterItem> clusterManager:clusterManagers)
        {
            clusterManager.cluster();
        }
        /*map.setOnCameraIdleListener(clusterManager);
        map.setOnMarkerClickListener(clusterManager);
        clusterManager.cluster();*/
    }

    /*public ArchidniClusterItem addClusterItem (Coordinate coordinate,int clusterId)
    {

        clusterManagers.get(clusterId).cluster();
        return archidniClusterItem;
    }*/

    public BoundingBox getBoundingBox()
    {
        LatLng northEast =  map.getProjection().getVisibleRegion().latLngBounds.northeast;
        LatLng southWest = map.getProjection().getVisibleRegion().latLngBounds.southwest;
        Coordinate northEastCoordinate = new Coordinate(northEast.latitude,northEast.longitude);
        Coordinate southWestCoordinate = new Coordinate(southWest.latitude,southWest.longitude);
        return new BoundingBox(northEastCoordinate,southWestCoordinate);
    }

    public void addPreparedAnnotations ()
    {

    }

    public void clearMap ()
    {
        map.clear();
    }

    public static BitmapDescriptor getBitmapDescriptor(int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Drawable vectorDrawable = App.getAppContext().getDrawable(id);

            int h = vectorDrawable.getIntrinsicHeight();
            int w = vectorDrawable.getIntrinsicWidth();

            vectorDrawable.setBounds(0, 0, w, h);

            Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bm);
            vectorDrawable.draw(canvas);

            return BitmapDescriptorFactory.fromBitmap(bm);

        } else {
            return BitmapDescriptorFactory.fromResource(id);
        }
    }

    public interface OnCameraIdle {
        void onCameraIdle (Coordinate coordinate,BoundingBox boundingBox,double zoom);
    }

    public interface OnCameraMoveListener {
        void onCameraMove (Coordinate coordinate, BoundingBox boundingBox, double zoom);
    }


    public interface OnMapLongClickListener {
        void onMapLongClick(Coordinate coordinate);
    }

    public interface OnMapShortClickListener {
        void onMapShortClick(Coordinate coordinate);
    }

    public interface OnMarkerClickListener {
        void onMarkerClick (ArchidniMarker archidniMarker);
    }

    public interface OnClusterItemClickListener {
        void onClusterItemClick (ArchidniClusterItem archidniClusterItem,Marker marker);
    }


}
