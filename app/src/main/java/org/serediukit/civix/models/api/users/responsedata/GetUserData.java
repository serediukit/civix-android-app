package org.serediukit.civix.models.api.users.responsedata;

import com.squareup.moshi.Json;

import org.serediukit.civix.models.entities.user.User;

public class GetUserData {
    @Json(name = "user")
    private User user;

    public GetUserData() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
