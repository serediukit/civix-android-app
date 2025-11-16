package org.serediukit.civix.models;

import android.content.Context;
import android.util.Log;

import org.serediukit.civix.models.api.auth.AuthClient;
import org.serediukit.civix.models.api.auth.response.RefreshResponse;

import retrofit2.Response;

public class LoaderModel {
    private final AuthClient authClient;

    public LoaderModel(Context context) {
        authClient = AuthClient.getInstance(context);
    }

    public RefreshResponse refreshTokens(String refreshToken) {
        try {
            Response<RefreshResponse> response = authClient.refresh(refreshToken).execute();
            if (response.isSuccessful() && response.body() != null) {
                return response.body();
            } else {
                throw new Exception("auth service error");
            }
        } catch (Exception e) {
            Log.e("LOADER MODEL", e.toString());
        }

        return null;
    }
}
