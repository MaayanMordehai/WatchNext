package com.example.watchnext.viewmodel.factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.watchnext.viewmodel.UserWithReviewListViewModel;

import java.util.Objects;

public class UserWithReviewListByUserIdViewModelFactory implements ViewModelProvider.Factory {
    private final String userId;

    public UserWithReviewListByUserIdViewModelFactory(String userId) {
        this.userId = userId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return Objects.requireNonNull(modelClass.cast(new UserWithReviewListViewModel(userId)));
    }
}
