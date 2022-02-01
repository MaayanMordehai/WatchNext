package com.example.watchnext.models.firebase;

import com.example.watchnext.models.firebase.users.interfaces.IsEmailExistOnFailureListener;
import com.example.watchnext.models.firebase.users.interfaces.IsEmailExistOnSuccessListener;
import com.example.watchnext.models.firebase.users.interfaces.LoginOnFailureListener;
import com.example.watchnext.models.firebase.users.interfaces.LoginOnSuccessListener;
import com.example.watchnext.models.firebase.users.interfaces.LogoutListener;
import com.example.watchnext.models.firebase.users.interfaces.RegisterListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthFirebase {

    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public void register(String email, String password, RegisterListener registerListener) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().getUser() != null) {
                registerListener.onComplete(task.getResult().getUser().getUid());
            }
        });
    }

    public void login(String email, String password, LoginOnSuccessListener onSuccessListener, LoginOnFailureListener onFailureListener) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener(task -> {
                onSuccessListener.onComplete();
            })
            .addOnFailureListener(command -> {
                onFailureListener.onComplete(command.getMessage());
            });
    }

    public void logout(LogoutListener lis) {
        firebaseAuth.signOut();
        lis.onComplete();
    }
    public boolean isSignedIn(){
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        return (currentUser != null);
    }

    public String getCurrentUserUid() {
        if (isSignedIn()) {
            return firebaseAuth.getCurrentUser().getUid();
        }
        return null;
    }

    public void isEmailExist(String email, IsEmailExistOnSuccessListener onSuccessListener, IsEmailExistOnFailureListener onFailureListener) {
        firebaseAuth.fetchSignInMethodsForEmail(email)
                .addOnSuccessListener(task -> {
                    if(task.getSignInMethods() != null) {
                        boolean isNewUser = task.getSignInMethods().isEmpty();
                        onSuccessListener.onComplete(!isNewUser);
                    }
                })
                .addOnFailureListener(command -> {
                    onFailureListener.onComplete(command.getMessage());
                });
    }
}
