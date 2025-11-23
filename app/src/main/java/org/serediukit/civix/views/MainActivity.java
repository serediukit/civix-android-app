package org.serediukit.civix.views;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.serediukit.civix.R;
import org.serediukit.civix.models.MainModel;
import org.serediukit.civix.models.entities.city.Location;
import org.serediukit.civix.models.entities.report.Report;
import org.serediukit.civix.util.location.LocationHelper;
import org.serediukit.civix.util.uicodes.UICode;
import org.serediukit.civix.viewmodels.MainViewModel;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

public class MainActivity extends AppCompatActivity {
    private MainViewModel mainViewModel;
    private LocationHelper locationHelper;
    private TextView mainTV;

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

        mainTV = findViewById(R.id.main_activity_text_view);

        locationHelper = new LocationHelper(this, this);

        loadReports();
    }

    private void loadReports() {
        new Thread(() -> {
            CountDownLatch latch = new CountDownLatch(1);

            final double[] loc = new double[2];

            locationHelper.getLocation(new LocationHelper.ILocationCallback() {
                @Override
                public void onLocationReceived(double lat, double lon) {
                    loc[0] = lat;
                    loc[1] = lon;
                    latch.countDown();
                }

                @Override
                public void onError(String msg) {
                    latch.countDown();
                }
            });

            try {
                latch.await();
            } catch (InterruptedException e) {
                Log.e("CIVIX | LOCATION", Objects.requireNonNull(e.getMessage()));
            }

            Location location = new Location(loc[0], loc[1]);
            List<Report> reports = mainViewModel.getAllReports(location);

            runOnUiThread(() -> {
                if (reports != null) {
                    StringBuilder displayText = new StringBuilder();

                    for (Report report : reports) {
                        displayText.append(report.toString());
                        displayText.append("\n");
                    }

                    mainTV.setText(displayText.toString());
                } else {
                    Toast.makeText(MainActivity.this, R.string.get_reports_error, Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }
}