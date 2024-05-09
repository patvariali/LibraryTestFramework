package com.library.jupiter.extension;

import com.library.jupiter.annotation.User;
import com.library.model.apiModels.UserJson;
import com.library.utilities.ConfigurationReader;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class UserExtension implements BeforeEachCallback {
    public static final ExtensionContext.Namespace NAMESPACE
            = ExtensionContext.Namespace.create(UserExtension.class);

    private static final Map<User.UserType, UserJson> USERS = new ConcurrentHashMap<>();
    static {
        USERS.put(User.UserType.LIBRARIAN, UserJson.simpleUser(ConfigurationReader.getProperty("username"), ConfigurationReader.getProperty("password")));
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
