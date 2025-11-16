package org.serediukit.civix.models;

import android.util.Log;

import org.serediukit.civix.models.api.auth.AuthClient;
import org.serediukit.civix.models.api.auth.response.LoginResponse;

import retrofit2.Response;

public class LoginModel {
    private final AuthClient authClient;

    public LoginModel() {
        authClient = AuthClient.getInstance();
    }

    public LoginResponse login(String email, String password) {
        try {
            Response<LoginResponse> response = authClient.login(email, password).execute();
            if (response.isSuccessful() && response.body() != null) {
                Log.d("LOGIN MODEL",  response.body().toString());
                return response.body();
            } else {
                throw new Exception("auth service error");
            }
        } catch (Exception e) {
            Log.e("LOGIN MODEL", e.toString());
        }

        return null;
    }
}
