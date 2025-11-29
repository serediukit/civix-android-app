package org.serediukit.civix.models.api.auth.response;

import com.squareup.moshi.Json;

import org.serediukit.civix.models.api.auth.responsedata.RegisterData;

public class RegisterResponse {
    @Json(name = "data")
    private RegisterData data;

    @Json(name = "success")
    private boolean success;

    public RegisterData getData() {
        return data;
    }

    public boolean isSuccess() {
        return success;
    }
}