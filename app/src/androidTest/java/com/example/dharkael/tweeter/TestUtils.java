package com.example.dharkael.tweeter;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.test.rule.UiThreadTestRule;

import com.example.dharkael.tweeter.data.entities.Tweet;
import com.example.dharkael.tweeter.data.entities.User;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class TestUtils {
    static final String USER_ID = "user_id";
    static final String USER_ID1 = "user_id1";
    static final String USER_NAME = "user_name";
    static final String USER_NAME1 = "user_name1";
    static final long THEN = 0;
    static final long NOW = 1;

    static final String TWEET_ID = "TweetId";
    static final String TWEET_ID1 = "TweetId1";

    static public User createUser() {
        return new User(USER_ID, USER_NAME, THEN);
    }

    static public User createUserUpdated() {
        return new User(USER_ID, USER_NAME, NOW);
    }

    static public User createUser1() {
        return new User(USER_ID1, USER_NAME1, THEN);
    }

    static public User createUser1Updated() {
        return new User(USER_ID1, USER_NAME1, NOW);
    }

    static public Tweet creatTweet(String userId, String id, long createdAt) {
        return new Tweet(id, createdAt, id + " text", userId);
    }

    static public Tweet creatTweet() {
        return creatTweet(USER_ID, TWEET_ID, THEN);
    }


    static public Tweet creatUpdatedTweet() {
        return creatTweet(USER_ID, TWEET_ID, NOW);
    }

    static public Tweet creatTweet1() {
        return creatTweet(USER_ID, TWEET_ID1, THEN);
    }

    static public Tweet creatUpdatedTweet1() {
        return creatTweet(USER_ID, TWEET_ID1, NOW);
    }

    public static  <T> T getValue(LiveData<T> liveData, UiThreadTestRule uiThreadTestRule) throws InterruptedException {
        AtomicReference<T> reference = new AtomicReference<>();
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final Observer<T> observer = new Observer<T>() {
            @Override
            public void onChanged(@Nullable T t) {
                reference.set(t);
                countDownLatch.countDown();
                try {
                    uiThreadTestRule.runOnUiThread(() -> liveData.removeObserver(this));
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        };
        liveData.observeForever(observer);
        countDownLatch.await(2, TimeUnit.SECONDS);

        return reference.get();
    }

}
