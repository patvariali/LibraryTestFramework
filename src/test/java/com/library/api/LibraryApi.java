package com.library.api;

import com.library.model.apiModels.*;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface LibraryApi {
    @POST("login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @GET("get_all_users")
    Call<List<AllUserModel>> getAllUsers();

    @GET("get_user_by_id/{id}")
    Call<UserJson> getUserById(@Path("id") String userId);

    @POST("add_book")
    Call<UserOrBookResponse> addBook(@Body BookJson bookJson);

    @FormUrlEncoded
    @POST("add_user")
    Call<UserOrBookResponse> addUser(@Body UserJson userJson);

    @POST("decode")
    Call<UserJson> decode(@Body String token);

}
