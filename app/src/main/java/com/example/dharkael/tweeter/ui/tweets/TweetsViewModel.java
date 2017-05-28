package com.example.dharkael.tweeter.ui.tweets;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.dharkael.tweeter.data.TweetDao;
import com.example.dharkael.tweeter.data.UserDao;
import com.example.dharkael.tweeter.data.entities.User;
import com.example.dharkael.tweeter.data.pojos.TweetAndSender;

import java.util.List;


public class TweetsViewModel extends ViewModel {
    private TweetDao tweetDao;
    private UserDao userDao;
    private String userId;
    private LiveData<List<TweetAndSender>> tweetAndSenders;
    private LiveData<User> user;
    public TweetsViewModel(TweetDao tweetDao, UserDao userDao) {
        this.tweetDao = tweetDao;
        this.userDao = userDao;
    }

    public void setUserId(String userId) {
        if (this.userId != null && this.userId.equals(userId)) {
            return;
        }
        this.userId = userId;
        user = userDao.loadUser(userId);
        tweetAndSenders = tweetDao.getTweetAndSenders(userId);
    }

    LiveData<User> getUser() {
        return user;
    }

    LiveData<List<TweetAndSender>> getTweetAndSenders() {
        return tweetAndSenders;
    }
}
