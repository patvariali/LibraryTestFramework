package com.library.model.apiModels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AllUserModel {
    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

}
