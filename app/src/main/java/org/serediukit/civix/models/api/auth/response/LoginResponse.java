package org.serediukit.civix.models.api.auth.response;

import com.squareup.moshi.Json;

import org.serediukit.civix.models.api.auth.responsedata.LoginData;

public class LoginResponse {
    @Json(name = "data")
    private LoginData loginData;

    @Json(name = "success")
    private boolean success;

    public String getAccessToken() {
        // Check for nulls defensively
        if (loginData != null && loginData.getAccessTokenWrapper() != null) {
            return loginData.getAccessTokenWrapper().getToken();
        }
        return null;
    }

    public String getRefreshToken() {
        if (loginData != null && loginData.getRefreshTokenWrapper() != null) {
            return loginData.getRefreshTokenWrapper().getToken();
        }
        return null;
    }

    public boolean isSuccess() {
        return success;
    }
}