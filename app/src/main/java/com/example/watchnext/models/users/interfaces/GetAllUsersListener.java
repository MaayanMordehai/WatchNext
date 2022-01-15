package com.example.watchnext.models.users.interfaces;

import com.example.watchnext.models.users.User;

import java.util.List;

public interface GetAllUsersListener {
    void onComplete(List<User> list);

}
