package org.serediukit.civix.models.api.auth.request;

import com.squareup.moshi.Json;

public class RefreshRequest {
    @Json(name = "refresh_token")
    private final String token;

    public RefreshRequest(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}