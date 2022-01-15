package com.example.watchnext.models.reviews;

import com.example.watchnext.models.reviews.interfaces.*;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ReviewModelFirebase {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ReviewModelFirebase() {}

    public void getAllReviews(long since, GetAllReviewsListener listener) {
        db.collection(Review.COLLECTION_NAME)
                .whereGreaterThanOrEqualTo(Review.LAST_UPDATED, new Timestamp(since, 0))
                .get()
                .addOnCompleteListener(task -> {
                    List<Review> list = new LinkedList<>();
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            list.add(Review.create(doc.getData()));
                        }
                    }
                    listener.onComplete(list);
                });
    }

    public void addReview(AddReviewListener lis, Review r) {
        Map<String, Object> jsonReview = r.toMap();
        db.collection(Review.COLLECTION_NAME)
                .document(r.getId())
                .set(jsonReview)
                .addOnSuccessListener(unused -> lis.onComplete())
                .addOnFailureListener(e -> lis.onComplete());
    }

    public void updateReview(UpdateReviewListener lis, Review r) {
        Map<String, Object> jsonReview = r.toMap();
        db.collection(Review.COLLECTION_NAME)
                .document(r.getId())
                .update(jsonReview)
                .addOnSuccessListener(unused -> lis.onComplete())
                .addOnFailureListener(e -> lis.onComplete());
    }

    public void getReviewById(GetReviewListener lis, String id) {
        db.collection(Review.COLLECTION_NAME)
                .document(id)
                .get()
                .addOnCompleteListener( task -> {
                    Review r = null;
                    if (task.isSuccessful() & task.getResult() != null) {
                        r = Review.create(task.getResult().getData());
                    }
                    lis.onComplete(r);
                });
    }
}
