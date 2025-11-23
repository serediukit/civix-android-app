package org.serediukit.civix.models.api.reports.responsedata;

import com.squareup.moshi.Json;

import org.serediukit.civix.models.entities.report.Report;

public class CreateReportData {
    @Json(name = "report")
    private Report report;

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }
}
