package com.example.watchnext.models.room.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.watchnext.models.entities.Review;
import com.example.watchnext.models.entities.relations.ReviewWithOwner;

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

    @Transaction
    @Query("SELECT * FROM Review")
    List<ReviewWithOwner> getReviewsWithOwners();

    @Query("SELECT * FROM Review WHERE ownerId=:userId")
    List<Review> getReviewListByUserId(String userId);

}
