package com.example.dharkael.tweeter.ui.tweets;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.dharkael.tweeter.data.TweetDao;
import com.example.dharkael.tweeter.data.UserDao;
import com.example.dharkael.tweeter.data.entities.Tweet;
import com.example.dharkael.tweeter.data.entities.User;
import com.example.dharkael.tweeter.data.pojos.TweetAndSender;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;


public class TweetsViewModel extends ViewModel {
    private TweetDao tweetDao;
    private UserDao userDao;
    private String userId;
    private LiveData<List<TweetAndSender>> tweetAndSenders;
    private LiveData<User> user;
    private final ExecutorService executorService;
    public TweetsViewModel(TweetDao tweetDao, UserDao userDao, ExecutorService executorService) {
        this.tweetDao = tweetDao;
        this.userDao = userDao;
        this.executorService = executorService;
    }

    public void setUserId(String userId) {
        if (this.userId != null && this.userId.equals(userId)) {
            return;
        }
        this.userId = userId;
        user = userDao.loadUser(userId);
        tweetAndSenders = tweetDao.getTweetAndSenders();
    }

    LiveData<User> getUser() {
        return user;
    }

    LiveData<List<TweetAndSender>> getTweetAndSenders() {
        return tweetAndSenders;
    }

     public void addTweet(String text) {
        long createdAt = new Date().getTime();
        final String tweetId = String.format(Locale.US, "%s_%d", userId, createdAt);
        final Tweet tweet = new Tweet(tweetId, createdAt, text, userId);
        executorService.submit(() -> tweetDao.insertTweet(tweet));
    }


}
