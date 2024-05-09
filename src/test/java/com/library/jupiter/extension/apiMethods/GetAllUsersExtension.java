package com.library.jupiter.extension.apiMethods;

import com.library.api.LibraryApi;
import com.library.jupiter.annotation.api.GetAllUsers;
import com.library.jupiter.extension.LoginApiExtension;
import com.library.model.apiModels.AllUserModel;
import com.library.model.apiModels.LoginResponse;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static com.library.utilities.ApiUtilities.getOkHttpClient;
import static com.library.utilities.ApiUtilities.getRetrofit;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GetAllUsersExtension implements BeforeEachCallback, ParameterResolver {
    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(GetAllUsersExtension.class);


    @Override
    public void beforeEach(ExtensionContext context) {
        String token = context.getStore(LoginApiExtension.NAMESPACE).get(context.getUniqueId(), LoginResponse.class).getToken();

        LibraryApi libraryApi = getRetrofit(getOkHttpClient(token)).create(LibraryApi.class);

        AnnotationSupport.findAnnotation(
                context.getRequiredTestMethod(),
                GetAllUsers.class
        ).ifPresent(
                getAllUsers -> {
                    try {
                        Allure.step("Send GET to /get_all_users");
                        Response<List<AllUserModel>> response = libraryApi.getAllUsers().execute();
                        List<AllUserModel> result = response.body();

                        Allure.step("Then status code should be 200\n" +
                                "And Response Content type is 'application/json; charset=utf-8'", () -> {
                            assertEquals(200, response.code());
                            assertTrue(("application/json; charset=utf-8").contains((Objects.requireNonNull(response.headers().get("Content-Type")))));
                        });


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
                .isAssignableFrom(List.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId());
    }
}
