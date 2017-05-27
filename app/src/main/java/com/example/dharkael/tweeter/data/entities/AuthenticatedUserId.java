package com.example.dharkael.tweeter.data.entities;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;


@Entity(tableName = "authenticated_user_id")
public class AuthenticatedUserId {
    static final int ID = 0;
    @PrimaryKey
    public final int id;

    @ColumnInfo(name = "user_id")
    public final String userId;

    @ColumnInfo(name = "updated_at")
    public final long updatedAt;

    public AuthenticatedUserId(int id, String userId, long updatedAt) {
        this.id = id;
        this.userId = userId;
        this.updatedAt = updatedAt;
    }
    @Ignore
    public AuthenticatedUserId(String userId, long updatedAt) {this(ID, userId, updatedAt); }
}
