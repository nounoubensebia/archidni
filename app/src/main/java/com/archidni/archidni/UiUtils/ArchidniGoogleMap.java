package com.archidni.archidni.UiUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.support.v4.content.ContextCompat;

import com.archidni.archidni.App;
import com.archidni.archidni.Model.BoundingBox;
import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.Transport.Station;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.ui.IconGenerator;

import java.util.ArrayList;

public class ArchidniGoogleMap  {
    private GoogleMap map;
    private MapFragment mapFragment;
    private boolean mapLoaded = false;
    private Coordinate userLocation;
    private OnCameraIdle onCameraIdle;
    private ClusterHandler clusterHandler;

    @SuppressLint("MissingPermission")
    public ArchidniGoogleMap(final Activity activity, final MapFragment mapFragment, final OnMapReadyCallback onMapReadyCallback) {
        clusterHandler =
                //new GoogleUtilsClusterHandler(ArchidniGoogleMap.this,activity);
                new ShareWireClusterHandler(activity,ArchidniGoogleMap.this);
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

                //map.setTrafficEnabled(true);
            }
        });
    }

    public OnCameraIdle getOnCameraIdle() {
        return onCameraIdle;
    }

    public void setOnCameraIdle(OnCameraIdle onCameraIdle) {
        this.onCameraIdle = onCameraIdle;
    }

    public void addCluster (Context context, final ClusterHandler.OnClusterItemClickListener onClusterItemClickListener)
    {
        clusterHandler.addCluster(context,onClusterItemClickListener);
    }


    public void removeAllClusterItems (int clusterId)
    {
        clusterHandler.removeAllClusterItems(clusterId);
    }




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


    @SuppressLint("MissingPermission")
    public void setMyLocationEnabled (boolean enabled)
    {
        map.setMyLocationEnabled(enabled);
    }

    public Coordinate getUserLocation ()
    {
        return userLocation;
    }



    public void trackUser ()
    {
       //TODO IMPLEMENT
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
       map.addMarker(new MarkerOptions().icon(getBitmapDescriptor(markerDrawableResource))
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
        clusterHandler.prepareClusterItem(coordinate,drawable,clusterId,tag);
    }



    public void renderClusters ()
    {
        clusterHandler.renderClusters();
    }



    public void initClusters ()
    {
        clusterHandler.initClusters();
    }
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
        //clusterManagers = new ArrayList<>();
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

    public GoogleMap getMap() {
        return map;
    }


    public interface OnCameraIdle {
        public void onCameraIdle (Coordinate coordinate,LatLngBounds latLngBounds,double zoom);
    }

    public interface OnCameraMoveListener {
        void onCameraMove (Coordinate coordinate, LatLngBounds latLngBounds, double zoom);
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



    public interface OnUserLocationUpdated
    {
        void onUserLocationCaptured(Coordinate userLocation);
    }

}
