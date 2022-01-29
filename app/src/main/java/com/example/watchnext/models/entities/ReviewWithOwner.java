package com.example.watchnext.models.entities;

import androidx.room.Embedded;
import androidx.room.Relation;

public class ReviewWithOwner {
    @Embedded public Review review;
    @Relation(
            parentColumn = "ownerId",
            entityColumn = "id")
    public User user;
}
