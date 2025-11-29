package org.serediukit.civix.models.api.users.response;

import com.squareup.moshi.Json;

import org.serediukit.civix.models.api.users.responsedata.GetUserData;

public class GetUserResponse {
    @Json(name = "success")
    private boolean success;

    @Json(name = "data")
    private GetUserData data;

    public GetUserResponse() {
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public GetUserData getData() {
        return data;
    }

    public void setData(GetUserData data) {
        this.data = data;
    }
}
