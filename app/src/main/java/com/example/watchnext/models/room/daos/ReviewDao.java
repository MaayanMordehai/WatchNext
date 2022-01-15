package com.example.watchnext.models.room.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.watchnext.models.entities.Review;

import java.util.List;

@Dao
public interface ReviewDao {

    @Query("SELECT * FROM Review WHERE id=:reviewId")
    Review getById(String reviewId);

    @Query("SELECT * FROM Review")
    List<Review> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Review... reviews);

    @Delete
    void delete(Review review);

}
