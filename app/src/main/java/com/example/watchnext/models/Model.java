package com.example.watchnext.models;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.watchnext.enums.LoadingStateEnum;
import com.example.watchnext.models.entities.Review;
import com.example.watchnext.models.entities.User;
import com.example.watchnext.models.firebase.AuthFirebase;
import com.example.watchnext.models.firebase.ModelFirebase;
import com.example.watchnext.models.firebase.reviews.interfaces.AddReviewListener;
import com.example.watchnext.models.firebase.reviews.interfaces.GetReviewListener;
import com.example.watchnext.models.firebase.reviews.interfaces.UpdateReviewListener;
import com.example.watchnext.models.firebase.reviews.interfaces.UploadReviewImageListener;
import com.example.watchnext.models.firebase.users.interfaces.AddUserListener;
import com.example.watchnext.models.firebase.users.interfaces.GetUserListener;
import com.example.watchnext.models.firebase.users.interfaces.UploadUserImageListener;
import com.example.watchnext.models.room.WatchNextLocalDb;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Model {

    public static final Model instance = new Model();
    public final Executor executor = Executors.newFixedThreadPool(1);
    public final Handler mainThread = HandlerCompat.createAsync(Looper.getMainLooper());

    private final ModelFirebase modelfirebase = new ModelFirebase();
    private final AuthFirebase authFirebase = new AuthFirebase();
    private final MutableLiveData<List<Review>> reviewsList = new MutableLiveData<>();
    private final MutableLiveData<List<User>> usersList = new MutableLiveData<>();
    private final MutableLiveData<LoadingStateEnum> reviewListLoadingState = new MutableLiveData<>();
    private final MutableLiveData<LoadingStateEnum> userListLoadingState = new MutableLiveData<>();

    private Model() {}

    public MutableLiveData<LoadingStateEnum> getReviewListLoadingState() {
        return reviewListLoadingState;
    }

    public MutableLiveData<LoadingStateEnum> getUserListLoadingState() {
        return userListLoadingState;
    }

    public void refreshReviewList() {
        reviewListLoadingState.setValue(LoadingStateEnum.loading);
        Long lastUpdateDate = Review.getLocalLastUpdated();

        executor.execute(() -> {
            List<Review> reviewList = WatchNextLocalDb.db.reviewDao().getAll();
            reviewsList.postValue(reviewList);
        });

        modelfirebase.getAllReviews(lastUpdateDate, (reviews) -> {
            executor.execute(() -> {
                Long lastUpdated = 0L;
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
                reviewListLoadingState.postValue(LoadingStateEnum.loaded);
            });
        });
    }

    public LiveData<List<Review>> getAllReviews() {
        if (reviewsList.getValue() == null){
            refreshReviewList();
        }
        return reviewsList;
    }

    public void refreshUserList() {
        userListLoadingState.setValue(LoadingStateEnum.loading);
        Long lastUpdateDate = Review.getLocalLastUpdated();

        executor.execute(() -> {
            List<User> userList = WatchNextLocalDb.db.userDao().getAll();
            usersList.postValue(userList);
        });

        modelfirebase.getAllUsers(lastUpdateDate, (users) -> {
            executor.execute(() -> {
                Long lastUpdated = 0L;
                for (User u: users) {
                    if (lastUpdated < u.getUpdateDate()) {
                        lastUpdated = u.getUpdateDate();
                    }
                    WatchNextLocalDb.db.userDao().insertAll(u);
                }
                Review.setLocalLastUpdated(lastUpdated);
                List<Review> rwList = WatchNextLocalDb.db.reviewDao().getAll();
                reviewsList.postValue(rwList);
                reviewListLoadingState.postValue(LoadingStateEnum.loaded);
            });
        });
    }

    public LiveData<List<User>> getAllUsers() {
        if (usersList.getValue() == null) {
            refreshUserList();
        }
        return usersList;
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

    public void uploadReviewImage(Bitmap imageBmp, String name, UploadReviewImageListener listener) {
        modelfirebase.uploadReviewImage(imageBmp, name, listener);
    }

    public void uploadUserImage(Bitmap imageBmp, String name, UploadUserImageListener listener) {
        modelfirebase.uploadUserImage(imageBmp, name, listener);
    }

    public boolean isSignedIn() {
        return authFirebase.isSignedIn();
    }

}