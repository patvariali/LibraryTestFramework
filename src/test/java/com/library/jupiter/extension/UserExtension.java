package com.library.jupiter.extension;

import com.library.jupiter.annotation.User;
import com.library.model.apiModels.UserJson;
import io.qameta.allure.Allure;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

@Slf4j
public class UserExtension implements BeforeEachCallback {
    public static final ExtensionContext.Namespace NAMESPACE
            = ExtensionContext.Namespace.create(UserExtension.class);

    private static final Map<User.UserType, UserJson> USERS = new ConcurrentHashMap<>();
    static {
        USERS.put(User.UserType.LIBRARIAN, UserJson.simpleUser("librarian10@library", "libraryUser"));
        USERS.put(User.UserType.STUDENT, UserJson.simpleUser("someEmail", "somePassword"));

    }



    @Override
    public void beforeEach(ExtensionContext context) {

        AnnotationSupport.findAnnotation(
                context.getRequiredTestMethod(),
                User.class
        ).ifPresent(
                api -> {
                    Allure.step("Log in as " + api.userType());
                    UserJson currentUser = USERS.get(User.UserType.valueOf(api.userType().name()));
                    context.getStore(NAMESPACE).put(context.getUniqueId(), Objects.requireNonNull(currentUser));
                }
        );
    }

}
