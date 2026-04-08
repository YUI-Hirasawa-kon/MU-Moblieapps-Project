package com.example.mumoblieappsproject;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mumoblieappsproject.adapter.SchoolAdapter;
import com.example.mumoblieappsproject.model.SchoolFeature;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_VOICE = 1000;
    private EditText etSearch;
    private List<SchoolFeature> allSchoolsList = new ArrayList<>();
    private static final String TAG = "DataTest";

    private RecyclerView recyclerView;
    private SchoolAdapter adapter;

    private ImageButton btnOpenWishlist;

    private Button btnLanguageToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerViewSchools);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // This is just an initial empty shell for the Adapter; the actual data will be replaced later after the asynchronous loading is complete.
        adapter = new SchoolAdapter(new ArrayList<>(), this);

        // Binding adapter
        recyclerView.setAdapter(adapter);

        // Enable asynchronous thread loading to prevent UI lag.
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            String json = loadGeoJsonFromFile();
            List<SchoolFeature> schools = parseJson(json);

            if (schools != null) {
                // Switch back to the main thread to update the UI list
                runOnUiThread(() -> {
                    allSchoolsList.clear();
                    allSchoolsList.addAll(schools);
                    adapter.updateData(schools);
                    Log.d(TAG, "Data loading complete, have " + schools.size() + " recording");
                });
            }
        });

// The original call to loadSchoolData() has been completely removed here to avoid conflict errors.


        etSearch = findViewById(R.id.etSearch);
        ImageButton btnVoiceSearch = findViewById(R.id.btnVoiceSearch);

        // 1. Set up a text search listener
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterSchools(s.toString()); // 每次输入都触发过滤
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // 2. Set the click event for the voice search button
        btnVoiceSearch.setOnClickListener(v -> startVoiceRecognition());

        // 3. Add logic to update allSchoolsList
        /* runOnUiThread(() -> {
            allSchoolsList.clear();
            allSchoolsList.addAll(schools); // Save a complete backup
            adapter.updateData(schools);
        });
        */



        btnOpenWishlist = findViewById(R.id.fabWishlist);

        // Set a click event to redirect to the wishlist page.
        btnOpenWishlist.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, WishlistActivity.class);
            startActivity(intent);
        });

        // Find  toggle button in the onCreate method.
        Button btnLanguageToggle = findViewById(R.id.btnLanguageToggle);

        btnLanguageToggle.setOnClickListener(v -> {
            // Get the language setting of the current application
            LocaleListCompat currentLocales = AppCompatDelegate.getApplicationLocales();

            // If the current field is empty (following the system) or the current language is Chinese, then switch to English.
            if (currentLocales.isEmpty() || currentLocales.get(0).getLanguage().equals("zh")) {
                AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("en"));
                Toast.makeText(this, "Switched to English", Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, switch back to tc
                AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("zh"));
                Toast.makeText(this, "已切換為中文", Toast.LENGTH_SHORT).show();
            }
        });

    }

//    // ========== Multilingual switching function(useless) ==========
//    private void switchToChinese() {
//        LocaleListCompat appLocale = LocaleListCompat.forLanguageTags("zh");
//        AppCompatDelegate.setApplicationLocales(appLocale);
//    }
//
//    private void switchToEnglish() {
//
//        LocaleListCompat appLocale = LocaleListCompat.forLanguageTags("en");
//
//        AppCompatDelegate.setApplicationLocales(appLocale);
//    }

    // ========== Read local GeoJSON files ==========
    private String loadGeoJsonFromFile() {
        String json = null;
        try {
            InputStream is = getAssets().open("S_Schools_Profiles_converted.geojson");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            Log.e(TAG, "File reading failed", ex);
            return null;
        }
        return json;
    }

    // ========== Parse a JSON string into a list of objects ==========
    private List<SchoolFeature> parseJson(String json) {
        Gson gson = new Gson();
        List<SchoolFeature> schoolList = new ArrayList<>();
        if (json == null) return schoolList;

        try {
            JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
            JsonArray features = jsonObject.getAsJsonArray("features");

            for (int i = 0; i < features.size(); i++) {
                SchoolFeature feature = gson.fromJson(features.get(i), SchoolFeature.class);
                schoolList.add(feature);
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to parse JSON", e);
        }
        return schoolList;
    }
    private void startVoiceRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Please state the school you are looking for....");
        try {
            startActivityForResult(intent, REQUEST_CODE_VOICE);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Your device does not support voice input.", Toast.LENGTH_SHORT).show();
        }
    }
    // Receive speech recognition results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_VOICE && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && !result.isEmpty()) {
                String spokenText = result.get(0);
                etSearch.setText(spokenText); // Automatically fills in the search box, triggering TextWatcher's automatic search.
            }
        }
    }// ========== Local search filtering logic ==========
    private void filterSchools(String query) {
        List<SchoolFeature> filteredList = new ArrayList<>();
        if (query == null || query.trim().isEmpty()) {
            filteredList.addAll(allSchoolsList); // If empty, display all.
        } else {
            String lowerCaseQuery = query.toLowerCase();
            for (SchoolFeature school : allSchoolsList) {
                String nameTc = school.properties.schoolNameTc != null ? school.properties.schoolNameTc : "";
                String nameEn = school.properties.schoolNameEn != null ? school.properties.schoolNameEn.toLowerCase() : "";
                // Supports fuzzy matching of Chinese and English.
                if (nameTc.contains(lowerCaseQuery) || nameEn.contains(lowerCaseQuery)) {
                    filteredList.add(school);
                }
            }
        }
        adapter.updateData(filteredList); // Update adapter display
    }
}