package org.serediukit.civix.auth;

import android.content.Context;
import androidx.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AuthRepository {

    // Define navigation targets
    public enum NavigationTarget {
        MAIN_SCREEN,
        LOGIN_SCREEN
    }

    // Callback interface for the asynchronous result
    public interface AuthCheckCallback {
        void onResult(NavigationTarget target);
    }

    private final TokenManager tokenManager;
    private final Executor executor = Executors.newSingleThreadExecutor(); // For background work

    public AuthRepository(Context context) {
        this.tokenManager = new TokenManager(context);
    }

    /**
     * Executes the authentication check on a background thread.
     * @param callback The callback to receive the navigation result.
     */
    public void checkAuthenticationState(@NonNull AuthCheckCallback callback) {
        executor.execute(() -> {
            NavigationTarget target = checkTokenAndRefresh();
            callback.onResult(target);
        });
    }

    /**
     * Core logic: check for token, validate, refresh, and decide navigation.
     */
    private NavigationTarget checkTokenAndRefresh() {
        String token = tokenManager.getToken();

        if (token == null) {
            // Case 1: Token missing. Go to login.
            return NavigationTarget.LOGIN_SCREEN;
        }

        // --- Simulated Network Logic ---

        // 1. Validate Token (Quick local check or lightweight remote API call)
        boolean isValid = isTokenValid(token);

        if (!isValid) {
            // Case 2: Token found but invalid (e.g., expired signature). Clear and go to login.
            tokenManager.clearToken();
            return NavigationTarget.LOGIN_SCREEN;
        }

        // 2. Perform Refresh (Heavy remote API call)
        String newToken = attemptTokenRefresh(token);

        if (newToken != null) {
            // Case 3: Refresh successful. Save the new token and go to main.
            tokenManager.saveToken(newToken);
            return NavigationTarget.MAIN_SCREEN;
        } else {
            // Case 4: Refresh failed (e.g., server error, token invalidated). Clear and go to login.
            tokenManager.clearToken();
            return NavigationTarget.LOGIN_SCREEN;
        }
    }

    // --- Placeholder Methods for Network/Validation ---

    private boolean isTokenValid(String token) {
        // In a real app, this would involve checking the token expiry time (local check)
        // and potentially a quick API call to verify it hasn.
        try {
            // Simulate network latency (200ms)
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return true; // Assume token is valid for this example
    }

    private String attemptTokenRefresh(String token) {
        // In a real app, this is a heavy network call to exchange the old token/refresh token for a new pair.
        try {
            // Simulate network latency (500ms)
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        // Return the new token or null on failure.
        return "refreshed_jwt_token_12345"; // Return a simulated new token
    }
}