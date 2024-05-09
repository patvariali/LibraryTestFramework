package com.library.jupiter.extension.apiMethods;

import com.library.api.LibraryApi;
import com.library.jupiter.annotation.api.AddBook;
import com.library.jupiter.extension.LoginApiExtension;
import com.library.model.apiModels.*;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;
import retrofit2.Response;

import java.io.IOException;
import java.util.Objects;

import static com.library.model.apiModels.BookJson.getRandomBook;
import static com.library.utilities.ApiUtilities.getOkHttpClient;
import static com.library.utilities.ApiUtilities.getRetrofit;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AddBookExtension implements BeforeEachCallback, ParameterResolver {
    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(AddBookExtension.class);


    @Override
    public void beforeEach(ExtensionContext context) {
        String token = context.getStore(LoginApiExtension.NAMESPACE).get(context.getUniqueId(), LoginResponse.class).getToken();

        LibraryApi libraryApi = getRetrofit(getOkHttpClient(token)).create(LibraryApi.class);

        AnnotationSupport.findAnnotation(
                context.getRequiredTestMethod(),
                AddBook.class
        ).ifPresent(
                getAllUsers -> {
                    try {
                        Allure.step("create a random 'book' as request body");
                        BookJson createdBook = getRandomBook();

                        Allure.step("send POST request to '/add_book' endpoint");
                        Response<UserOrBookResponse> response = libraryApi.addBook(createdBook).execute();
                        UserOrBookResponse userOrBookResponse = response.body();

                        Allure.step("Then status code should be 200, And Response Content type is 'application/json; charset=utf-8'", () -> {

                            assertEquals(200, response.code());
                            assertTrue(("application/json; charset=utf-8").contains(Objects.requireNonNull(response.headers().get("Content-Type"))));

                        });

                        context.getStore(NAMESPACE).put(context.getUniqueId(), TestParameters.createNewTestParameters(userOrBookResponse, createdBook));


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
                .isAssignableFrom(TestParameters.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId());
    }
}
