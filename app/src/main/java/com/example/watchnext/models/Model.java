package com.example.watchnext.models;

import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.watchnext.models.reviews.Review;
import com.example.watchnext.models.reviews.interfaces.AddReviewListener;
import com.example.watchnext.models.reviews.interfaces.UpdateReviewListener;
import com.example.watchnext.models.reviews.interfaces.GetReviewListener;
import com.example.watchnext.models.users.User;
import com.example.watchnext.models.users.interfaces.AddUserListener;
import com.example.watchnext.models.users.interfaces.GetUserListener;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Model {
    public static final Model instance = new Model();
    private static final Executor executor = Executors.newFixedThreadPool(1);
    private static final Handler mainThread = HandlerCompat.createAsync(Looper.getMainLooper());
    private static final ModelFirebase modelfirebase = new ModelFirebase();
    private static final MutableLiveData<List<Review>> reviewsList = new MutableLiveData<List<Review>>();
    private static final MutableLiveData<List<User>> usersList = new MutableLiveData<List<User>>();
    private static final MutableLiveData<LoadingState> reviewListLoadingState = new MutableLiveData<LoadingState>();
    private static final MutableLiveData<LoadingState> userListLoadingState = new MutableLiveData<LoadingState>();
    public enum LoadingState {
        loading,
        loaded
    }

    private Model() {
    }

    public static MutableLiveData<LoadingState> getReviewListLoadingState() {
        return reviewListLoadingState;
    }

    public static MutableLiveData<LoadingState> getUserListLoadingState() {
        return userListLoadingState;
    }

    public void refreshReviewList() {
        reviewListLoadingState.setValue(LoadingState.loading);
        Long lastUpdateDate = Review.getLocalLastUpdated();

        modelfirebase.getAllReviews(lastUpdateDate, (reviews) -> {
            executor.execute(() -> {
                Long lastUpdated = new Long(0);
                for (Review r: reviews) {
                    if (lastUpdated < r.getUpdateDate()) {
                        lastUpdated = r.getUpdateDate();
                    }
                    WatchNextLocalDb.db.reviewDao().insertAll(r);
                    if (r.isDeleted()){
                        WatchNextLocalDb.db.reviewDao().delete(r);
                    }
                }
                Review.setLocalLastUpdated(lastUpdated);
                List<Review> rwList = WatchNextLocalDb.db.reviewDao().getAll();
                reviewsList.postValue(rwList);
                reviewListLoadingState.postValue(LoadingState.loaded);
            });
        });
    }

    public LiveData<List<Review>> getAllReviews() {
        if (this.reviewsList.getValue() == null){
            refreshReviewList();
        }
        return this.reviewsList;
    }

    public void refreshUserList() {
        userListLoadingState.setValue(LoadingState.loading);
        // Get last local update date
        Long lastUpdateDate = Review.getLocalLastUpdated();

        modelfirebase.getAllUsers(lastUpdateDate, (users) -> {
            executor.execute(() -> {
                Long lastUpdated = new Long(0);
                for (User u: users) {
                    if (lastUpdated < u.getUpdateDate()) {
                        lastUpdated = u.getUpdateDate();
                    }
                    WatchNextLocalDb.db.userDao().insertAll(u);
                }
                Review.setLocalLastUpdated(lastUpdated);
                List<Review> rwList = WatchNextLocalDb.db.reviewDao().getAll();
                reviewsList.postValue(rwList);
                reviewListLoadingState.postValue(LoadingState.loaded);
            });
        });
    }

    public LiveData<List<User>> getAllUsers() {
        if (this.usersList.getValue() == null) {
            refreshUserList();
        }
        return this.usersList;
    }

    public void getReviewById(GetReviewListener listener, String id) {
        modelfirebase.getReviewById(listener, id);
    }

    public void addReview(AddReviewListener listener, Review review) {
        modelfirebase.addReview(listener, review);
    }

    public void addUser(AddUserListener lis, User user) {
        modelfirebase.addUser(lis, user);
    }

    public void getUserById(GetUserListener listener, String id) {
        modelfirebase.getUserById(listener, id);
    }

    public void updateReview(UpdateReviewListener lis, Review r) {
        modelfirebase.updateReview(lis, r);
    }

    public void deleteReview(UpdateReviewListener lis, Review r) {
        r.setDeleted(true);
        modelfirebase.updateReview(lis, r);
    }

}