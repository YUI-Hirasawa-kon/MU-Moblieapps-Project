package com.example.mumoblieappsproject;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mumoblieappsproject.model.SchoolFeatureCollection;
import com.google.gson.Gson;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "DataTest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        loadSchoolData();
    }

    private void loadSchoolData() {
        try {
            // read assets
            InputStream is = getAssets().open("S_Schools_Profiles_converted.geojson");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            // Convert bytes to strings
            String jsonString = new String(buffer, StandardCharsets.UTF_8);

            // Using Gson for parsing
            Gson gson = new Gson();
            SchoolFeatureCollection collection = gson.fromJson(jsonString, SchoolFeatureCollection.class);

            // test result
            if (collection != null && collection.features != null) {
                Log.d(TAG, "Successfully parsed " + collection.features.size() + " Data from schools！");

                // print first school name
                String firstSchoolName = collection.features.get(0).properties.schoolNameTc;
                Log.d(TAG, "The first school was : " + firstSchoolName);
            }

        } catch (Exception e) {
            Log.e(TAG, "Failed to parse JSON", e);
        }
    }
}