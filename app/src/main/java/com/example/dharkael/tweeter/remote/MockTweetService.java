package com.example.dharkael.tweeter.remote;


import com.example.dharkael.tweeter.api.LoginBody;
import com.example.dharkael.tweeter.api.LoginResponse;
import com.example.dharkael.tweeter.api.TweetService;

import retrofit2.Call;
import retrofit2.mock.BehaviorDelegate;

public class MockTweetService implements TweetService {

    private final String USER_NAME = "SomeUserId";
    private final String USER_PASSWORD = "SomePassword";


    private BehaviorDelegate<TweetService> behaviorDelegate;

    public MockTweetService(BehaviorDelegate<TweetService> behaviorDelegate){
        this.behaviorDelegate = behaviorDelegate;
    }
    @Override
    public Call<LoginResponse> login(LoginBody body) {

        if(body == null){
            final IllegalArgumentException exception = new IllegalArgumentException("body must mot be null");
            return behaviorDelegate.returningResponse( new LoginResponse(exception)).login(null);
        }
        if(USER_NAME.equals(body.username) && USER_PASSWORD.equals(body.password)){
            return behaviorDelegate.returningResponse(new LoginResponse(USER_NAME)).login(body);
        }

        return behaviorDelegate.returningResponse(new LoginResponse(new Exception("Invalid username, password"))).login(body);
    }


}
