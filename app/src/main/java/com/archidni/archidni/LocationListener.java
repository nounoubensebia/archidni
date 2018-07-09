package com.archidni.archidni;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.location.Location;

import com.archidni.archidni.Model.Coordinate;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class LocationListener {

    private FusedLocationProviderClient fusedLocationClient;
    private Coordinate userLocation;
    private LocationRequest locationRequest;

    public LocationListener(Activity activity) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
        createLocationRequest();
    }

    private void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @SuppressLint("MissingPermission")
    public void getLastKnownUserLocation (final OnUserLocationUpdated onUserLocationUpdated)
    {
        fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location!=null)
                {
                    onUserLocationUpdated.onUserLocationUpdated(new Coordinate(location.getLatitude(),
                            location.getLongitude()));
                }
            }
        });
    }


    @SuppressLint("MissingPermission")
    public void listenForLocationUpdates (final OnUserLocationUpdated onUserLocationUpdated)
    {
        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                Location location = locationResult.getLastLocation();
                userLocation = new Coordinate(location.getLatitude(),location.getLongitude());
                if (onUserLocationUpdated !=null)
                {
                    onUserLocationUpdated.onUserLocationUpdated(userLocation);
                }
            }
        };
        fusedLocationClient.requestLocationUpdates(locationRequest,locationCallback,null);
    }

    public interface OnUserLocationUpdated
    {
        void onUserLocationUpdated(Coordinate userLocation);
    }

}
