package com.example.watchnext.viewmodel.factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.watchnext.viewmodel.ReviewWithOwnerListViewModel;
import java.util.Objects;

public class ReviewWithOwnerListViewModelFactory implements ViewModelProvider.Factory {
    private final String userId;

    public ReviewWithOwnerListViewModelFactory(String userId) {
        this.userId = userId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return Objects.requireNonNull(modelClass.cast(new ReviewWithOwnerListViewModel(userId)));
    }
}