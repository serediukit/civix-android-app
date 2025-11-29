package org.serediukit.civix.viewmodels;

import android.util.Log;

import org.serediukit.civix.models.RegisterModel;
import org.serediukit.civix.models.api.auth.response.RegisterResponse;
import org.serediukit.civix.models.entities.city.Location;
import org.serediukit.civix.util.uicodes.UICode;

public class RegisterViewModel {
    private final RegisterModel registerModel;

    public RegisterViewModel(RegisterModel registerModel) {
        this.registerModel = registerModel;
    }

    public UICode register(String email, String password, String name, String surname, String phoneNumber, Location location) {
        RegisterResponse resp = registerModel.register(email, password, name, surname, phoneNumber, location);
        if (resp == null || !resp.isSuccess()) {
            return UICode.AuthError;
        }

        Log.d("REGISTER VIEWMODEL | REGISTER", "User registered: " + resp.getData().getEmail());
        return UICode.Ok;
    }
}