package com.example.dharkael.tweeter.dagger;

import com.example.dharkael.tweeter.MainActivity;
import com.example.dharkael.tweeter.ui.login.LoginActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    void inject(LoginActivity loginActivity);

    void inject(MainActivity mainActivity);
}
