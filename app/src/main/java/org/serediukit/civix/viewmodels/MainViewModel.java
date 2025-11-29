package org.serediukit.civix.viewmodels;

import android.util.Log;

import org.serediukit.civix.models.MainModel;
import org.serediukit.civix.models.api.reports.request.CreateReportRequest;
import org.serediukit.civix.models.api.reports.response.CreateReportResponse;
import org.serediukit.civix.models.api.reports.response.GetReportsResponse;
import org.serediukit.civix.models.entities.city.Location;
import org.serediukit.civix.models.entities.report.Report;

import java.util.List;

public class MainViewModel {
    private final MainModel mainModel;

    public MainViewModel(MainModel mainModel) {
        this.mainModel = mainModel;
    }

    public List<Report> getAllReports(Location location) {
        GetReportsResponse resp = mainModel.getAllReports(location);
        if (resp == null) {
            return null;
        }

        return resp.getReports();
    }

    public Report createReport(CreateReportRequest request) {
        CreateReportResponse resp = mainModel.createReport(request);
        if (resp == null || !resp.isSuccess()) {
            return null;
        }

        return resp.getReport();
    }
}
