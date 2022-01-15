package com.example.watchnext.models;

import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;

import com.example.watchnext.models.reviews.Review;
import com.example.watchnext.models.reviews.interfaces.AddReviewListener;
import com.example.watchnext.models.reviews.interfaces.GetAllReviewsListener;
import com.example.watchnext.models.reviews.interfaces.GetReviewListener;
import com.example.watchnext.models.users.User;
import com.example.watchnext.models.users.interfaces.AddUserListener;
import com.example.watchnext.models.users.interfaces.GetAllUsersListener;
import com.example.watchnext.models.users.interfaces.GetUserListener;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Model {
    public static final Model instance = new Model();
    private static final Executor executor = Executors.newFixedThreadPool(1);
    private static final Handler mainThread = HandlerCompat.createAsync(Looper.getMainLooper());
    private static final ModelFirebase modelfirebase = new ModelFirebase();

    private Model() {
    }



    public void getAllReviews(GetAllReviewsListener listener) {
        modelfirebase.getAllReviews(listener);
        /*executor.execute(() -> {
            List<Review> reviews = WatchNextLocalDb.db.reviewDao().getAll();
            mainThread.post(() -> {
                listener.onComplete(reviews);
            });
        });*/
    }


    public void getReviewById(GetReviewListener listener, String id) {
        modelfirebase.getReviewById(listener, id);
        /*executor.execute(() -> {
            Review review = WatchNextLocalDb.db.reviewDao().getById(id);
            mainThread.post(() -> {
                listener.onComplete(review);
            });
        });*/
    }

    public void addReview(AddReviewListener listener, Review review) {
        modelfirebase.addReview(listener, review);
        /*executor.execute(() -> {
            WatchNextLocalDb.db.reviewDao().upsert(review);
            mainThread.post(() -> {
                listener.onComplete();
            });
        });*/
    }
    public void addUser(AddUserListener lis, User user) {
        modelfirebase.addUser(lis, user);
    }

    public void getUserById(GetUserListener listener, String id) {
        modelfirebase.getUserById(listener, id);
    }

    public void getAllUsers(GetAllUsersListener lis) {
        modelfirebase.getAllUsers(lis);
    }

 /*   interface DeleteReviewListener {
        void onComplete();
    }

    public void deleteReview(DeleteReviewListener listener, Review review) {
        executor.execute(() -> {
            WatchNextLocalDb.db.reviewDao().delete(review);
            mainThread.post(() -> {
                listener.onComplete();
            });
        });
    }*/

    /*interface GetUserListener{
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

    public void addUser(AddUserListener listener, User user) {
        modelfirebase.addUser(listener, user);
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
    }*/


}