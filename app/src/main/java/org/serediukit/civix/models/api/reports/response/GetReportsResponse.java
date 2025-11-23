package org.serediukit.civix.models.api.reports.response;

import com.squareup.moshi.Json;

import org.serediukit.civix.models.api.reports.responsedata.GetReportsData;
import org.serediukit.civix.models.entities.report.Report;

import java.util.ArrayList;
import java.util.List;

public class GetReportsResponse {
    @Json(name = "data")
    private GetReportsData data;

    @Json(name = "success")
    private boolean success;

    public List<Report> getReports() {
        if (data != null && data.getReports() != null) {
            return data.getReports();
        }
        return new ArrayList<>();
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
