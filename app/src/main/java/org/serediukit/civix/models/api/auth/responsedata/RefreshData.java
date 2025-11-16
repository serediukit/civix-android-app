package org.serediukit.civix.models.api.auth.responsedata;

import com.squareup.moshi.Json;

public class RefreshData {

    @Json(name = "access_token")
    private TokenDetail accessTokenWrapper;

    @Json(name = "refresh_token")
    private TokenDetail refreshTokenWrapper;

    public TokenDetail getAccessTokenWrapper() {
        return accessTokenWrapper;
    }

    public TokenDetail getRefreshTokenWrapper() {
        return refreshTokenWrapper;
    }
}