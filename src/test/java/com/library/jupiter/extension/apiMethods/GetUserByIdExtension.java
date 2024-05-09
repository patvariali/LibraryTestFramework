package com.library.jupiter.extension.apiMethods;

import com.library.api.LibraryApi;
import com.library.jupiter.annotation.api.GetUserById;
import com.library.jupiter.extension.LoginApiExtension;
import com.library.model.apiModels.LoginResponse;
import com.library.model.apiModels.UserJson;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;
import retrofit2.Response;

import java.io.IOException;
import java.util.Objects;

import static com.library.utilities.ApiUtilities.getOkHttpClient;
import static com.library.utilities.ApiUtilities.getRetrofit;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GetUserByIdExtension implements BeforeEachCallback, ParameterResolver {
    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(GetUserByIdExtension.class);


    @Override
    public void beforeEach(ExtensionContext context) {

        String token = context.getStore(LoginApiExtension.NAMESPACE).get(context.getUniqueId(), LoginResponse.class).getToken();

        LibraryApi libraryApi = getRetrofit(getOkHttpClient(token)).create(LibraryApi.class);

        AnnotationSupport.findAnnotation(
                context.getRequiredTestMethod(),
                GetUserById.class
        ).ifPresent(
                getUserById -> {
                    try {
                        Allure.step("/get_user_by_id/" + getUserById.id());
                        Response<UserJson> response = libraryApi.getUserById(getUserById.id()).execute();
                        UserJson result = response.body();

                        Allure.step("Then status code should be 200\n" +
                                "And Response Content type is 'application/json; charset=utf-8'", () -> {
                            assertEquals(200, response.code());
                            assertTrue(Objects.requireNonNull(response.headers().get("Content-Type")).contains("application/json; charset=utf-8"));
                        });

                        Allure.step("And 'id' field should be same with path param", () -> assertThat(result.id(), is(getUserById.id())));


                        context.getStore(NAMESPACE).put(context.getUniqueId(), result);

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext
                .getParameter()
                .getType()
                .isAssignableFrom(UserJson.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId());
    }
}
