package com.example.watchnext.models;

import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Model {
    public static final Model instance = new Model();
    private static final Executor executor = Executors.newFixedThreadPool(1);
    private static final Handler mainThread = HandlerCompat.createAsync(Looper.getMainLooper());


    private Model() {
    }


    // ROOM

    // Reviews
    interface GetAllReviewsListener{
        void onComplete(List<Review> list);
    }

    public void getAllReviews(GetAllReviewsListener listener) {
        executor.execute(() -> {
            List<Review> reviews = WatchNextLocalDb.db.reviewDao().getAll();
            mainThread.post(() -> {
                listener.onComplete(reviews);
            });
        });
    }

    interface GetReviewListener{
        void onComplete(Review review);
    }

    public void getReviewById(GetReviewListener listener, String id) {
        executor.execute(() -> {
            Review review = WatchNextLocalDb.db.reviewDao().getById(id);
            mainThread.post(() -> {
                listener.onComplete(review);
            });
        });
    }

    interface AddReviewListener {
        void onComplete();
    }
    public void addReview(Review review, AddReviewListener listener) {
        executor.execute(() -> {
            WatchNextLocalDb.db.reviewDao().upsert(review);
            mainThread.post(() -> {
                listener.onComplete();
            });
        });
    }

    interface DeleteReviewListener {
        void onComplete();
    }

    public void deleteReview(Review review, DeleteReviewListener listener) {
        executor.execute(() -> {
            WatchNextLocalDb.db.reviewDao().delete(review);
            mainThread.post(() -> {
                listener.onComplete();
            });
        });
    }
    // Users
    interface GetAllUsersListener{
        void onComplete(List<User> list);
    }

    public void getAllUsers(GetAllUsersListener listener) {
        executor.execute(() -> {
            List<User> users = WatchNextLocalDb.db.userDao().getAll();
            mainThread.post(() -> {
                listener.onComplete(users);
            });
        });
    }

    interface GetUserListener{
        void onComplete(User user);
    }

    public void getUserById(GetUserListener listener, String id) {
        executor.execute(() -> {
            User user = WatchNextLocalDb.db.userDao().getById(id);
            mainThread.post(() -> {
                listener.onComplete(user);
            });;
        });
    }

    interface AddUserListener {
        void onComplete();
    }
    public void addUser(User user, AddUserListener listener) {
        executor.execute(() -> {
            WatchNextLocalDb.db.userDao().upsert(user);
            mainThread.post(() -> {
                listener.onComplete();
            });
        });
    }

    interface DeleteUserListener {
        void onComplete();
    }
    public void deleteUser(User user, DeleteUserListener listener) {
        executor.execute(() -> {
            WatchNextLocalDb.db.userDao().delete(user);
            mainThread.post(() -> {
                listener.onComplete();
            });
        });
    }


}