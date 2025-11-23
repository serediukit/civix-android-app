package org.serediukit.civix.models.api.reports.response;

import com.squareup.moshi.Json;

import org.serediukit.civix.models.api.reports.responsedata.CreateReportData;
import org.serediukit.civix.models.entities.report.Report;

public class CreateReportResponse {
    @Json(name = "data")
    private CreateReportData data;

    @Json(name = "success")
    private boolean success;

    public Report getReport() {
        if (data != null) {
            return data.getReport();
        }
        return null;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
