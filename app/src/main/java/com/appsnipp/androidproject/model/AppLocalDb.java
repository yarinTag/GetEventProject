package com.appsnipp.androidproject.model;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.appsnipp.androidproject.MyApplication;


@Database(entities = {User.class,Event.class,Product.class}, version = 15)
abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract EventDao eventDao();
    public abstract ProductDao productDao();
}
public class AppLocalDb{
    static public AppLocalDbRepository db =
            Room.databaseBuilder(MyApplication.context,
                    AppLocalDbRepository.class,
                    "dbFileName.db")
                    .fallbackToDestructiveMigration()
                    .build();
}