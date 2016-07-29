package com.hand.demo.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;

public class PersistentLogin {

    @NotNull
    private String series;

    @NotNull
    private String username;

    @NotNull
    private String token;

    @NotNull
    private Date last_used;

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getLast_used() {
        return last_used;
    }

    public void setLast_used(Date last_used) {
        this.last_used = last_used;
    }
}
