package com.archidni.archidni.UiUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;

import com.archidni.archidni.Model.Coordinate;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.MarkerView;
import com.mapbox.mapboxsdk.annotations.MarkerViewOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdate;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.MyBearingTracking;
import com.mapbox.mapboxsdk.constants.MyLocationTracking;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import java.util.ArrayList;

/**
 * Created by noure on 02/02/2018.
 */

public class ArchidniMap {
    private MapView mapView;
    private MapboxMap mapboxMap;
    private Coordinate userLocation;
    private ArrayList<ArchidniMarker> archidniMarkers;
    private ArrayList<PreparedArchidniMarker> preparedArchidniMarkers;

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
        archidniMarkers = new ArrayList<>();
        preparedArchidniMarkers = new ArrayList<>();
    }

    public Coordinate getCenter ()
    {
        return new Coordinate(mapboxMap.getCameraPosition().target);
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

    public void moveCameraToBounds (ArrayList<Coordinate> coordinates, int padding)
    {
        LatLngBounds.Builder latLngBoundsBulder = new LatLngBounds.Builder();
        for (Coordinate c:coordinates)
        {
            latLngBoundsBulder.include(c.toMapBoxLatLng());
        }
        com.mapbox.mapboxsdk.geometry.LatLngBounds latLngBounds = latLngBoundsBulder.build();


        mapboxMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds,padding));
    }

    public void moveCameraToBounds (ArrayList<Coordinate> coordinates, int paddingLeft,int paddingTop,
                                    int paddingRight,int paddingDown)
    {
        com.mapbox.mapboxsdk.geometry.LatLngBounds.Builder latLngBoundsBulder = new com.mapbox.mapboxsdk.geometry.LatLngBounds.Builder();
        for (Coordinate c:coordinates)
        {
            latLngBoundsBulder.include(c.toMapBoxLatLng());
        }
        com.mapbox.mapboxsdk.geometry.LatLngBounds latLngBounds = latLngBoundsBulder.build();

        mapboxMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds,paddingLeft,paddingTop,
                paddingRight,paddingDown));

    }

    public ArchidniMarker addMarker(Coordinate coordinate, int markerDrawableResource) {
        IconFactory iconFactory = IconFactory.getInstance(mapView.getContext());
        Icon icon = iconFactory.fromBitmap(getBitmapFromVectorDrawable(markerDrawableResource));
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(coordinate.toMapBoxLatLng());
        markerOptions.icon(icon);
        Marker marker = mapboxMap.addMarker(markerOptions);
        ArchidniMarker archidniMarker = new ArchidniMarker(marker);
        archidniMarkers.add(archidniMarker);
        return archidniMarker;
    }

    public ArchidniMarker addMarker(Coordinate coordinate, int markerDrawableResource,Object tag) {
        IconFactory iconFactory = IconFactory.getInstance(mapView.getContext());
        Icon icon = iconFactory.fromBitmap(getBitmapFromVectorDrawable(markerDrawableResource));
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(coordinate.toMapBoxLatLng());
        markerOptions.icon(icon);
        Marker marker = mapboxMap.addMarker(markerOptions);
        ArchidniMarker archidniMarker = new ArchidniMarker(marker,tag);
        archidniMarkers.add(archidniMarker);
        return archidniMarker;
    }

    public void changeMarkerIcon (ArchidniMarker archidniMarker,int drawable)
    {
        IconFactory iconFactory = IconFactory.getInstance(mapView.getContext());
        Icon icon = iconFactory.fromBitmap(getBitmapFromVectorDrawable(drawable));
        archidniMarker.getMarker().setIcon(icon);
    }

    public void changeMarkerIcon (int drawable,Object tag)
    {
        IconFactory iconFactory = IconFactory.getInstance(mapView.getContext());
        Icon icon = iconFactory.fromBitmap(getBitmapFromVectorDrawable(drawable));
        for (ArchidniMarker archidniMarker:archidniMarkers)
        {
            if (archidniMarker.getTag().equals(tag))
            {
                archidniMarker.getMarker().setIcon(icon);
            }
        }
    }

    public void removeMarker (Object tag)
    {
        for (ArchidniMarker archidniMarker:archidniMarkers)
        {
            if (archidniMarker.getTag()!=null&&archidniMarker.getTag().equals(tag))
            {
                mapboxMap.removeMarker(archidniMarker.getMarker());
            }
            if (archidniMarker.getTag()==null&&tag==null)
            {
                mapboxMap.removeMarker(archidniMarker.getMarker());
            }
        }
        for (int i = 0; i < archidniMarkers.size(); i++) {
            ArchidniMarker archidniMarker = archidniMarkers.get(i);
            if (archidniMarker.getTag()!=null&&archidniMarker.getTag().equals(tag))
            {
                mapboxMap.removeMarker(archidniMarker.getMarker());
                archidniMarkers.remove(archidniMarker);
            }
            if (archidniMarker.getTag()==null&&tag==null)
            {
                mapboxMap.removeMarker(archidniMarker.getMarker());
                archidniMarkers.remove(archidniMarker);
            }
        }
    }

    public void removeMarkers (ArrayList arrayList)
    {
        for (ArchidniMarker archidniMarker:archidniMarkers)
        {
            for (Object object : arrayList)
            {
                if (archidniMarker.getTag()!=null&&archidniMarker.getTag().equals(object))
                {
                    mapboxMap.removeMarker(archidniMarker.getMarker());
                }
            }
        }
    }

    public void prepareMarker(Coordinate coordinate, int markerDrawableResource) {
        IconFactory iconFactory = IconFactory.getInstance(mapView.getContext());
        Icon icon = iconFactory.fromBitmap(getBitmapFromVectorDrawable(markerDrawableResource));
        MarkerOptions marker = new MarkerOptions().position(new LatLng(coordinate.getLatitude(),
                coordinate.getLongitude()))
                .icon(icon)
                ;
        preparedArchidniMarkers.add(new PreparedArchidniMarker(marker));
    }

    public void prepareMarker(Coordinate coordinate, int markerDrawableResource,Object tag) {
        IconFactory iconFactory = IconFactory.getInstance(mapView.getContext());
        Icon icon = iconFactory.fromBitmap(getBitmapFromVectorDrawable(markerDrawableResource));
        MarkerOptions marker = new com.mapbox.mapboxsdk.annotations.MarkerOptions()
                .position(new LatLng(coordinate.getLatitude(), coordinate.getLongitude()))
                .icon(icon);
        preparedArchidniMarkers.add(new PreparedArchidniMarker(marker,tag));
    }

    public void setOnMarkerClickListener (final OnMarkerClickListener onMarkerClickListener)
    {
        mapboxMap.setOnMarkerClickListener(new MapboxMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                for(ArchidniMarker archidniMarker:archidniMarkers)
                {
                    if (archidniMarker.getMarker().getPosition().getLatitude()
                            ==marker.getPosition().getLatitude()&&
                            archidniMarker.getMarker().getPosition().getLongitude()
                                    ==marker.getPosition().getLongitude())
                    {
                        onMarkerClickListener.onMarkerClick(archidniMarker);
                        return true;
                    }
                }
                return false;
            }
        });
    }

    public void addPreparedAnnotations ()
    {
        ArrayList<MarkerOptions> markerViewOptions = new ArrayList<>();
        for (PreparedArchidniMarker preparedArchidniMarker : preparedArchidniMarkers)
        {
            markerViewOptions.add(preparedArchidniMarker.getMarkerViewOptions());

        }
        ArrayList<Marker> markers = new ArrayList<>();
        markers.addAll(mapboxMap.addMarkers(markerViewOptions));
        for (Marker marker:markers)
        {
            PreparedArchidniMarker preparedArchidniMarker = null;
            for (PreparedArchidniMarker preparedArchidniMarker1:preparedArchidniMarkers)
            {
                if (preparedArchidniMarker1.getMarkerViewOptions().getPosition().getLongitude()
                        ==marker.getPosition().getLongitude()&&
                        preparedArchidniMarker1
                                .getMarkerViewOptions()
                                .getMarker().getPosition().getLatitude()==marker.getPosition()
                                .getLatitude())
                {
                    preparedArchidniMarker = preparedArchidniMarker1;
                    break;
                }
            }
            archidniMarkers.add(new ArchidniMarker(marker,
                    preparedArchidniMarker.getTag()));
        }
        preparedArchidniMarkers = new ArrayList<>();
    }

    public void clearMap ()
    {
        mapboxMap.clear();
        archidniMarkers = new ArrayList<>();
        preparedArchidniMarkers = new ArrayList<>();
    }

    public void clearMarkersWithTags ()
    {
        int i = 0;
        while (i< archidniMarkers.size())
        {
            ArchidniMarker archidniMarker = archidniMarkers.get(i);
            if (archidniMarker.getTag()!=null)
                removeMarker(archidniMarker.getTag());
            else
                i++;
        }

    }

    private Bitmap getBitmapFromVectorDrawable(int drawableId) {
        Context context = mapView.getContext();
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public void setOnCameraMoveListener (final OnCameraMoveListener onCameraMoveListener)
    {
        mapboxMap.setOnCameraMoveListener(new MapboxMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                onCameraMoveListener.onCameraMove(new Coordinate(mapboxMap.getCameraPosition().target),
                        mapboxMap.getCameraPosition().zoom);
            }
        });
    }

    public interface OnCameraMoveListener {
        void onCameraMove (Coordinate coordinate,double zoom);
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

    public interface OnMarkerClickListener {
        void onMarkerClick (ArchidniMarker archidniMarker);
    }
}
