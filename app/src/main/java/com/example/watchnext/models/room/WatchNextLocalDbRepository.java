package com.example.watchnext.models.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.watchnext.models.entities.Review;
import com.example.watchnext.models.room.daos.ReviewDao;
import com.example.watchnext.models.entities.User;
import com.example.watchnext.models.room.daos.UserDao;

@Database(entities = {Review.class, User.class}, version = 1)
public abstract class WatchNextLocalDbRepository extends RoomDatabase {

    public abstract ReviewDao reviewDao();
    public abstract UserDao userDao();

}

