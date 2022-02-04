package com.example.watchnext.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.watchnext.models.Model;
import com.example.watchnext.models.entities.relations.ReviewWithOwner;

import java.util.List;

public class ReviewWithOwnerListViewModel extends ViewModel {

    private final LiveData<List<ReviewWithOwner>> reviewsWithOwner;

    public ReviewWithOwnerListViewModel() {
        reviewsWithOwner = Model.instance.getAllReviewsWithOwner();
    }

    public LiveData<List<ReviewWithOwner>> getData() {
        return reviewsWithOwner;
    }
}