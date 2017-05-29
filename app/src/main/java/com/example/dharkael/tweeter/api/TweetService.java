package com.example.dharkael.tweeter.api;

import com.example.dharkael.tweeter.data.entities.Tweet;
import com.example.dharkael.tweeter.data.entities.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface TweetService {
    String BASE_URL = "https://example.org";
    String LOGIN_URL = "/login";
    String FOLLOWING = "/following/{userId}";
    String TIMELINE = "/timeline/{userid}/{sinceId}";

    @POST(LOGIN_URL)
    Call<LoginResponse> login(@Body LoginBody body);

    @GET(FOLLOWING)
    Call<List<User>> following(@Path("userId")String userId);

    @GET(TIMELINE)
    Call<List<Tweet>> timeline(@Path("userId")String userId, long sinceId);



}
