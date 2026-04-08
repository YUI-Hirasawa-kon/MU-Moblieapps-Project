package com.example.mumoblieappsproject.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {FavoriteSchool.class}, version = 1, exportSchema = false)

public abstract class AppDatabase extends RoomDatabase {

    //Expose the DAO interface for external calls.
    public abstract FavoriteSchoolDao favoriteSchoolDao();

    // 单例模式，防止创建多个数据库实例
    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "school_wishlist_db")
                            .fallbackToDestructiveMigration() // allowing destructive migration
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
