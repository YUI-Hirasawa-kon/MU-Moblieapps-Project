package com.example.mumoblieappsproject.db;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FavoriteSchoolDao {

    //Add: Insert a favorite school; replace if it already exists.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(FavoriteSchool school);

    // Delete: Remove from favorites
    @Delete
    void delete(FavoriteSchool school);

    // Search: Get a list of all saved schools
    @Query("SELECT * FROM favorite_schools")
    List<FavoriteSchool> getAllFavorites();

    // Search: Check if a school has been saved by its ID.
    @Query("SELECT * FROM favorite_schools WHERE schoolId = :id LIMIT 1")
    FavoriteSchool getFavoriteById(String id);

    // update : Update the memo content for specific schools.
    @Query("UPDATE favorite_schools SET memo = :newMemo WHERE schoolId = :id")
    void updateMemo(String id, String newMemo);
}
