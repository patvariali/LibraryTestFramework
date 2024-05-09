package com.library.model.apiModels;

import io.qameta.allure.internal.shadowed.jackson.annotation.JsonIgnoreProperties;
import io.qameta.allure.internal.shadowed.jackson.annotation.JsonProperty;
import lombok.Getter;
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginResponse {
    @JsonProperty("token")
    private String token;

    @JsonProperty("redirect_uri")
    private String redirect_uri;

}