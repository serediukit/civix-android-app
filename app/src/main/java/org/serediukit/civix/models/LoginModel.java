package org.serediukit.civix.models;

import android.content.Context;
import android.util.Log;

import org.serediukit.civix.models.api.auth.AuthClient;
import org.serediukit.civix.models.api.auth.response.LoginResponse;

import retrofit2.Response;

public class LoginModel {
    private final AuthClient authClient;

    public LoginModel(Context context) {
        authClient = AuthClient.getInstance(context);
    }

    public LoginResponse login(String email, String password) {
        try {
            Response<LoginResponse> response = authClient.login(email, password).execute();
            if (response.isSuccessful() && response.body() != null) {
                Log.d("LOGIN MODEL | LOGIN",  response.body().toString());
                return response.body();
            } else {
                throw new Exception("auth service error");
            }
        } catch (Exception e) {
            Log.e("LOGIN MODEL | LOGIN", e.toString());
        }

        return null;
    }
}
