package org.serediukit.civix.models;

import android.content.Context;
import android.util.Log;

import org.serediukit.civix.models.api.reports.ReportsClient;
import org.serediukit.civix.models.api.reports.response.GetReportsResponse;
import org.serediukit.civix.models.entities.city.Location;

import java.util.List;

import retrofit2.Response;

public class MainModel {
    public final static int DEFAULT_PAGE_SIZE = 100;
    private final ReportsClient reportsClient;

    public MainModel(Context context) {
        reportsClient = ReportsClient.getInstance(context);
    }

    public GetReportsResponse getAllReports(Location location) {
        try {
            Response<GetReportsResponse> response = reportsClient.getReports(location.getLat(), location.getLon()).execute();
            if (response.isSuccessful() && response.body() != null) {
                Log.d("MAIN MODEL | GET REPORTS",  response.body().toString());
                return response.body();
            } else {
                throw new Exception("reports service error");
            }
        } catch (Exception e) {
            Log.e("MAIN MODEL | GET REPORTS", e.toString());
        }

        return null;
    }
}
