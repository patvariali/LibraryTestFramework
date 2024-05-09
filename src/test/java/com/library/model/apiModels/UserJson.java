package com.library.model.apiModels;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UserJson(
        @JsonProperty("id")
        String id,
        @JsonProperty("password")
        String password,
        @JsonProperty("full_name")
        String fullName,

        @JsonProperty("email")
        String email,

        @JsonProperty("user_group_id")
        String userGroupId,

        @JsonProperty("token")
        String token,

        @JsonProperty("image")
        String image,

        @JsonProperty("status")
        String status,

        @JsonProperty("is_admin")
        String isAdmin,

        @JsonProperty("start_date")
        @JsonFormat(pattern = "yyyy-MM-dd")
        Date startDate,

        @JsonProperty("end_date")
        @JsonFormat(pattern = "yyyy-MM-dd")
        Date endDate,

        @JsonProperty("address")
        String address,

        @JsonIgnore
        TestData testData
) {
        public static UserJson simpleUser (String email, String password) {
                return new UserJson(
                        null,
                        null,
                        null,
                        email,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        new TestData(password)
                );
        }

        public String email() {
                return email;
        }

        public TestData testData() {
                return testData;
        }
}

