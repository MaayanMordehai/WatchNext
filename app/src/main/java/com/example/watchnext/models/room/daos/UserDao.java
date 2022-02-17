package com.example.watchnext.models.room.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.watchnext.models.entities.User;

@Dao
public interface UserDao {

    @Query("SELECT * FROM User WHERE id = :userId")
    User getById(String userId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(User... users);

}
