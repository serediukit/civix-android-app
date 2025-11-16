package org.serediukit.civix.models.api.auth;

import android.util.Log;

import com.squareup.moshi.Moshi;

import org.serediukit.civix.models.api.BaseHTTPClient;
import org.serediukit.civix.models.api.auth.request.LoginRequest;
import org.serediukit.civix.models.api.auth.request.RefreshRequest;
import org.serediukit.civix.models.api.auth.response.LoginResponse;
import org.serediukit.civix.models.api.auth.response.RefreshResponse;

import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class AuthClient implements BaseHTTPClient {
    private static AuthClient instance;
    private AuthService authService;

    private AuthClient() {
        try {
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
                    .build();

            Moshi moshi = new Moshi.Builder()
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(MoshiConverterFactory.create(moshi))
                    .build();

            authService = retrofit.create(AuthService.class);
        } catch (Exception e) {
            Log.e("AUTH CLIENT", e.toString());
        }
    }

    public static AuthClient getInstance() {
        if (instance == null) {
            instance = new AuthClient();
        }
        return instance;
    }

    /**
     * @param email The user's email.
     * @param password The user's password.
     * @return A Retrofit Call object for the login request.
     */
    public Call<LoginResponse> login(String email, String password) {
        LoginRequest request = new LoginRequest(email, password);
        return authService.login(request);
    }

    /**
     * @param token The user's refresh_token.
     * @return A Retrofit Call object for the refresh request.
     */
    public Call<RefreshResponse> refresh(String token) {
        RefreshRequest request = new RefreshRequest(token);
        return authService.refresh(request);
    }
}
