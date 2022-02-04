package com.example.watchnext.models.entities.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.watchnext.models.entities.Review;
import com.example.watchnext.models.entities.User;

public class ReviewWithOwner {
    @Embedded public Review review;
    @Relation(
            parentColumn = "ownerId",
            entityColumn = "id")
    public User user;
}
