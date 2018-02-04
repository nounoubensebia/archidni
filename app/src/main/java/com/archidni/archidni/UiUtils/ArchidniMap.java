package com.archidni.archidni.UiUtils;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.archidni.archidni.Model.Coordinate;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdate;
import com.mapbox.mapboxsdk.constants.MyBearingTracking;
import com.mapbox.mapboxsdk.constants.MyLocationTracking;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;

/**
 * Created by noure on 02/02/2018.
 */

public class ArchidniMap {
    private MapView mapView;
    private MapboxMap mapboxMap;
    private Coordinate userLocation;

    public ArchidniMap(MapView mapView, Bundle savedInstanceState,
                       final OnMapReadyCallback onMapReadyCallback)
    {
        this.mapView = mapView;
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new com.mapbox.mapboxsdk.maps.OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                ArchidniMap.this.mapboxMap = mapboxMap;
                mapboxMap.getUiSettings().setCompassEnabled(false);
                onMapReadyCallback.onMapReady();
            }
        });
    }

    public void moveCamera (final Coordinate coordinate)
    {
        mapboxMap.moveCamera(new CameraUpdate() {
            @Override
            public CameraPosition getCameraPosition(@NonNull MapboxMap mapboxMap) {
                CameraPosition.Builder builder = new CameraPosition.Builder()
                        .target(coordinate.toMapBoxLatLng());
                return builder.build();
            }
        });
    }

    public void moveCamera(final Coordinate coordinate, final int zoom) {
        mapboxMap.moveCamera(new CameraUpdate() {
            @Override
            public CameraPosition getCameraPosition(@NonNull MapboxMap mapboxMap) {
                CameraPosition.Builder builder = new CameraPosition.Builder()
                        .target(coordinate.toMapBoxLatLng()).zoom(zoom);
                return builder.build();
            }
        });
    }

    public void animateCamera(final Coordinate coordinate, int duration) {

        mapboxMap.animateCamera(new CameraUpdate() {
            @Override
            public CameraPosition getCameraPosition(@NonNull MapboxMap mapboxMap) {
                CameraPosition.Builder builder1 = new CameraPosition.Builder()
                        .target(coordinate.toMapBoxLatLng());
                return builder1.build();
            }
        },duration);
    }

    public void animateCamera(final Coordinate coordinate, final int zoom, int duration) {

        mapboxMap.animateCamera(new CameraUpdate() {
            @Override
            public CameraPosition getCameraPosition(@NonNull MapboxMap mapboxMap) {
                CameraPosition.Builder builder1 = new CameraPosition.Builder()
                        .target(coordinate.toMapBoxLatLng()).zoom(zoom);
                return builder1.build();
            }
        },duration);
    }

    public void setMyLocationEnabled (boolean enabled)
    {
        mapboxMap.setMyLocationEnabled(enabled);
    }

    public Coordinate getUserLocation ()
    {
        mapboxMap.setMyLocationEnabled(true);
        if (mapboxMap.getMyLocation()!=null)
        {
            userLocation = new Coordinate(mapboxMap.getMyLocation()
                    .getLatitude(),mapboxMap.getMyLocation().getLongitude());
        }
        return userLocation;
    }

    public void trackUser ()
    {
        mapboxMap.getTrackingSettings()
                    .setMyLocationTrackingMode(MyLocationTracking.TRACKING_FOLLOW);
        mapboxMap.getTrackingSettings()
                    .setMyBearingTrackingMode(MyBearingTracking.COMPASS);

    }

    public void setOnMapLongClickListener(final OnMapLongClickListener onMapLongClickListenerListener)
    {
        mapboxMap.setOnMapLongClickListener(new MapboxMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(@NonNull LatLng point) {
                onMapLongClickListenerListener.onMapLongClick(new Coordinate(point));
            }
        });
    }

    public void setOnMapShortClickListener (final OnMapShortClickListener onMapShortClickListener)
    {
        mapboxMap.setOnMapClickListener(new MapboxMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng point) {
                onMapShortClickListener.onMapShortClick(new Coordinate(point));
            }
        });
    }

    public interface OnMapReadyCallback {
        void onMapReady();
    }

    public interface OnMapLongClickListener {
        void onMapLongClick(Coordinate coordinate);
    }

    public interface OnMapShortClickListener {
        void onMapShortClick(Coordinate coordinate);
    }
}
