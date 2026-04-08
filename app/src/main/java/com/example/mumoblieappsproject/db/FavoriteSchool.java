package com.example.mumoblieappsproject.db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorite_schools")
public class FavoriteSchool {


    @PrimaryKey
    @NonNull
    public String schoolId;

    public String schoolNameTc;
    public String schoolNameEn;

    // Used to store handwritten memos by users (e.g., there's an open day in May).
    public String memo;

    // 构造函数
    public FavoriteSchool(@NonNull String schoolId, String schoolNameTc, String schoolNameEn) {
        this.schoolId = schoolId;
        this.schoolNameTc = schoolNameTc;
        this.schoolNameEn = schoolNameEn;
        this.memo = ""; // The default memo is empty.
    }
}
