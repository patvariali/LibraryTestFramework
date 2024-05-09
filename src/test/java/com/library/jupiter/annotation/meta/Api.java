package com.library.jupiter.annotation.meta;

import com.library.jupiter.extension.apiMethods.AddBookExtension;
import com.library.jupiter.extension.apiMethods.GetAllUsersExtension;
import com.library.jupiter.extension.LoginApiExtension;
import com.library.jupiter.extension.UserExtension;
import com.library.jupiter.extension.apiMethods.GetUserByIdExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ExtendWith({
        UserExtension.class,
        LoginApiExtension.class,
        GetAllUsersExtension.class,
        GetUserByIdExtension.class,
        AddBookExtension.class
})

public @interface Api {
}
