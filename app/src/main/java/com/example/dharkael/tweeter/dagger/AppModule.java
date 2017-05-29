package com.example.dharkael.tweeter.dagger;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.persistence.room.Room;

import com.example.dharkael.tweeter.RxSchedulers;
import com.example.dharkael.tweeter.api.TweetService;
import com.example.dharkael.tweeter.data.AppDatabase;
import com.example.dharkael.tweeter.data.TweetDao;
import com.example.dharkael.tweeter.data.UserDao;
import com.example.dharkael.tweeter.ui.login.LoginViewModel;
import com.example.dharkael.tweeter.ui.tweets.TweetsViewModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Module(subcomponents = {ViewModelSubComponent.class}, includes = {NetworkModule.class})
public class AppModule {
    private Application app;

    public AppModule(Application app) {
        this.app = app;
    }

    @Provides
    @Singleton
    public Application provideApplication() {
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
    UserDao provideUserDao(AppDatabase appDatabase) {
        return appDatabase.userDao();
    }


    @Singleton
    @Provides
    TweetDao provideTweetDao(AppDatabase appDatabase) {
        return appDatabase.tweetDao();
    }

    @Singleton
    @Provides
    ViewModelProvider.Factory provideViewModelFactory(
            ViewModelSubComponent.Builder viewModelSubComponent) {
        return new ViewModelFactory(viewModelSubComponent.build());
    }

    @Provides
    @Named("SingleThreaded")
    ExecutorService provideSingleThreadedExecutorService() {
        return Executors.newSingleThreadScheduledExecutor();
    }

    @Provides
    LoginViewModel provideLoginViewModel(TweetService tweetService, UserDao userDao, TweetDao tweetDao, @Named("SingleThreaded") ExecutorService executorService) {
        return new LoginViewModel(tweetService, userDao, tweetDao, new MutableLiveData<>(), new MutableLiveData<>(), executorService);
    }

    @Provides
    TweetsViewModel provideTweetsViewModel(TweetDao tweetDao,
                                           UserDao userDao,
                                           @Named("SingleThreaded") ExecutorService executorService) {
        return new TweetsViewModel(tweetDao, userDao, executorService);
    }

    @Provides
    @Singleton
    RxSchedulers provideRxSchedulers() {
        return new RxSchedulers() {
            @Override
            public Scheduler io() {
                return Schedulers.io();
            }

            @Override
            public Scheduler computation() {
                return Schedulers.computation();
            }

            @Override
            public Scheduler main() {
                return AndroidSchedulers.mainThread();
            }
        };
    }

}
