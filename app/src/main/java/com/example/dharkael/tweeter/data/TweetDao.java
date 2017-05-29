package com.example.dharkael.tweeter.data;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.dharkael.tweeter.data.entities.Tweet;
import com.example.dharkael.tweeter.data.pojos.TweetAndSender;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface TweetDao {
    @Insert(onConflict = REPLACE)
    void insertTweet(Tweet tweet);

    @Insert(onConflict = REPLACE)
    void insertTweets(List<Tweet>  tweets);

    @Query("SELECT * FROM tweet WHERE user_id = :userId")
    LiveData<List<Tweet>> getTweets(String userId);

    @Query("SELECT tweet.* , user.name AS users_name, user.id AS users_id, "
            +" user.created_at AS  users_created_at "
            +"FROM tweet , user WHERE  user.id == user_id ORDER BY created_at DESC")
    LiveData<List<TweetAndSender>> getTweetAndSenders();

  @Query("DELETE FROM tweet WHERE 1 == 1")
  void delete();
}
