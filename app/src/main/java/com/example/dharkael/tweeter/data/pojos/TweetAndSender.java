package com.example.dharkael.tweeter.data.pojos;

import android.arch.persistence.room.Embedded;

import com.example.dharkael.tweeter.data.entities.Tweet;
import com.example.dharkael.tweeter.data.entities.User;


public class TweetAndSender {
    @Embedded
    public final Tweet tweet;
    @Embedded(prefix = "users_")
    public final User sender;

    public TweetAndSender(Tweet tweet, User sender) {
        this.tweet = tweet;
        this.sender = sender;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TweetAndSender that = (TweetAndSender) o;

        if (tweet != null ? !tweet.equals(that.tweet) : that.tweet != null) return false;
        return sender != null ? sender.equals(that.sender) : that.sender == null;
    }

    @Override
    public int hashCode() {
        int result = tweet != null ? tweet.hashCode() : 0;
        result = 31 * result + (sender != null ? sender.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TweetAndSender{" +
                "tweet=" + tweet +
                ", sender=" + sender +
                '}';
    }
}
