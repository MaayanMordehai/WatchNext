package com.example.watchnext.models.firebase.users;

import android.graphics.Bitmap;

import com.example.watchnext.models.entities.User;
import com.example.watchnext.models.firebase.users.interfaces.AddUserListener;
import com.example.watchnext.models.firebase.users.interfaces.GetAllUsersListener;
import com.example.watchnext.models.firebase.users.interfaces.GetUserListener;
import com.example.watchnext.models.firebase.users.interfaces.UpdateUserListener;
import com.example.watchnext.models.firebase.users.interfaces.UploadUserImageListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserModelFirebase {

    public static final String IMAGE_FOLDER = "users";
    public static final String COLLECTION_NAME = "users";

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseStorage storage = FirebaseStorage.getInstance();

    public UserModelFirebase() { }

    public void getAllUsers(long since, GetAllUsersListener listener) {
        db.collection(COLLECTION_NAME)
                .whereGreaterThanOrEqualTo(User.UPDATE_FIELD, new Timestamp(since, 0))
                .get()
                .addOnCompleteListener(task -> {
                    List<User> list = new ArrayList<>();
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            list.add(User.create(doc.getData(), doc.getId()));
                        }
                    }
                    listener.onComplete(list);
                });
    }

    public void addUser(AddUserListener lis, User u) {
        Map<String, Object> jsonUser = u.toMap();
        db.collection(COLLECTION_NAME)
                .document()
                .set(jsonUser)
                .addOnSuccessListener(unused -> lis.onComplete())
                .addOnFailureListener(e -> lis.onComplete());

    }

    public void updateUser(UpdateUserListener lis, User u) {
        Map<String, Object> jsonUser = u.toMap();
        db.collection(COLLECTION_NAME)
                .document(u.getId())
                .update(jsonUser)
                .addOnSuccessListener(unused -> lis.onComplete())
                .addOnFailureListener(e -> lis.onComplete());
    }

    public void getUserById(GetUserListener lis, String id) {
        db.collection(COLLECTION_NAME)
                .document(id)
                .get()
                .addOnCompleteListener( task -> {
                    User u = null;
                    if ((task.isSuccessful()) &&
                        (task.getResult() != null) &&
                        (task.getResult().getData() != null)) {
                        u = User.create(task.getResult().getData(), task.getResult().getId());
                    }
                    lis.onComplete(u);
                });
    }

    public void getUserByEmail(GetUserListener lis, String email) {
        db.collection(COLLECTION_NAME)
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener( task -> {
                    User u = null;
                    if ((task.isSuccessful()) &&
                            (task.getResult() != null) &&
                            (task.getResult().getDocuments().get(0).getData() != null)) {
                        u = User.create(task.getResult().getDocuments().get(0).getData(), task.getResult().getDocuments().get(0).getId());
                    }
                    lis.onComplete(u);
                });
    }

    public void uploadUserImage(Bitmap imageBmp, String name, UploadUserImageListener listener){
        final StorageReference imagesRef = storage.getReference().child(IMAGE_FOLDER).child(name);

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
