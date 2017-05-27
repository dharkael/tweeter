package com.example.dharkael.tweeter;

import android.app.Application;

import com.example.dharkael.tweeter.dagger.AppComponent;
import com.example.dharkael.tweeter.dagger.AppModule;
import com.example.dharkael.tweeter.dagger.DaggerAppComponent;


public class TweeterApp extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        initAppComponent();
    }

    public AppComponent appComponent() {
        return appComponent;
    }

    private void initAppComponent() {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }
}
