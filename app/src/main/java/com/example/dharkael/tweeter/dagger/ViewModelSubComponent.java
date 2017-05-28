package com.example.dharkael.tweeter.dagger;

import com.example.dharkael.tweeter.ui.login.LoginViewModel;
import com.example.dharkael.tweeter.ui.tweets.TweetsViewModel;

import dagger.Subcomponent;

@SuppressWarnings({"UnusedDeclaration"})
@Subcomponent
public interface ViewModelSubComponent {
    @Subcomponent.Builder
    interface Builder {
        ViewModelSubComponent build();
    }

    LoginViewModel loginViewModel();

    TweetsViewModel tweetsViewModel();

}
