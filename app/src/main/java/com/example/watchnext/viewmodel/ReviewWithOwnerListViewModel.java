package com.example.watchnext.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.watchnext.models.Model;
import com.example.watchnext.models.entities.User;
import com.example.watchnext.models.entities.relations.ReviewWithOwner;

import java.util.List;

public class ReviewWithOwnerListViewModel extends ViewModel {

    private final LiveData<User> user;
    private final LiveData<List<ReviewWithOwner>> reviewsWithOwner;

    public ReviewWithOwnerListViewModel(String userId) {
        reviewsWithOwner = Model.instance.getAllReviewsWithOwner();
        user = Model.instance.getUserById(userId);
    }

    public LiveData<List<ReviewWithOwner>> getData() {
        return reviewsWithOwner;
    }

    public LiveData<User> getUser() {
        return user;
    }
}