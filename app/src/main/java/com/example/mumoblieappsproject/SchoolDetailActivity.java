package com.example.mumoblieappsproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mumoblieappsproject.db.AppDatabase;
import com.example.mumoblieappsproject.db.FavoriteSchool;
import com.example.mumoblieappsproject.model.SchoolFeature;
import com.example.mumoblieappsproject.model.SchoolProperties;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.gson.Gson;
import android.provider.CalendarContract;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.Executors;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.ActionBar;

public class SchoolDetailActivity extends AppCompatActivity {

    private PieChart chartTeacherQual;
    private HorizontalBarChart chartTeacherExp;
    private SchoolFeature currentSchool;

    private TextView tvDetailSchoolName;
    private TextView tvDetailMotto;
    private TextView tvFacilities;

    private boolean isMissionExpanded = false; // Used to control the implementation of the school's mission./Collapsed

    private ImageButton btnFavoriteIcon;

    private ImageButton btnFavorite;
    private boolean isFavorited = false;
    private AppDatabase db;



    private Button btnCall;
    private Button btnEmail;
    private Button btnWeb;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_detail);


        chartTeacherQual = findViewById(R.id.chartTeacherQual);
        chartTeacherExp = findViewById(R.id.chartTeacherExp);
        tvDetailSchoolName = findViewById(R.id.tvDetailSchoolName);
        tvDetailMotto = findViewById(R.id.tvDetailMotto);
        tvFacilities = findViewById(R.id.tvFacilities);
        btnFavorite = findViewById(R.id.btnFavorite);
        btnCall = findViewById(R.id.btnCall);
        btnEmail = findViewById(R.id.btnEmail);
        btnWeb = findViewById(R.id.btnWeb);

        // First initialize the database
        db = AppDatabase.getInstance(this);

        // get data
        String schoolJson = getIntent().getStringExtra("school_data");
        if (schoolJson != null) {
            currentSchool = new Gson().fromJson(schoolJson, SchoolFeature.class);
            setupUI(currentSchool);
        } else {
            Toast.makeText(this, "Unable to obtain school information", Toast.LENGTH_SHORT).show();
            finish();
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.app_name);
        }
    }

    private void setupUI(SchoolFeature school) {






        String schoolId = school.properties.id;
        Executors.newSingleThreadExecutor().execute(() -> {
            FavoriteSchool existing = db.favoriteSchoolDao().getFavoriteById(schoolId);
            isFavorited = (existing != null);
            runOnUiThread(() -> updateFavoriteButtonUI());
        });

        SchoolProperties p = school.properties;
        boolean isEnglish = getResources().getConfiguration().locale.getLanguage().equals("en");

        // ========= 1. Header information =========
        tvDetailSchoolName.setText(isEnglish ? p.schoolNameEn : p.schoolNameTc);

        // School motto
        String motto = isEnglish ? p.schoolMottoEn : p.schoolMottoTc;
        if (motto != null && !motto.trim().isEmpty()) {
            tvDetailMotto.setText((isEnglish ? getString(R.string.motto_prefix_en) : getString(R.string.motto_prefix)) + "：" + motto);
            tvDetailMotto.setVisibility(View.VISIBLE);
        } else {
            tvDetailMotto.setVisibility(View.GONE);
        }

        // address
        TextView tvAddress = findViewById(R.id.tvDetailAddress);
        String address = isEnglish ? p.schoolAddressEn : p.schoolAddressTc;
        tvAddress.setText((isEnglish ? getString(R.string.address_prefix_en) : getString(R.string.address_prefix)) + "：" + (address != null ? address : "No data available."));

        // ========= 2. School Mission and Background =========
        TextView tvReligion = findViewById(R.id.tvDetailReligion);
        String religion = isEnglish ? p.religionEn : p.religionTc;
        tvReligion.setText(getString(R.string.religion_bg) + "：" + (religion != null ? religion : "None"));

        TextView tvMission = findViewById(R.id.tvDetailMission);
        String mission = isEnglish ? p.schoolMissionEn : p.schoolMissionTc;
        tvMission.setText(mission != null ? mission.replace("<br>", "\n") : "No data available");

        TextView tvMissionToggle = findViewById(R.id.tvMissionToggle);
        tvMissionToggle.setText(isEnglish ? getString(R.string.expand) : getString(R.string.expand));
        tvMissionToggle.setOnClickListener(v -> {
            if (isMissionExpanded) {
                tvMission.setMaxLines(3);
                tvMissionToggle.setText(isEnglish ? getString(R.string.expand) : getString(R.string.expand));
            } else {
                tvMission.setMaxLines(Integer.MAX_VALUE);
                tvMissionToggle.setText(isEnglish ? getString(R.string.collapse) : getString(R.string.collapse));
            }
            isMissionExpanded = !isMissionExpanded;
        });

        // ========= 3. Academics and Admissions =========
        TextView tvLang = findViewById(R.id.tvLanguagePolicy);
        String lang = isEnglish ? p.languagePolicyEn : p.languagePolicyTc;
        // Temporary solution: Display Chinese characters if no English fields are available.
        if (isEnglish && (p.languagePolicyEn == null || p.languagePolicyEn.isEmpty())) {
            lang = p.languagePolicyTc;
        }
        tvLang.setText(getString(R.string.language_policy) + "：\n" + (lang != null ? lang.replace("<br>", "\n") : "No data available"));

        TextView tvAdmis = findViewById(R.id.tvAdmission);
        String admission = isEnglish ? p.s1AdmissionEn : p.s1AdmissionTc;
        if (isEnglish && (p.s1AdmissionEn == null || p.s1AdmissionEn.isEmpty())) admission = p.s1AdmissionTc;
        tvAdmis.setText(getString(R.string.admission_criteria) + "：\n" + (admission != null ? admission.replace("<br>", "\n") : "No data available"));

        TextView tvClasses = findViewById(R.id.tvClasses);
        String classStruct = getString(R.string.class_structure) + "：\ns1(" + formatNull(p.classS1) + ") s2(" + formatNull(p.classS2) + ") s3(" + formatNull(p.classS3) +
                ")\ns4(" + formatNull(p.classS4) + ") s5(" + formatNull(p.classS5) + ") s6(" + formatNull(p.classS6) + ")";
        tvClasses.setText(classStruct);

        // ========= 4. Faculty and staff team (chart) =========
        setupExpBarChart(p, isEnglish);
        setupQualChart(school, isEnglish);

        // ========= 5. school facilities =========
        TextView tvFac = findViewById(R.id.tvFacilities);
        String facilities = isEnglish ? p.schoolFacilitiesEn : p.schoolFacilitiesTc;
        tvFac.setText((isEnglish ? getString(R.string.campus_facilities) : getString(R.string.campus_facilities)) + "：\n" + (facilities != null ? facilities.replace("<br>", "\n") : "No data available"));

        // ========= 6. Shortcut button click events =========
        btnCall.setOnClickListener(v -> {
            if (p.schoolTel != null && !p.schoolTel.isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + p.schoolTel));
                startActivity(intent);
            } else {
                Toast.makeText(this, "No phone number available", Toast.LENGTH_SHORT).show();
            }
        });

        btnWeb.setOnClickListener(v -> {
            if (p.schoolWebsite != null && !p.schoolWebsite.isEmpty()) {
                String url = p.schoolWebsite.startsWith("http") ? p.schoolWebsite : "http://" + p.schoolWebsite;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            } else {
                Toast.makeText(this, "No website link available", Toast.LENGTH_SHORT).show();
            }
        });

        btnEmail.setOnClickListener(v -> {
            if (p.schoolEmail != null && !p.schoolEmail.isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + p.schoolEmail));
                startActivity(intent);
            } else {
                Toast.makeText(this, "No email address available", Toast.LENGTH_SHORT).show();
            }
        });






        //wish list function
        btnFavorite.setOnClickListener(v -> {
            Executors.newSingleThreadExecutor().execute(() -> {
                if (isFavorited) {
                    FavoriteSchool toDelete = new FavoriteSchool(schoolId, p.schoolNameTc, p.schoolNameEn);
                    db.favoriteSchoolDao().delete(toDelete);
                    runOnUiThread(() -> {
                        isFavorited = false;
                        updateFavoriteButtonUI();
                        Toast.makeText(this, "Removed from favorites", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    FavoriteSchool toInsert = new FavoriteSchool(schoolId, p.schoolNameTc, p.schoolNameEn);
                    db.favoriteSchoolDao().insert(toInsert);
                    runOnUiThread(() -> {
                        isFavorited = true;
                        updateFavoriteButtonUI();
                        Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show();
                    });
                }
            });
        });

    }


    private void setupExpBarChart(SchoolProperties p, boolean isEnglish) {
        chartTeacherExp.getDescription().setEnabled(false);
        chartTeacherExp.getLegend().setEnabled(false);
        chartTeacherExp.getAxisRight().setEnabled(false);
        chartTeacherExp.getAxisLeft().setAxisMinimum(0f);

        float exp0_4 = safeParseFloat(p.exp0to4);
        float exp5_9 = safeParseFloat(p.exp5to9);
        float exp10 = safeParseFloat(p.exp10Plus);

        if (exp0_4 == 0 && exp5_9 == 0 && exp10 == 0) {
            chartTeacherExp.setNoDataText("The school did not provide data on teachers' years of service.");
            return;
        }

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, exp0_4));
        entries.add(new BarEntry(1f, exp5_9));
        entries.add(new BarEntry(2f, exp10));

        BarDataSet dataSet = new BarDataSet(entries, "Seniority Ratio");
        dataSet.setColors(new int[]{
                android.graphics.Color.parseColor("#81D4FA"),
                android.graphics.Color.parseColor("#29B6F6"),
                android.graphics.Color.parseColor("#0288D1")
        });
        dataSet.setValueTextSize(12f);

        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.6f);
        chartTeacherExp.setData(barData);

        // Set X-axis labels (declare only once)
        XAxis xAxis = chartTeacherExp.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);

        String[] labels;
        if (isEnglish) {
            labels = new String[]{getString(R.string.exp_0_4_en), getString(R.string.exp_5_9_en), getString(R.string.exp_10_plus_en)};
        } else {
            labels = new String[]{getString(R.string.exp_0_4), getString(R.string.exp_5_9), getString(R.string.exp_10_plus)};
        }
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

        chartTeacherExp.animateY(1000);
        chartTeacherExp.invalidate();
    }


    private void setupQualChart(SchoolFeature school, boolean isEnglish) {
        try {
            String bachelorStr = school.properties.tsi_percent_of_bachelor;
            String masterStr = school.properties.tsi_percent_of_master_doctorate_or_above;

            float totalBachelor = safeParseFloat(bachelorStr);
            float masterPlus = safeParseFloat(masterStr);

            float onlyBachelor = totalBachelor - masterPlus;
            if (onlyBachelor < 0) onlyBachelor = 0;

            if (totalBachelor == 0 && masterPlus == 0) {
                chartTeacherQual.setNoDataText("The school did not provide academic data");
                chartTeacherQual.setData(null);
                chartTeacherQual.invalidate();
                return;
            }

            // First, create a list of entries.
            ArrayList<PieEntry> entries = new ArrayList<>();
            if (onlyBachelor > 0) {
                entries.add(new PieEntry(onlyBachelor, isEnglish ? getString(R.string.only_bachelor_en) : getString(R.string.only_bachelor)));
            }
            if (masterPlus > 0) {
                entries.add(new PieEntry(masterPlus, isEnglish ? getString(R.string.master_plus_en) : getString(R.string.master_plus)));
            }

            PieDataSet dataSet = new PieDataSet(entries, "");
            dataSet.setColors(new int[]{
                    android.graphics.Color.parseColor("#AB47BC"),
                    android.graphics.Color.parseColor("#26A69A")
            });
            dataSet.setValueTextSize(14f);
            dataSet.setValueTextColor(android.graphics.Color.WHITE);

            chartTeacherQual.setDrawHoleEnabled(true);
            chartTeacherQual.setHoleRadius(50f);
            chartTeacherQual.setCenterText(isEnglish ? getString(R.string.qualification_chart_title_en) : getString(R.string.qualification_chart_title));
            chartTeacherQual.setCenterTextSize(14f);

            chartTeacherQual.setData(new PieData(dataSet));
            chartTeacherQual.getDescription().setEnabled(false);
            chartTeacherQual.animateY(1000);
            chartTeacherQual.invalidate();

        } catch (Exception e) {
            e.printStackTrace();
            chartTeacherQual.setNoDataText("Data parsing error");
            chartTeacherQual.invalidate();
        }
    }




    // 🛡️ Safe conversion of strings to floating-point numbers
    private float safeParseFloat(String value) {
        if (value == null || value.trim().isEmpty() || value.equals("-")) {
            return 0f;
        }
        try {
            String cleanValue = value.replace("%", "").trim();
            return Float.parseFloat(cleanValue);
        } catch (NumberFormatException e) {
            return 0f;
        }
    }

    // Small tool: Handling cases where the number of classes is empty.
    private String formatNull(String value) {
        return (value != null && !value.isEmpty() && !value.equals("-")) ? value : "0";
    }


    private void updateFavoriteButtonUI() {
        if (isFavorited) {
            // Use the same "on" status icon as the homepage.
            btnFavorite.setImageResource(android.R.drawable.btn_star_big_on);
        } else {

            btnFavorite.setImageResource(android.R.drawable.btn_star_big_off);
        }
    }


}