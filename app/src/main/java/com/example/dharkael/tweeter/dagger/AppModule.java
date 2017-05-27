package com.example.dharkael.tweeter.dagger;

import android.app.Application;
import android.arch.lifecycle.ViewModelProvider;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(subcomponents = {ViewModelSubComponent.class}, includes = {NetworkModule.class})
public class AppModule {
    private Application app;

    public AppModule(Application app){
        this.app = app;
    }


    @Singleton
    @Provides
    ViewModelProvider.Factory provideViewModelFactory(
            ViewModelSubComponent.Builder viewModelSubComponent) {
        return new ViewModelFactory(viewModelSubComponent.build());
    }

}
