package com.example.dharkael.tweeter.data;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;


import com.example.dharkael.tweeter.data.entities.Tweet;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface TweetDao {
    @Insert(onConflict = REPLACE)
    void insertTweet(Tweet tweet);

    @Query("SELECT * FROM tweet WHERE user_id = :userId")
    LiveData<List<Tweet>> getTweets(String userId);
}
