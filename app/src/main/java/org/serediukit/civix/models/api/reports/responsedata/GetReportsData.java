package org.serediukit.civix.models.api.reports.responsedata;

import com.squareup.moshi.Json;

import org.serediukit.civix.models.entities.report.Report;

import java.util.List;

public class GetReportsData {
    @Json(name = "reports")
    private List<Report> reports;

    public List<Report> getReports() {
        return reports;
    }

    public void setReports(List<Report> reports) {
        this.reports = reports;
    }
}
