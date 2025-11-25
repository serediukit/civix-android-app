package org.serediukit.civix.views;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
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
import org.serediukit.civix.models.api.reports.request.CreateReportRequest;
import org.serediukit.civix.models.entities.city.Location;
import org.serediukit.civix.models.entities.report.Report;
import org.serediukit.civix.models.entities.report.ReportCategory;
import org.serediukit.civix.models.entities.report.ReportStatus;
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
    private Location location;

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
            location = getUserLocation();
            List<Report> reports = fetchReports();
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

    private List<Report> fetchReports() {
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

        googleMap.getUiSettings().setZoomControlsEnabled(false);
        googleMap.getUiSettings().setMapToolbarEnabled(false);

        enableMyLocation();

        if (reports != null) {
            displayReportsOnMap();
        }
    }

    private void displayReportsOnMap() {
        if (googleMap == null || reports == null || reports.isEmpty()) {
            return;
        }

        runOnUiThread(() -> {
            googleMap.clear();

            LatLng userLocation = new LatLng(location.getLat(), location.getLon());
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15f));

            for (Report report : reports) {
                Location rLocation = report.getLocation();
                if (rLocation != null) {
                    LatLng latLng = new LatLng(rLocation.getLat(), rLocation.getLon());

                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(latLng)
                            .title(report.getDescription())
                            .snippet(report.getUpdateTime()+"\n"+ReportStatus.fromValue(report.getCurrentStatusId()).toString());

                    googleMap.addMarker(markerOptions);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1001) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadReports();
                enableMyLocation();
            }
        }
    }

    private void enableMyLocation() {
        if (googleMap == null) {
            return;
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        }
    }

    public void centerOnMyLocation(View view) {
        if (googleMap == null) {
            return;
        }

        locationHelper.getLocation(new LocationHelper.ILocationCallback() {
            @Override
            public void onLocationReceived(double lat, double lon) {
                location = new Location(lat, lon);
                Log.d("CIVIX | LOCATION", "lat:"+lat+" lon:"+lon);
                LatLng userLocation = new LatLng(lat, lon);
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15f));
            }

            @Override
            public void onError(String msg) {
                Log.e("CIVIX | LOCATION", msg);
                Toast.makeText(MainActivity.this, R.string.error_location_failed, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void openProfile(View view) {
    }

    public void createReport(View view) {
        centerOnMyLocation(view);
        showCreateReportDialog();
    }

    private void showCreateReportDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_create_report);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);

        Spinner categorySpinner = dialog.findViewById(R.id.category_spinner);
        EditText descriptionInput = dialog.findViewById(R.id.description_input);
        Button submitButton = dialog.findViewById(R.id.submit_button);
        Button cancelButton = dialog.findViewById(R.id.cancel_button);

        String[] categories = {
                getString(R.string.category_road),
                getString(R.string.category_sideway),
                getString(R.string.category_electric),
                getString(R.string.category_water),
                getString(R.string.category_gas),
                getString(R.string.category_heat)
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        cancelButton.setOnClickListener(v -> dialog.dismiss());

        submitButton.setOnClickListener(v -> {
            String description = descriptionInput.getText().toString().trim();
            int categoryPosition = categorySpinner.getSelectedItemPosition();

            if (description.isEmpty()) {
                Toast.makeText(this, R.string.error_description_empty, Toast.LENGTH_SHORT).show();
                return;
            }

            if (location == null) {
                Toast.makeText(this, R.string.error_location_unavailable, Toast.LENGTH_SHORT).show();
                return;
            }

            ReportCategory category = ReportCategory.values()[categoryPosition + 1];
            CreateReportRequest request = new CreateReportRequest(location, description, category.getValue());

            dialog.dismiss();
            submitReport(request);
        });

        dialog.show();
    }

    private void submitReport(CreateReportRequest request) {
        Toast.makeText(this, R.string.report_submitting, Toast.LENGTH_SHORT).show();

        new Thread(() -> {
            Report newReport = mainViewModel.createReport(request);
            runOnUiThread(() -> {
                if (newReport != null) {
                    Toast.makeText(MainActivity.this, R.string.report_success, Toast.LENGTH_SHORT).show();
                    loadReports();
                } else {
                    Toast.makeText(MainActivity.this, R.string.report_failed, Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }
}