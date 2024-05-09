package com.library.jupiter.annotation.api;

import com.library.jupiter.annotation.LoginApi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@LoginApi
public @interface AddBook {

}
