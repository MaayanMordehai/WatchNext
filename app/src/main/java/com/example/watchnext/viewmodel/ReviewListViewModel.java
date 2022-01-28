package com.example.watchnext.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.watchnext.models.Model;
import com.example.watchnext.models.entities.Review;

import java.util.List;

public class ReviewListViewModel extends ViewModel {

    private LiveData<List<Review>> reviews;

    public ReviewListViewModel() {
        reviews = Model.instance.getAllReviews();
    }

    public  LiveData<List<Review>> getData() {
        return reviews;
    }
}