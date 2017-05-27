package com.example.dharkael.tweeter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.dharkael.tweeter.ui.login.LoginActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        gotoLogin();

    }

    private void gotoLogin() {
        startActivity(LoginActivity.intentForStart(this));
    }
}
