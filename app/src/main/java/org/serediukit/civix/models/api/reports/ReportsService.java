package org.serediukit.civix.models.api.reports;

import org.serediukit.civix.models.api.reports.request.CreateReportRequest;
import org.serediukit.civix.models.api.reports.response.CreateReportResponse;
import org.serediukit.civix.models.api.reports.response.GetReportsResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ReportsService {

    @GET("reports/")
    Call<GetReportsResponse> getReports(
            @Query("lat") double lat,
            @Query("lon") double lon,
            @Query("statuses") int[] statuses,
            @Query("page_size") int pageSize
    );

    @POST("reports/")
    Call<CreateReportResponse> createReport(@Body CreateReportRequest request);
}
