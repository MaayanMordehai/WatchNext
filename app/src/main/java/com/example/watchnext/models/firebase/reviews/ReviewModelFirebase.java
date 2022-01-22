package com.example.watchnext.models.firebase.reviews;

import android.graphics.Bitmap;

import com.example.watchnext.models.entities.Review;
import com.example.watchnext.models.firebase.reviews.interfaces.AddReviewListener;
import com.example.watchnext.models.firebase.reviews.interfaces.GetAllReviewsListener;
import com.example.watchnext.models.firebase.reviews.interfaces.GetReviewListener;
import com.example.watchnext.models.firebase.reviews.interfaces.UpdateReviewListener;
import com.example.watchnext.models.firebase.reviews.interfaces.UploadReviewImageListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ReviewModelFirebase {

    public static final String IMAGE_FOLDER = "reviews";
    public static final String COLLECTION_NAME = "reviews";

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseStorage storage = FirebaseStorage.getInstance();

    public ReviewModelFirebase() {
    }

    public void getAllReviews(long since, GetAllReviewsListener listener) {
        db.collection(COLLECTION_NAME)
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
        db.collection(COLLECTION_NAME)
                .document()
                .set(jsonReview)
                .addOnSuccessListener(unused -> lis.onComplete())
                .addOnFailureListener(e -> lis.onComplete());
    }

    public void updateReview(UpdateReviewListener lis, Review r) {
        Map<String, Object> jsonReview = r.toMap();
        db.collection(COLLECTION_NAME)
                .document(r.getId())
                .update(jsonReview)
                .addOnSuccessListener(unused -> lis.onComplete())
                .addOnFailureListener(e -> lis.onComplete());
    }

    public void getReviewById(GetReviewListener lis, String id) {
        db.collection(COLLECTION_NAME)
                .document(id)
                .get()
                .addOnCompleteListener(task -> {
                    Review r = null;
                    if ((task.isSuccessful()) &&
                        (task.getResult() != null) &&
                        (task.getResult().getData() != null)) {
                        r = Review.create(task.getResult().getData());
                    }
                    lis.onComplete(r);
                });
    }

    public void uploadReviewImage(Bitmap imageBmp, String name, UploadReviewImageListener listener) {
        final StorageReference imagesRef = storage.getReference().child(IMAGE_FOLDER).child(name);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnFailureListener(exception -> listener.onComplete(null))
                .addOnSuccessListener(taskSnapshot -> imagesRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    listener.onComplete(uri.toString());
                }));
    }

}
