package com.example.watchnext.models.firebase.users.interfaces;

import com.example.watchnext.models.entities.User;

import java.util.List;

public interface GetAllUsersListener {
    void onComplete(List<User> list);

}
