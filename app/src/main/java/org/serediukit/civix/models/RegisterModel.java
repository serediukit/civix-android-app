package org.serediukit.civix.models;

import android.content.Context;
import android.util.Log;

import org.serediukit.civix.models.api.auth.AuthClient;
import org.serediukit.civix.models.api.auth.response.RegisterResponse;
import org.serediukit.civix.models.entities.city.Location;

import retrofit2.Response;

public class RegisterModel {
    private final AuthClient authClient;

    public RegisterModel(Context context) {
        authClient = AuthClient.getInstance(context);
    }

    public RegisterResponse register(String email, String password, String name, String surname, String phoneNumber, Location location) {
        try {
            Response<RegisterResponse> response = authClient.register(email, password, name, surname, phoneNumber, location).execute();
            if (response.isSuccessful() && response.body() != null) {
                Log.d("REGISTER MODEL | REGISTER", response.body().toString());
                return response.body();
            } else {
                throw new Exception("auth service error");
            }
        } catch (Exception e) {
            Log.e("REGISTER MODEL | REGISTER", e.toString());
        }

        return null;
    }
}