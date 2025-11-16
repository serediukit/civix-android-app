package org.serediukit.civix.models.api.auth;

import com.squareup.moshi.Moshi;

import org.serediukit.civix.models.api.BaseHTTPClient;
import org.serediukit.civix.models.api.auth.request.LoginRequest;
import org.serediukit.civix.models.api.auth.request.RefreshRequest;
import org.serediukit.civix.models.api.auth.response.LoginResponse;
import org.serediukit.civix.models.api.auth.response.RefreshResponse;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class AuthClient implements BaseHTTPClient {
    private static AuthClient instance;
    private final AuthService authService;

    private AuthClient() {
        Moshi moshi = new Moshi.Builder()
                // Add any custom adapters here if needed
                .build();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                // Add interceptors for logging, headers, etc.
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build();

        authService = retrofit.create(AuthService.class);
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
