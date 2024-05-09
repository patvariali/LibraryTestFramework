package com.library.model.apiModels;

import com.fasterxml.jackson.annotation.JsonProperty;


public record UserOrBookResponse(
        @JsonProperty("message")
        String message,

        @JsonProperty("user_id")
        int userId,

        @JsonProperty("book_id")
        int book_id) {

}

