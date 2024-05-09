package com.library.jupiter.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/* Scenario: Retrieve all users from the API endpoint
    Given the user logged in Library API as a "librarian"
    And Accept header is "application/json"
    When the user sends GET request to "/get_all_users" endpoint
    Then status code should be 200
    And Response Content type is "application/json; charset=utf-8"
    And "id" field should not be null
    And "name" field should not be null  */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface LoginApi {


}
