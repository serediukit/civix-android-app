package org.serediukit.civix.models;

import android.util.Log;

import org.serediukit.civix.models.api.auth.AuthClient;
import org.serediukit.civix.models.api.auth.response.RefreshResponse;
import org.serediukit.civix.models.entities.JWTTokens;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Response;

public class LoaderModel {
    private AuthClient authClient;

    public LoaderModel() {
        authClient = AuthClient.getInstance();
    }

    public JWTTokens refreshTokens(String refreshToken) {
        try {
            Response<RefreshResponse> response = authClient.refresh(refreshToken).execute();
            if (response.isSuccessful() && response.body() != null) {
                RefreshResponse data = response.body();
                return new JWTTokens(data.getAccessToken(), data.getRefreshToken());
            } else {
                throw new Exception("auth service error");
            }
        } catch (Exception e) {
            Log.e("LOADER MODEL", e.toString());
        }

        return null;
    }
}
