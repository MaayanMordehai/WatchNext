package com.example.watchnext.modelviews;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.watchnext.models.Model;
import com.example.watchnext.models.entities.User;
import com.example.watchnext.models.firebase.users.interfaces.AddUserListener;
import com.example.watchnext.models.firebase.users.interfaces.LoginListener;
import com.example.watchnext.models.firebase.users.interfaces.UpdateUserListener;

import java.util.List;

public class UserViewModel extends ViewModel {
    LiveData<List<User>> data;

    public UserViewModel() {
        data = Model.instance.getAllUsers();
    }

    public LiveData<List<User>> getUsers() {return data;}

    public LiveData<User> getUserById(String id) {
        return Model.instance.getUserById(id);
    }

    public void register(AddUserListener lis, User user) {
        Model.instance.register(lis, user);
    }

    public void login(LoginListener lis, String email, String password){
        Model.instance.login(lis, email, password);
    }
    public void updateUser(UpdateUserListener lis, User user) {
        Model.instance.updateUser(lis, user);
    }

}
