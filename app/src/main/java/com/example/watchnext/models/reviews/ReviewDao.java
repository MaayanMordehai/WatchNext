package com.example.watchnext.models.reviews;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.watchnext.models.reviews.Review;

import java.util.List;

@Dao
public interface ReviewDao {

    @Query("SELECT * FROM Review WHERE id=:reviewId")
    Review getById(String reviewId);

    @Query("SELECT * FROM Review")
    List<Review> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void upsertAll(Review... reviews);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void upsert(Review review);

    @Delete
    void delete(Review review);

}
