package com.example.watchnext.models.room;

import androidx.room.Room;

import com.example.watchnext.ContextApplication;

public class WatchNextLocalDb {
    static public WatchNextLocalDbRepository db =
            Room.databaseBuilder(ContextApplication.getContext(),
                    WatchNextLocalDbRepository.class,
                    "watchNextDb.db")
                    .fallbackToDestructiveMigration()
                    .build();
}
