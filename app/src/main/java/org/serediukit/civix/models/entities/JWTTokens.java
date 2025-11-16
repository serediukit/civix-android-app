package org.serediukit.civix.models.entities;

public class JWTTokens {
    private final String accessToken;
    private final String refreshToken;

    public JWTTokens(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
