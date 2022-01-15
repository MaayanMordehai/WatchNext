package com.example.watchnext.models.firebase.reviews.interfaces;

import com.example.watchnext.models.entities.Review;

public interface GetReviewListener {
    void onComplete(Review review);
}
