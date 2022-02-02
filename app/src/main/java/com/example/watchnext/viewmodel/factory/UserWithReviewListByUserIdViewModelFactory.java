package com.example.watchnext.viewmodel.factory;


import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.watchnext.viewmodel.UserWithReviewListViewModel;

import java.util.Objects;

// TODO: Delete
/*public class ReviewListByUserIdViewModelFactory implements ViewModelProvider.Factory {
    private final String userId;

    public ReviewListByUserIdViewModelFactory(String userId) {
        this.userId = userId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return Objects.requireNonNull(modelClass.cast(new UserWithReviewListViewModel(userId)));
    }
}*/

/*MyViewModel myViewModel = ViewModelProvider(this, new MyViewModelFactory(this.getApplication(), "my awesome param")).get(MyViewModel.class);*/

