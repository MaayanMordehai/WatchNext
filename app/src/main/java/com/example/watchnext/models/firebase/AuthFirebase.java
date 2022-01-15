package com.example.watchnext.models.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthFirebase {

    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public boolean isSignedIn(){
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        return (currentUser != null);
    }

}
