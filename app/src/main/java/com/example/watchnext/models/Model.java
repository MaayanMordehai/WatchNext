package com.example.watchnext.models;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.watchnext.common.interfaces.RefreshListener;
import com.example.watchnext.enums.LoadingStateEnum;
import com.example.watchnext.models.entities.Review;
import com.example.watchnext.models.entities.ReviewWithOwner;
import com.example.watchnext.models.entities.User;
import com.example.watchnext.models.firebase.AuthFirebase;
import com.example.watchnext.models.firebase.ModelFirebase;
import com.example.watchnext.models.firebase.reviews.interfaces.AddReviewListener;
import com.example.watchnext.models.firebase.reviews.interfaces.UpdateReviewListener;
import com.example.watchnext.models.firebase.reviews.interfaces.UploadReviewImageListener;
import com.example.watchnext.models.firebase.users.interfaces.AddUserListener;
import com.example.watchnext.models.firebase.users.interfaces.IsEmailExistListener;
import com.example.watchnext.models.firebase.users.interfaces.LoginListener;
import com.example.watchnext.models.firebase.users.interfaces.LogoutListener;
import com.example.watchnext.models.firebase.users.interfaces.UpdateUserListener;
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
    private final MutableLiveData<LoadingStateEnum> reviewWithOwnerListLoadingState = new MutableLiveData<>();
    private final MutableLiveData<List<ReviewWithOwner>> reviewWithOwnerList = new MutableLiveData<>();

    private Model() {}

    public MutableLiveData<LoadingStateEnum> getReviewWithOwnerListLoadingState() {
        return reviewWithOwnerListLoadingState;
    }

    public MutableLiveData<LoadingStateEnum> getReviewListLoadingState() {
        return reviewListLoadingState;
    }

    public MutableLiveData<LoadingStateEnum> getUserListLoadingState() {
        return userListLoadingState;
    }

    public void refreshReviewList() {
        refreshReviewList(() -> {});
    }

    private void refreshReviewList(RefreshListener lis) {
        reviewListLoadingState.postValue(LoadingStateEnum.loading);
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
                lis.onComplete();
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
        this.refreshUserList(() -> {});
    }

    private void refreshUserList(RefreshListener lis) {
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
                lis.onComplete();
            });
        });
    }

    public LiveData<List<User>> getAllUsers() {
        if (usersList.getValue() == null) {
            refreshUserList();
        }
        return usersList;
    }

    public void refreshReviewWithOwnerList() {
        reviewWithOwnerListLoadingState.setValue(LoadingStateEnum.loading);
        refreshUserList(() -> {
            refreshReviewList(() -> {
                executor.execute(() -> {
                    reviewWithOwnerList.postValue(WatchNextLocalDb.db.reviewDao().getReviewsWithOwners());
                    reviewWithOwnerListLoadingState.postValue(LoadingStateEnum.loaded);
                });
            });
        });
    }

    public LiveData<List<ReviewWithOwner>> getAllReviewsWithOwner() {
        if (reviewWithOwnerList.getValue() == null) {
            refreshReviewWithOwnerList();
        }
        return reviewWithOwnerList;
    }

    public LiveData<Review> getReviewById(String id) {
        MutableLiveData<Review> review = new MutableLiveData<>();
        Model.instance.refreshReviewList();
        executor.execute(() -> {
            Review r = WatchNextLocalDb.db.reviewDao().getById(id);
            review.postValue(r);
        });
        return review;
    }

    public void addReview(AddReviewListener listener, Review review) {
        modelfirebase.addReview(listener, review);
    }

    public LiveData<User> getUserById(String id) {
        MutableLiveData<User> user = new MutableLiveData<>();
        Model.instance.refreshUserList();
        executor.execute(() -> {
            User u = WatchNextLocalDb.db.userDao().getById(id);
            user.postValue(u);
        });
        return user;
    }

    public void updateUser(UpdateUserListener lis, User u) {
        modelfirebase.updateUser(lis, u);
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

    public void register(AddUserListener userLis, User user, String password) {
        authFirebase.register(user.getEmail(), password);
        modelfirebase.addUser(userLis, user);
    }

    public void logout(LogoutListener lis) {
        authFirebase.logout(lis);
    }

    public void login(LoginListener lis, String email, String password) {
        authFirebase.login(lis, email, password);
    }

    public void isEmailExists(String email, IsEmailExistListener lis){
        authFirebase.isEmailExist(email, lis);
    }

    public boolean isSignedIn() {
        return authFirebase.isSignedIn();
    }
}