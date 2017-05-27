package com.example.dharkael.tweeter.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface TweetService {
    String BASE_URL = "https://example.org";
    String LOGIN_URL = "/login";

    @POST(LOGIN_URL)
    Call<LoginResponse> login(@Body LoginBody body);

}
