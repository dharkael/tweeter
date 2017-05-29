package com.example.dharkael.tweeter;

import android.arch.lifecycle.LifecycleActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.dharkael.tweeter.data.UserDao;
import com.example.dharkael.tweeter.data.entities.AuthenticatedUserId;
import com.example.dharkael.tweeter.ui.login.LoginActivity;
import com.example.dharkael.tweeter.ui.tweets.TweetsActivity;

import javax.inject.Inject;

public class MainActivity extends LifecycleActivity {

    @Inject
    UserDao userDao;
    private AuthenticatedUserId lastAuthUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((TweeterApp) getApplication()).appComponent().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        userDao.getAuthenticatedUserId().observe(this, this::maybeGotoLogin);
    }

    @Override
    protected void onPause() {
        userDao.getAuthenticatedUserId().removeObserver(this::maybeGotoLogin);
        lastAuthUserId = null;
        super.onPause();
    }

    private void maybeGotoLogin(AuthenticatedUserId authenticatedUserId) {
        if(lastAuthUserId != null && lastAuthUserId.equals(authenticatedUserId)) return;
        lastAuthUserId = authenticatedUserId;
        Log.i("MainActivity", "maybeGotoLogin " + authenticatedUserId);
        if (authenticatedUserId == null) {
            gotoLogin();
        } else {
            gotoTweets(authenticatedUserId.userId);
        }
    }

    private void gotoLogin() {
        startActivity(LoginActivity.intentForStart(this));
    }

    private void gotoTweets(String userId) {
        final Intent intentForStart = TweetsActivity.intentForStart(this, userId);
        startActivity(intentForStart);
    }
}
