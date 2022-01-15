package com.example.watchnext.models.users;

import android.graphics.Bitmap;

import com.example.watchnext.models.users.interfaces.AddUserListener;
import com.example.watchnext.models.users.interfaces.GetAllUsersListener;
import com.example.watchnext.models.users.interfaces.GetUserListener;
import com.example.watchnext.models.users.interfaces.UploadUserImageListener;
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

public class UserModelFirebase {
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static FirebaseStorage storage = FirebaseStorage.getInstance();

    public UserModelFirebase() { }

    public void getAllUsers(long since, GetAllUsersListener listener) {
        db.collection(User.COLLECTION_NAME)
                .whereGreaterThanOrEqualTo(User.LAST_UPDATED, new Timestamp(since, 0))
                .get()
                .addOnCompleteListener(task -> {
                    List<User> list = new LinkedList<>();
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            list.add(User.create(doc.getData()));
                        }
                    }
                    listener.onComplete(list);
                });
    }

    public void addUser(AddUserListener lis, User u) {
        Map<String, Object> jsonReview = u.toMap();
        db.collection(User.COLLECTION_NAME)
                .document(u.getId())
                .set(jsonReview)
                .addOnSuccessListener(unused -> lis.onComplete())
                .addOnFailureListener(e -> lis.onComplete());
    }

    public void getUserById(GetUserListener lis, String id) {
        db.collection(User.COLLECTION_NAME)
                .document(id)
                .get()
                .addOnCompleteListener( task -> {
                    User u = null;
                    if (task.isSuccessful() & task.getResult() != null) {
                        u = User.create(task.getResult().getData());
                    }
                    lis.onComplete(u);
                });
    }

    public static void uploadUserImage(Bitmap imageBmp, String name, UploadUserImageListener listener){
        final StorageReference imagesRef = storage.getReference().child(User.IMAGE_FOLDER).child(name);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnFailureListener(exception -> listener.onComplete(null))
                .addOnSuccessListener(taskSnapshot -> imagesRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    listener.onComplete(uri.toString());
                }));
    };
}
