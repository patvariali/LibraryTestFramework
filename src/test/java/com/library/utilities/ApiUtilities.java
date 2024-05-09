package com.library.utilities;

import com.github.javafaker.Faker;
import io.qameta.allure.okhttp3.AllureOkHttp3;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class ApiUtilities {
    public static Retrofit getRetrofit(OkHttpClient okHttpClient){
        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://library2.cydeo.com/rest/v1/")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }

    public static OkHttpClient getOkHttpClient(final String token){

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder()
                        .header("x-library-token", token); // Добавляем токен в заголовок
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });
        httpClient.addInterceptor(new AllureOkHttp3()
                .setRequestTemplate("http-request.ftl")
                .setResponseTemplate("http-response.ftl"));

        return httpClient.build();
    }


    public static Map<String,Object> getRandomUserMap(){

        Faker faker = new Faker() ;
        Map<String,Object> userMap = new LinkedHashMap<>();
        String fullName = faker.name().fullName();
        String email=fullName.substring(0,fullName.indexOf(" "))+"@library";
        System.out.println(email);
        userMap.put("full_name", fullName );
        userMap.put("email", email);
        userMap.put("password", "libraryUser");
        // 2 is librarian as role
        userMap.put("user_group_id",2);
        userMap.put("status", "ACTIVE");
        userMap.put("start_date", "2023-03-11");
        userMap.put("end_date", "2024-03-11");
        userMap.put("address", faker.address().cityName());

        return userMap ;
    }
}
