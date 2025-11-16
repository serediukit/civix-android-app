package org.serediukit.civix.models.api.auth;

import org.serediukit.civix.models.api.auth.request.LoginRequest;
import org.serediukit.civix.models.api.auth.request.RefreshRequest;
import org.serediukit.civix.models.api.auth.response.LoginResponse;
import org.serediukit.civix.models.api.auth.response.RefreshResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {

    @POST("auth/login") // Assuming a simple login endpoint
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("auth/refresh") // Assuming a simple login endpoint
    Call<RefreshResponse> refresh(@Body RefreshRequest request);
}
