package com.library.model.apiModels;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginRequest {
    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    private String password;
    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

}
