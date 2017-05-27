package com.example.dharkael.tweeter.data;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.dharkael.tweeter.data.entities.AuthenticatedUserId;
import com.example.dharkael.tweeter.data.entities.User;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface UserDao {

    @Insert(onConflict = REPLACE)
    void insertUser(User user);

    @Update
    void updateUser(User user);

    @Delete
    void deleteUser(User user);

    @Query("SELECT * FROM user")
    LiveData<List<User>> loadUsers();

    @Query("SELECT * FROM user WHERE id = :id LIMIT 1")
    LiveData<User> loadUser(String id);

    @Insert(onConflict = REPLACE)
    void upsertAuthenticatedUserId(AuthenticatedUserId authenticatedUserId);

    @Delete
    void deleteAuthenticatedUserId(AuthenticatedUserId authenticatedUserId);

    @Query("SELECT * FROM authenticated_user_id WHERE id = 0 LIMIT 1")
    LiveData<AuthenticatedUserId> getAuthenticatedUserId();


    @Query("SELECT * FROM authenticated_user_id WHERE id = :id LIMIT 1")
    LiveData<AuthenticatedUserId> getAuthenticatedUserId(int id);

}
