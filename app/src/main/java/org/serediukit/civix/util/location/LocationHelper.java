package org.serediukit.civix.util.location;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.*;

public class LocationHelper {

    public interface ILocationCallback {
        void onLocationReceived(double lat, double lon);
        void onError(String msg);
    }

    private final Context context;
    private final Activity activity;
    private final FusedLocationProviderClient fused;

    private static final int REQUEST_CODE = 1001;

    public LocationHelper(@NonNull Context context, @NonNull Activity activity) {
        this.context = context;
        this.activity = activity;
        this.fused = LocationServices.getFusedLocationProviderClient(context);
    }

    public void getLocation(ILocationCallback callback) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE);

            callback.onError("Permission required");
            return;
        }

        fused.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        callback.onLocationReceived(
                                location.getLatitude(),
                                location.getLongitude()
                        );
                    } else {
                        requestLiveUpdate(callback);
                    }
                })
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    private void requestLiveUpdate(ILocationCallback callback) {
        LocationRequest req = LocationRequest.create()
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                .setInterval(2000);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            callback.onError("No location permission");
            return;
        }

        fused.requestLocationUpdates(req, new LocationCallbackCustom(callback), Looper.getMainLooper());
    }

    private class LocationCallbackCustom extends LocationCallback {
        private final ILocationCallback cb;

        public LocationCallbackCustom(ILocationCallback cb) {
            this.cb = cb;
        }

        @Override
        public void onLocationResult(@NonNull LocationResult result) {
            if (result.getLastLocation() != null) {
                double lat = result.getLastLocation().getLatitude();
                double lon = result.getLastLocation().getLongitude();

                cb.onLocationReceived(lat, lon);

                fused.removeLocationUpdates(this);
            }
        }
    }
}

