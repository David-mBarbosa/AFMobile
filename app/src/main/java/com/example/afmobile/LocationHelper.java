package com.example.afmobile;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class LocationHelper {
    private final Activity activity;
    private final FusedLocationProviderClient fusedLocationClient;
    public static final int LOCATION_REQUEST_CODE = 1001;

    public interface LocationCallback {
        void onLocationResult(double latitude, double longitude);
        void onPermissionDenied();
    }

    public LocationHelper(Activity activity) {
        this.activity = activity;
        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
    }

    public void getDeviceLocation(LocationCallback callback) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_REQUEST_CODE);
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(activity, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    callback.onLocationResult(location.getLatitude(), location.getLongitude());
                } else {
                    // Fallback se o GPS estiver desligado ou não houver cache
                    callback.onLocationResult(0.0, 0.0);
                }
            }
        });
    }
}