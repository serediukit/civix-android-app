package org.serediukit.civix.models.api.auth.request;

public class RefreshRequest {
    private final String token;

    public RefreshRequest(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}