package com.example.watchnext.models.firebase;

import com.example.watchnext.models.firebase.users.interfaces.LoginListener;
import com.example.watchnext.models.firebase.users.interfaces.LogoutListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthFirebase {

    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public void register(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password);
    }

    public void login(LoginListener lis, String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            lis.onComplete();
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

}
