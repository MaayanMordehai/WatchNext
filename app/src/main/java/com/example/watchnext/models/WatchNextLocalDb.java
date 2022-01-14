package com.example.watchnext.models;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.watchnext.ContextApplication;


@Database(entities = {Review.class, User.class}, version = 1)
abstract class WatchNextLocalDbRepository extends RoomDatabase {
    public abstract ReviewDao reviewDao();
    public abstract UserDao userDao();
}

public class WatchNextLocalDb {
    static public WatchNextLocalDbRepository db =
            Room.databaseBuilder(ContextApplication.getContext(),
                    WatchNextLocalDbRepository.class,
                    "dbFileName.db")
                    .fallbackToDestructiveMigration()
                    .build();
}

