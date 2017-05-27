package com.example.dharkael.tweeter.data.entities;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(indices = {@Index("user_id")},
        foreignKeys = {
                @ForeignKey(entity = User.class,
                        parentColumns = {"id"},
                        childColumns = {"user_id"})})
public class Tweet {

    @PrimaryKey
    public final String id;

    @ColumnInfo(name = "created_at")
    public final long createdAt;

    public final String text;

    @ColumnInfo(name = "user_id")
    public final String userId;

    public Tweet(@NonNull String id, long createdAt, @NonNull String text, @NonNull String userId) {
        this.id = id;
        this.createdAt = createdAt;
        this.text = text;
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tweet tweet = (Tweet) o;

        if (createdAt != tweet.createdAt) return false;
        if (!id.equals(tweet.id)) return false;
        if (!text.equals(tweet.text)) return false;
        return userId.equals(tweet.userId);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (int) (createdAt ^ (createdAt >>> 32));
        result = 31 * result + text.hashCode();
        result = 31 * result + userId.hashCode();
        return result;
    }
}
