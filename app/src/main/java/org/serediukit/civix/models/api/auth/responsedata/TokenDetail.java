package org.serediukit.civix.models.api.auth.responsedata;

import com.squareup.moshi.Json;

public class TokenDetail {
    @Json(name = "token")
    private String token;

    @Json(name = "expires_at")
    private long expiresAt;

    public String getToken() {
        return token;
    }
}
