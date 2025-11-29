package org.serediukit.civix.models.api.auth;

import org.serediukit.civix.models.api.auth.request.LoginRequest;
import org.serediukit.civix.models.api.auth.request.RefreshRequest;
import org.serediukit.civix.models.api.auth.request.RegisterRequest;
import org.serediukit.civix.models.api.auth.response.LoginResponse;
import org.serediukit.civix.models.api.auth.response.RefreshResponse;
import org.serediukit.civix.models.api.auth.response.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {

    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("auth/refresh")
    Call<RefreshResponse> refresh(@Body RefreshRequest request);

    @POST("auth/register")
    Call<RegisterResponse> register(@Body RegisterRequest request);
}
