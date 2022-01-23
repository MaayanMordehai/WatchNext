package com.example.watchnext.models.firebase;

import com.example.watchnext.models.firebase.users.interfaces.IsEmailExistListener;
import com.example.watchnext.models.firebase.users.interfaces.LoginListener;
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

    public void logout() {
        firebaseAuth.signOut();
    }
    public boolean isSignedIn(){
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        return (currentUser != null);
    }

    public void isEmailExist(String email, IsEmailExistListener lis) {
        firebaseAuth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener(task -> {
                        boolean isNewUser = task.getResult().getSignInMethods().isEmpty();
                        lis.onComplete(!isNewUser);
                });
    }
}
