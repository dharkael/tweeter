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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AuthenticatedUserId userId1 = (AuthenticatedUserId) o;

        if (id != userId1.id) return false;
        if (updatedAt != userId1.updatedAt) return false;
        return userId != null ? userId.equals(userId1.userId) : userId1.userId == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (int) (updatedAt ^ (updatedAt >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "AuthenticatedUserId{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
