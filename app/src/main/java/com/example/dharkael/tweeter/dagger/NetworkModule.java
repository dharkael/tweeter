package com.example.dharkael.tweeter.dagger;


import com.example.dharkael.tweeter.api.TweetService;
import com.example.dharkael.tweeter.remote.MockTweetService;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;
import retrofit2.mock.BehaviorDelegate;
import retrofit2.mock.MockRetrofit;
import retrofit2.mock.NetworkBehavior;

@Module
public class NetworkModule {



    @Singleton
    @Provides
    Retrofit provideRetrofit(){
        return new Retrofit.Builder().baseUrl(TweetService.BASE_URL)
                    .addConverterFactory(MoshiConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();

    }



//    @Singleton
//    @Provides
//    TweetService provideTweetService(Retrofit retrofit){
//        return retrofit.create(TweetService.class);
//
//    }

    @Singleton
    @Provides
    TweetService provideTweetService(Retrofit retrofit){
        final MockRetrofit mockRetrofit = new MockRetrofit
                .Builder(retrofit)
                .networkBehavior(NetworkBehavior.create())
                .build();
        final BehaviorDelegate<TweetService> behaviorDelegate = mockRetrofit.create(TweetService.class);
        return new MockTweetService(behaviorDelegate);

    }

}
