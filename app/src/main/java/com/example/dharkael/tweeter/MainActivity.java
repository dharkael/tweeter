package com.example.dharkael.tweeter;

import android.arch.lifecycle.LifecycleActivity;
import android.content.Intent;
import android.os.Bundle;

import com.example.dharkael.tweeter.data.UserDao;
import com.example.dharkael.tweeter.data.entities.AuthenticatedUserId;
import com.example.dharkael.tweeter.ui.login.LoginActivity;
import com.example.dharkael.tweeter.ui.tweets.TweetsActivity;

import javax.inject.Inject;

public class MainActivity extends LifecycleActivity {

    @Inject
    UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((TweeterApp) getApplication()).appComponent().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        userDao.getAuthenticatedUserId().observe(this,this::maybeGotoLogin);
    }

    private void maybeGotoLogin(AuthenticatedUserId authenticatedUserId){
        if (authenticatedUserId == null) {
            gotoLogin();
        } else {
            gotoTweets(authenticatedUserId.userId);
        }
    }

    private void gotoLogin() {
        startActivity(LoginActivity.intentForStart(this));
    }

    private void gotoTweets(String userId){
        final Intent intentForStart = TweetsActivity.intentForStart(this, userId);
        startActivity(intentForStart);
    }
}
