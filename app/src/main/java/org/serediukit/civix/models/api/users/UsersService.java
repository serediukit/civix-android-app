package org.serediukit.civix.models.api.users;

import org.serediukit.civix.models.api.users.response.GetUserResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface UsersService {

    @GET("users/me")
    Call<GetUserResponse> getCurrentUser();
}
