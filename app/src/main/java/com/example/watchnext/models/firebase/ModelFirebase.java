package com.example.watchnext.models.firebase;

import android.graphics.Bitmap;

import com.example.watchnext.models.entities.Review;
import com.example.watchnext.models.firebase.reviews.ReviewModelFirebase;
import com.example.watchnext.models.firebase.reviews.interfaces.AddReviewListener;
import com.example.watchnext.models.firebase.reviews.interfaces.GetAllReviewsListener;
import com.example.watchnext.models.firebase.reviews.interfaces.GetReviewListener;
import com.example.watchnext.models.firebase.reviews.interfaces.UpdateReviewListener;
import com.example.watchnext.models.firebase.reviews.interfaces.UploadReviewImageListener;
import com.example.watchnext.models.entities.User;
import com.example.watchnext.models.firebase.users.UserModelFirebase;
import com.example.watchnext.models.firebase.users.interfaces.AddUserListener;
import com.example.watchnext.models.firebase.users.interfaces.GetAllUsersListener;
import com.example.watchnext.models.firebase.users.interfaces.GetUserListener;
import com.example.watchnext.models.firebase.users.interfaces.UpdateUserListener;
import com.example.watchnext.models.firebase.users.interfaces.UploadUserImageListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class ModelFirebase {

    private final ReviewModelFirebase reviewFirebase = new ReviewModelFirebase();
    private final UserModelFirebase userFirebase = new UserModelFirebase();

    public ModelFirebase() {
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.setFirestoreSettings(settings);
    }

    public void getAllReviews(Long since, GetAllReviewsListener listener) {
        reviewFirebase.getAllReviews(since, listener);
    }

    public void addReview(AddReviewListener lis, Review r) {
        reviewFirebase.addReview(lis, r);
    }

    public void updateReview(UpdateReviewListener lis, Review r) {
        reviewFirebase.updateReview(lis, r);
    }

    public void getReviewById(GetReviewListener lis, String id) {
        reviewFirebase.getReviewById(lis, id);
    }

    public void getAllUsers(Long since, GetAllUsersListener listener) {
        userFirebase.getAllUsers(since, listener);
    }

    public void addUser(AddUserListener lis, User u) {
        userFirebase.addUser(lis, u);
    }

    public void getUserByEmail(GetUserListener lis, String id) {
        userFirebase.getUserByEmail(lis, id);
    }

    public void uploadUserImage(Bitmap imageBmp, String name, UploadUserImageListener listener) {
        userFirebase.uploadUserImage(imageBmp, name, listener);
    }

    public void uploadReviewImage(Bitmap imageBmp, String name, UploadReviewImageListener listener) {
        reviewFirebase.uploadReviewImage(imageBmp, name, listener);
    }

    public void updateUser(UpdateUserListener lis, User u) {
        userFirebase.updateUser(lis, u);
    }
}
