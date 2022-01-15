package com.example.watchnext.models.users;

import com.example.watchnext.models.users.interfaces.*;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class UserModelFirebase {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public UserModelFirebase() { }

    public void getAllUsers(GetAllUsersListener listener) {
        db.collection(User.COLLECTION_NAME)
                .get()
                .addOnCompleteListener(task -> {
                    List<User> list = new LinkedList<>();
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            User u = User.create(doc.getData());
                            if (u != null) {
                                list.add(u);
                            }
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
}
