package org.serediukit.civix.views;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.serediukit.civix.R;
import org.serediukit.civix.models.MainModel;
import org.serediukit.civix.models.entities.city.Location;
import org.serediukit.civix.models.entities.report.Report;
import org.serediukit.civix.util.location.LocationHelper;
import org.serediukit.civix.viewmodels.MainViewModel;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private MainViewModel mainViewModel;
    private LocationHelper locationHelper;
    private GoogleMap googleMap;
    private List<Report> reports;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();
    }

    private void init() {
        MainModel mainModel = new MainModel(this.getApplicationContext());
        mainViewModel = new MainViewModel(mainModel);

        locationHelper = new LocationHelper(this, this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        loadReports();
    }

    private void loadReports() {
        new Thread(() -> {
            Location location = getUserLocation();
            List<Report> reports = fetchReportsForLocation(location);
            runOnUiThread(() -> displayReports(reports));
        }).start();
    }

    private Location getUserLocation() {
        CountDownLatch latch = new CountDownLatch(1);
        final double[] loc = new double[2];

        locationHelper.getLocation(new LocationHelper.ILocationCallback() {
            @Override
            public void onLocationReceived(double lat, double lon) {
                loc[0] = lat;
                loc[1] = lon;
                Log.d("CIVIX | LOCATION", "lat:"+lat+" lon:"+lon);
                latch.countDown();
            }

            @Override
            public void onError(String msg) {
                Log.e("CIVIX | LOCATION", msg);
                latch.countDown();
            }
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            Log.e("CIVIX | LOCATION", Objects.requireNonNull(e.getMessage()));
        }

        return new Location(loc[0], loc[1]);
    }

    private List<Report> fetchReportsForLocation(Location location) {
        return mainViewModel.getAllReports(location);
    }

    private void displayReports(List<Report> reports) {
        this.reports = reports;
        if (reports != null && googleMap != null) {
            displayReportsOnMap();
        } else if (reports == null) {
            Toast.makeText(MainActivity.this, R.string.get_reports_error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        googleMap = map;

        if (reports != null) {
            displayReportsOnMap();
        }
    }

    private void displayReportsOnMap() {
        if (googleMap == null || reports == null || reports.isEmpty()) {
            return;
        }

        googleMap.clear();
        LatLng firstLocation = null;

        for (Report report : reports) {
            Location location = report.getLocation();
            if (location != null) {
                LatLng latLng = new LatLng(location.getLat(), location.getLon());

                MarkerOptions markerOptions = new MarkerOptions()
                        .position(latLng)
                        .title("Report #" + report.getReportId())
                        .snippet(report.getDescription());

                googleMap.addMarker(markerOptions);

                if (firstLocation == null) {
                    firstLocation = latLng;
                }
            }
        }

        if (firstLocation != null) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLocation, 12f));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1001) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadReports();
            }
        }
    }
}