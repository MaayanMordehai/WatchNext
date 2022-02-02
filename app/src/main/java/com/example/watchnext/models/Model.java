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
import com.example.watchnext.models.entities.relations.ReviewWithOwner;
import com.example.watchnext.models.entities.User;
import com.example.watchnext.models.firebase.AuthFirebase;
import com.example.watchnext.models.firebase.ModelFirebase;
import com.example.watchnext.models.firebase.reviews.interfaces.AddReviewListener;
import com.example.watchnext.models.firebase.reviews.interfaces.UpdateReviewListener;
import com.example.watchnext.models.firebase.reviews.interfaces.UploadReviewImageListener;
import com.example.watchnext.models.firebase.users.interfaces.AddUserListener;
import com.example.watchnext.models.firebase.users.interfaces.IsEmailExistOnFailureListener;
import com.example.watchnext.models.firebase.users.interfaces.IsEmailExistOnSuccessListener;
import com.example.watchnext.models.firebase.users.interfaces.LoginOnFailureListener;
import com.example.watchnext.models.firebase.users.interfaces.LoginOnSuccessListener;
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

    private final MutableLiveData<List<ReviewWithOwner>> reviewWithOwnerList = new MutableLiveData<>();
    private final MutableLiveData<LoadingStateEnum> reviewWithOwnerListLoadingState = new MutableLiveData<>();

    private final MutableLiveData<List<ReviewWithOwner>> reviewWithOwnerListByUserId = new MutableLiveData<>();
    private final MutableLiveData<LoadingStateEnum> reviewWithOwnerListByUserIdLoadingState = new MutableLiveData<>();

    private Model() {}

    public MutableLiveData<LoadingStateEnum> getReviewWithOwnerListLoadingState() {
        return reviewWithOwnerListLoadingState;
    }

    public MutableLiveData<LoadingStateEnum> getReviewWithOwnerListByUserIdLoadingState() {
        return reviewWithOwnerListByUserIdLoadingState;
    }

    private void refreshReviewList(RefreshListener lis) {
        Long lastUpdateDate = Review.getLocalLastUpdated();
        executor.execute(() -> {
            List<ReviewWithOwner> reviewList = WatchNextLocalDb.db.reviewDao().getReviewsWithOwners();
            reviewWithOwnerList.postValue(reviewList);
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
                lis.onComplete();
            });
        });
    }

    public void refreshUserList() {
        this.refreshUserList(() -> {});
    }

    private void refreshUserList(RefreshListener lis) {
        Long lastUpdateDate = Review.getLocalLastUpdated();
        modelfirebase.getAllUsers(lastUpdateDate, (users) -> {
            executor.execute(() -> {
                Long lastUpdated = 0L;
                for (User u: users) {
                    if (lastUpdated < u.getUpdateDate()) {
                        lastUpdated = u.getUpdateDate();
                    }
                    WatchNextLocalDb.db.userDao().insertAll(u);
                }
                User.setLocalLastUpdated(lastUpdated);
                lis.onComplete();
            });
        });
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

    public void refreshReviewWithOwnerListByUserId(String userId) {
        reviewWithOwnerListByUserIdLoadingState.setValue(LoadingStateEnum.loading);
        refreshUserList(() -> {
            refreshReviewList(() -> {
                executor.execute(() -> {
                    reviewWithOwnerListByUserId.postValue(WatchNextLocalDb.db.reviewDao().getReviewsWithOwnersByUserId(userId));
                    reviewWithOwnerListByUserIdLoadingState.postValue(LoadingStateEnum.loaded);
                });
            });
        });
    }

    public LiveData<List<ReviewWithOwner>> getReviewWithOwnerListByUserId(String userId) {
        if (reviewWithOwnerListByUserId.getValue() == null) {
            refreshReviewWithOwnerListByUserId(userId);
        }
        return reviewWithOwnerListByUserId;
    }

    public LiveData<User> getUserById(String id) {
        MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();
        Model.instance.refreshUserList();
        executor.execute(() -> {
            User user = WatchNextLocalDb.db.userDao().getById(id);
            userMutableLiveData.postValue(user);
        });
        return userMutableLiveData;
    }

    public void addReview(AddReviewListener listener, Review review) {
        modelfirebase.addReview(listener, review);
    }

    public String getCurrentUserId() {
        return authFirebase.getCurrentUserUid();
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
        authFirebase.register(user.getEmail(), password, uid -> {
            user.setId(uid);
            modelfirebase.addUser(userLis, user);
        });
    }

    public void logout(LogoutListener lis) {
        authFirebase.logout(lis);
    }

    public void login(String email, String password, LoginOnSuccessListener onSuccessListener, LoginOnFailureListener onFailureListener) {
        authFirebase.login(email, password, onSuccessListener, onFailureListener);
    }

    public void isEmailExists(String email, IsEmailExistOnSuccessListener onSuccessListener, IsEmailExistOnFailureListener onFailureListener){
        authFirebase.isEmailExist(email, onSuccessListener, onFailureListener);
    }

    public boolean isSignedIn() {
        return authFirebase.isSignedIn();
    }
}