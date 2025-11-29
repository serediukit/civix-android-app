package org.serediukit.civix.models.api.reports;

import android.content.Context;
import android.util.Log;

import com.squareup.moshi.Moshi;

import org.serediukit.civix.models.MainModel;
import org.serediukit.civix.models.api.BaseHTTPClient;
import org.serediukit.civix.models.api.auth.RefreshAuthenticator;
import org.serediukit.civix.models.api.interceptors.AuthInterceptor;
import org.serediukit.civix.models.api.reports.request.CreateReportRequest;
import org.serediukit.civix.models.api.reports.response.CreateReportResponse;
import org.serediukit.civix.models.api.reports.response.GetReportsResponse;
import org.serediukit.civix.models.entities.city.Location;
import org.serediukit.civix.models.token.TokenManager;

import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class ReportsClient implements BaseHTTPClient {
    private static ReportsClient instance;
    private ReportsService reportsService;

    private ReportsClient(Context context) {
        try {
            TokenManager tokenManager = TokenManager.getInstance(context);

            Moshi moshi = new Moshi.Builder().build();

            Retrofit baseRetrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(MoshiConverterFactory.create(moshi))
                    .build();

            AuthInterceptor authInterceptor = new AuthInterceptor(tokenManager);
            RefreshAuthenticator refreshAuthenticator = new RefreshAuthenticator(tokenManager, baseRetrofit);

            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) {}

                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType) {}

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[]{};
                        }
                    }
            };

            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCerts[0])
                    .hostnameVerifier((hostname, session) -> true)
                    .addInterceptor(authInterceptor)
                    .authenticator(refreshAuthenticator)
                    .build();

            Retrofit retrofit = baseRetrofit.newBuilder()
                    .client(okHttpClient)
                    .build();

            reportsService = retrofit.create(ReportsService.class);
        } catch (Exception e) {
            Log.e("REPORTS CLIENT", e.toString());
        }
    }

    public static ReportsClient getInstance(Context context) {
        if (instance == null) {
            instance = new ReportsClient(context);
        }
        return instance;
    }

    /**
     * Get reports based on location and optional filters.
     * @param lat Latitude coordinate.
     * @param lon Longitude coordinate.
     * @param statuses Optional array of report status IDs to filter by.
     * @param pageSize Maximum number of reports to return (max 100).
     * @return A Retrofit Call object for the get reports request.
     */
    public Call<GetReportsResponse> getReports(double lat, double lon, int[] statuses, int pageSize) {
        return reportsService.getReports(lat, lon, statuses, pageSize);
    }

    /**
     * Get reports based on location with default parameters.
     * @param lat Latitude coordinate.
     * @param lon Longitude coordinate.
     * @return A Retrofit Call object for the get reports request.
     */
    public Call<GetReportsResponse> getReports(double lat, double lon) {
        return reportsService.getReports(lat, lon, null, MainModel.DEFAULT_PAGE_SIZE);
    }

    /**
     * Create a new report.
     * @param location The location where the issue is reported.
     * @param description Description of the reported issue.
     * @param categoryId Category ID of the report.
     * @return A Retrofit Call object for the create report request.
     */
    public Call<CreateReportResponse> createReport(Location location, String description, int categoryId) {
        CreateReportRequest request = new CreateReportRequest(location, description, categoryId);
        return reportsService.createReport(request);
    }

    /**
     * Create a new report with a CreateReportRequest object.
     * @param request The CreateReportRequest containing all report details including photo URL.
     * @return A Retrofit Call object for the create report request.
     */
    public Call<CreateReportResponse> createReport(CreateReportRequest request) {
        return reportsService.createReport(request);
    }
}
