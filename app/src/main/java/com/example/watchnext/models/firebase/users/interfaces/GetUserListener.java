package com.example.watchnext.models.firebase.users.interfaces;

import com.example.watchnext.models.entities.User;

public interface GetUserListener {
    void onComplete(User user);

}
