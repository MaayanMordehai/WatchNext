package com.example.watchnext.models.reviews.interfaces;

import com.example.watchnext.models.reviews.Review;

import java.util.List;

public interface GetAllReviewsListener {
    void onComplete(List<Review> list);
}
