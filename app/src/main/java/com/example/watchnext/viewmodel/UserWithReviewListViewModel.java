package com.example.watchnext.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.watchnext.models.Model;
import com.example.watchnext.models.entities.User;
import com.example.watchnext.models.entities.relations.ReviewWithOwner;

import java.util.List;

public class UserWithReviewListViewModel extends ViewModel {

    private final LiveData<User> user;
    private final LiveData<List<ReviewWithOwner>> reviewListByUserId;

    public UserWithReviewListViewModel(String userId) {
        user = Model.instance.getUserById(userId);
        reviewListByUserId = Model.instance.getReviewWithOwnerListByUserId(userId);
    }

    public LiveData<List<ReviewWithOwner>> getReviewList() {
        return reviewListByUserId;
    }

    public LiveData<User> getUser() {
        return user;
    }
}