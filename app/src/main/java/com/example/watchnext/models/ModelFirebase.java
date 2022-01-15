package com.example.watchnext.models;

import com.example.watchnext.models.reviews.Review;
import com.example.watchnext.models.reviews.ReviewModelFirebase;
import com.example.watchnext.models.reviews.interfaces.AddReviewListener;
import com.example.watchnext.models.reviews.interfaces.GetAllReviewsListener;
import com.example.watchnext.models.reviews.interfaces.GetReviewListener;
import com.example.watchnext.models.reviews.interfaces.UpdateReviewListener;
import com.example.watchnext.models.users.User;
import com.example.watchnext.models.users.UserModelFirebase;
import com.example.watchnext.models.users.interfaces.AddUserListener;
import com.example.watchnext.models.users.interfaces.GetAllUsersListener;
import com.example.watchnext.models.users.interfaces.GetUserListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class ModelFirebase {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ReviewModelFirebase reviewFirebase = new ReviewModelFirebase();
    UserModelFirebase userFirebase = new UserModelFirebase();

    public ModelFirebase() {
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
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

    public void getUserById(GetUserListener lis, String id) {
        userFirebase.getUserById(lis, id);
    }
}
