package org.serediukit.civix.models.api.auth.response;

public class RefreshResponse {
    private final String accessToken;
    private final int accessExpiresAt;
    private final String refreshToken;
    private final int refreshExpiresAt;

    public RefreshResponse(
            String accessToken,
            int accessExpiresAt,
            String refreshToken,
            int refreshExpiresAt
    ) {
        this.accessToken = accessToken;
        this.accessExpiresAt = accessExpiresAt;
        this.refreshToken = refreshToken;
        this.refreshExpiresAt = refreshExpiresAt;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public int getAccessExpiresAt() {
        return accessExpiresAt;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public int getRefreshExpiresAt() {
        return refreshExpiresAt;
    }
}