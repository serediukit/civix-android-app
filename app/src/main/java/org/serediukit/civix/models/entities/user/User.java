package org.serediukit.civix.models.entities.user;

import com.squareup.moshi.Json;

public class User {
    @Json(name = "email")
    private String email;

    @Json(name = "name")
    private String name;

    @Json(name = "surname")
    private String surname;

    @Json(name = "phone_number")
    private String phoneNumber;

    @Json(name = "avatar_url")
    private String avatarUrl;

    @Json(name = "reg_time")
    private String regTime;

    @Json(name = "upd_time")
    private String updTime;

    @Json(name = "del_time")
    private String delTime;

    public User() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getRegTime() {
        return regTime;
    }

    public void setRegTime(String regTime) {
        this.regTime = regTime;
    }

    public String getUpdTime() {
        return updTime;
    }

    public void setUpdTime(String updTime) {
        this.updTime = updTime;
    }

    public String getDelTime() {
        return delTime;
    }

    public void setDelTime(String delTime) {
        this.delTime = delTime;
    }
}
