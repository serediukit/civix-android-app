package org.serediukit.civix.models.api.auth;

// RefreshAuthenticator.java

import okhttp3.Authenticator;
import okhttp3.Route;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;

import org.serediukit.civix.models.api.auth.response.RefreshResponse;
import org.serediukit.civix.models.entities.JWTTokens;
import org.serediukit.civix.models.token.TokenManager;
import org.serediukit.civix.models.api.auth.response.LoginResponse;
import org.serediukit.civix.models.api.auth.request.RefreshRequest;

import java.io.IOException;

public class RefreshAuthenticator implements Authenticator {

    private final TokenManager tokenManager;
    private final Retrofit baseRetrofit; // Used to create the refresh service

    public RefreshAuthenticator(TokenManager tokenManager, Retrofit baseRetrofit) {
        this.tokenManager = tokenManager;
        this.baseRetrofit = baseRetrofit;
    }

    @Override
    public Request authenticate(Route route, Response response) throws IOException {
        // 1. Check if the request failed due to authentication failure (401)
        if (response.code() == 401) {

            // IMPORTANT: Prevent infinite loop if the refresh token also fails
            // Check if we are already trying to refresh the token
            String originalToken = response.request().header("Authorization");
            String storedAccessToken = tokenManager.getAccessToken();

            if (originalToken == null || !originalToken.contains(storedAccessToken)) {
                // If the token in the header doesn't match the current stored token,
                // it means another thread already refreshed the token. Retry the call.
                return response.request().newBuilder()
                        .header("Authorization", "Bearer " + tokenManager.getAccessToken())
                        .build();
            }

            // 2. Perform the synchronous token refresh call
            String refreshToken = tokenManager.getRefreshToken();
            if (refreshToken == null) {
                // Cannot refresh, clear tokens and signal logout is needed
                tokenManager.clearTokens();
                return null;
            }

            // --- SYNCHRONOUS REFRESH CALL ---

            // Create a dedicated client for the refresh call to prevent the Authenticator
            // from recursively calling itself (StackOverflowError).
            // We strip any interceptors/authenticators here.
            OkHttpClient refreshClient = new OkHttpClient.Builder().build();

            Retrofit refreshRetrofit = baseRetrofit.newBuilder()
                    .client(refreshClient)
                    .build();

            AuthService authService = refreshRetrofit.create(AuthService.class);
            RefreshRequest refreshRequest = new RefreshRequest(refreshToken);

            Call<RefreshResponse> refreshCall = authService.refresh(refreshRequest);
            retrofit2.Response<RefreshResponse> refreshResponse = refreshCall.execute(); // Must be synchronous

            if (refreshResponse.isSuccessful() && refreshResponse.body() != null) {
                // 3. Refresh successful: Save new tokens
                RefreshResponse newTokens = refreshResponse.body();
                tokenManager.saveTokens(new JWTTokens(newTokens.getAccessToken(), newTokens.getRefreshToken()));

                // 4. Retry the original failed request with the NEW Access Token
                return response.request().newBuilder()
                        .header("Authorization", "Bearer " + tokenManager.getAccessToken())
                        .build();
            } else {
                // 5. Refresh failed: Tokens are invalid, clear them and force logout
                tokenManager.clearTokens();
                return null;
            }
        }

        // Return null for other status codes (e.g., 403 Forbidden, 500 Server Error)
        return null;
    }
}