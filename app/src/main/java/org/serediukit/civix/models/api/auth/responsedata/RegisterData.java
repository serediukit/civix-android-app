package org.serediukit.civix.models.api.auth.responsedata;

import com.squareup.moshi.Json;

public class RegisterData {
    @Json(name = "email")
    private String email;

    @Json(name = "name")
    private String name;

    @Json(name = "reg_time")
    private String regTime;

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getRegTime() {
        return regTime;
    }
}