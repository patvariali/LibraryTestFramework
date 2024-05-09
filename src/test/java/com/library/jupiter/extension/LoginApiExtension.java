package com.library.jupiter.extension;

import com.library.api.LibraryApi;
import com.library.jupiter.annotation.LoginApi;
import com.library.model.apiModels.LoginRequest;
import com.library.model.apiModels.LoginResponse;
import com.library.model.apiModels.UserJson;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;
import retrofit2.Response;

import java.io.IOException;
import java.util.Objects;

import static com.library.utilities.ApiUtilities.*;


public class LoginApiExtension implements BeforeEachCallback {
    public static final ExtensionContext.Namespace NAMESPACE
            = ExtensionContext.Namespace.create(LoginApiExtension.class);

    private final OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

    @Override
    public void beforeEach(ExtensionContext context)  {
        LibraryApi libraryApi = getRetrofit(okHttpClient).create(LibraryApi.class);

        UserJson currentUser = context.getStore(UserExtension.NAMESPACE).get(context.getUniqueId(), UserJson.class);


        AnnotationSupport.findAnnotation(
                context.getRequiredTestMethod(),
                LoginApi.class
        ).ifPresent(
                api -> {
                    LoginRequest loginRequest = new LoginRequest(currentUser.email(), currentUser.testData().password());
                    try {

                        Response<LoginResponse> response = libraryApi.login(loginRequest).execute();
                        LoginResponse loginResponse = response.body();
                        context.getStore(NAMESPACE).put(context.getUniqueId(), loginResponse);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
        );

    }


}
