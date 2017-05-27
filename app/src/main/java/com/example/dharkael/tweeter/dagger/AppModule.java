package com.example.dharkael.tweeter.dagger;

import android.app.Application;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.persistence.room.Room;

import com.example.dharkael.tweeter.data.AppDatabase;
import com.example.dharkael.tweeter.data.TweetDao;
import com.example.dharkael.tweeter.data.UserDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(subcomponents = {ViewModelSubComponent.class}, includes = {NetworkModule.class})
public class AppModule {
    private Application app;

    public AppModule(Application app){
        this.app = app;
    }

    @Provides
    @Singleton
    public Application provideApplication(){
        return app;
    }
    @Provides
    @Singleton
    public AppDatabase provideDatabase(Application app) {
        return Room.databaseBuilder(app,
                AppDatabase.class,
                AppDatabase.FAUX_DB_NAME)
                .build();
    }

    @Singleton
    @Provides
    UserDao provideUserDao(AppDatabase appDatabase){
        return appDatabase.userDao();
    }


    @Singleton
    @Provides
    TweetDao provideTweetDao(AppDatabase appDatabase){
        return appDatabase.tweetDao();
    }

    @Singleton
    @Provides
    ViewModelProvider.Factory provideViewModelFactory(
            ViewModelSubComponent.Builder viewModelSubComponent) {
        return new ViewModelFactory(viewModelSubComponent.build());
    }

}
