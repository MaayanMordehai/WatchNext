package com.example.watchnext.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.watchnext.models.Model;
import com.example.watchnext.models.entities.Review;
import com.example.watchnext.models.entities.User;

import java.util.List;

public class UserWithReviewListViewModel extends ViewModel {

    private final LiveData<User> user;
    private final LiveData<List<Review>> reviewListByUserId;

    public UserWithReviewListViewModel() {
        String userId = Model.instance.getCurrentUserId();
        user = Model.instance.getUserById(userId);
        reviewListByUserId = Model.instance.getReviewListByUserId(userId);
    }

    public LiveData<List<Review>> getReviewList() {
        return reviewListByUserId;
    }

    public LiveData<User> getUser() {
        return user;
    }
}