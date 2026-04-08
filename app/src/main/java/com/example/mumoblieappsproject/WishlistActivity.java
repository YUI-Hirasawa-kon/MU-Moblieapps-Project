package com.example.mumoblieappsproject;

import android.os.Bundle;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mumoblieappsproject.adapter.WishlistAdapter;
import com.example.mumoblieappsproject.db.AppDatabase;
import com.example.mumoblieappsproject.db.FavoriteSchool;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

public class WishlistActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private WishlistAdapter adapter;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);

        db = AppDatabase.getInstance(this);
        recyclerView = findViewById(R.id.recyclerViewWishlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the Adapter and implement the function to trigger a pop-up window when the interface is clicked.
        adapter = new WishlistAdapter(this, new ArrayList<>(), school -> showMemoDialog(school));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFavorites(); // Refresh data every time you return to the page
    }

    // ========== Database read ==========
    private void loadFavorites() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<FavoriteSchool> list = db.favoriteSchoolDao().getAllFavorites();
            runOnUiThread(() -> adapter.updateData(list));
        });
    }

    // ========== Memo pop-up and update logic ==========
    private void showMemoDialog(FavoriteSchool school) {
        boolean isEnglish = getResources().getConfiguration().locale.getLanguage().equals("en");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String title = isEnglish ? getString(R.string.memo_dialog_title_en) : getString(R.string.memo_dialog_title);
        builder.setTitle(title + " " + (isEnglish ? school.schoolNameEn : school.schoolNameTc));

        final EditText input = new EditText(this);
        input.setHint(isEnglish ? getString(R.string.memo_hint_en) : getString(R.string.memo_hint));
        input.setText(school.memo);
        builder.setView(input);

        builder.setPositiveButton(isEnglish ? "Save" : "保存", (dialog, which) -> {
            String newMemo = input.getText().toString().trim();
            saveMemoToDb(school.schoolId, newMemo);
        });
        builder.setNegativeButton(isEnglish ? "Cancel" : "取消", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    // ========== Database update ==========
    private void saveMemoToDb(String schoolId, String newMemo) {
        Executors.newSingleThreadExecutor().execute(() -> {
            // Call the update method of Dao
            db.favoriteSchoolDao().updateMemo(schoolId, newMemo);
            // After the update is complete, reload the list.
            loadFavorites();
        });
    }
}