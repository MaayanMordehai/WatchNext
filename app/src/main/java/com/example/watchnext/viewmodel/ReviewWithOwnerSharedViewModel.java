package com.example.watchnext.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.watchnext.models.entities.ReviewWithOwner;

public class ReviewWithOwnerSharedViewModel extends ViewModel {
    private final MutableLiveData<ReviewWithOwner> selected = new MutableLiveData<>();

    public void select(ReviewWithOwner reviewWithOwner) {
        selected.setValue(reviewWithOwner);
    }

    public LiveData<ReviewWithOwner> getSelected() {
        return selected;
    }
}
