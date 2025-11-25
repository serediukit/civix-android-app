package org.serediukit.civix.models;

import android.content.Context;
import android.util.Log;

import org.serediukit.civix.models.api.reports.ReportsClient;
import org.serediukit.civix.models.api.reports.request.CreateReportRequest;
import org.serediukit.civix.models.api.reports.response.CreateReportResponse;
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

    public CreateReportResponse createReport(CreateReportRequest request) {
        try {
            Log.d("MAIN MODEL | CREATE REPORT", "Creating report with photo_url: " + request.getPhotoUrl());
            Response<CreateReportResponse> response = reportsClient.createReport(request).execute();
            if (response.isSuccessful() && response.body() != null) {
                Log.d("MAIN MODEL | CREATE REPORT", "Report created successfully");
                return response.body();
            } else {
                Log.e("MAIN MODEL | CREATE REPORT", "Response not successful: " + response.code() + " - " + response.message());
                if (response.errorBody() != null) {
                    Log.e("MAIN MODEL | CREATE REPORT", "Error body: " + response.errorBody().string());
                }
                throw new Exception("create report service error");
            }
        } catch (Exception e) {
            Log.e("MAIN MODEL | CREATE REPORT", e.toString());
        }

        return null;
    }
}
