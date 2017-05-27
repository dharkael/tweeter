package com.example.dharkael.tweeter.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.dharkael.tweeter.data.entities.AuthenticatedUserId;
import com.example.dharkael.tweeter.data.entities.Tweet;
import com.example.dharkael.tweeter.data.entities.User;


@Database(entities = {User.class, Tweet.class, AuthenticatedUserId.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public static final String FAUX_DB_NAME = "faux-tweet-db";

    public abstract UserDao userDao();

    public abstract TweetDao tweetDao();

}
