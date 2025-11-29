package org.serediukit.civix.models.api.auth.request;

import com.squareup.moshi.Json;

import org.serediukit.civix.models.entities.city.Location;

public class RegisterRequest {
    @Json(name = "email")
    private final String email;

    @Json(name = "password")
    private final String password;

    @Json(name = "name")
    private final String name;

    @Json(name = "surname")
    private final String surname;

    @Json(name = "phone_number")
    private final String phoneNumber;

    @Json(name = "location")
    private final Location location;

    public RegisterRequest(String email, String password, String name, String surname, String phoneNumber, Location location) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.location = location;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Location getLocation() {
        return location;
    }
}