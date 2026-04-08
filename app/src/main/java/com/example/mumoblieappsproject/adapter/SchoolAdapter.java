package com.example.mumoblieappsproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mumoblieappsproject.R;
import com.example.mumoblieappsproject.SchoolDetailActivity;
import com.example.mumoblieappsproject.model.SchoolFeature;
import com.example.mumoblieappsproject.model.SchoolProperties;
import com.google.gson.Gson;

import java.util.List;
import java.util.Locale;

public class SchoolAdapter extends RecyclerView.Adapter<SchoolAdapter.SchoolViewHolder> {

    private List<SchoolFeature> schoolList;
    private Context context;

    // Constructor: Receives a list of data and a Context.
    public SchoolAdapter(List<SchoolFeature> schoolList, Context context) {
        this.schoolList = schoolList;
        this.context = context;
    }

    public void updateData(List<SchoolFeature> newList) {
        this.schoolList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SchoolViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (context == null) context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_school, parent, false);
        return new SchoolViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SchoolViewHolder holder, int position) {
        SchoolFeature school = schoolList.get(position);
        SchoolProperties p = school.properties;

        boolean isEnglish = context.getResources().getConfiguration().locale.getLanguage().equals("en");


        if (isEnglish) {
            holder.tvSchoolName.setText(p.schoolNameEn != null && !p.schoolNameEn.isEmpty() ? p.schoolNameEn : p.schoolNameTc);
            holder.tvDistrict.setText(p.districtEn != null ? p.districtEn : p.districtTc);
            holder.tvSchoolType.setText(p.schoolTypeEn != null ? p.schoolTypeEn : p.schoolTypeTc);
        } else {
            holder.tvSchoolName.setText(p.schoolNameTc);
            holder.tvDistrict.setText(p.districtTc);
            holder.tvSchoolType.setText(p.schoolTypeTc);
        }



        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, SchoolDetailActivity.class);
            String schoolJson = new Gson().toJson(school);
            intent.putExtra("school_data", schoolJson);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return schoolList == null ? 0 : schoolList.size();
    }

    //  Declare all controls completely in ViewHolder
    public static class SchoolViewHolder extends RecyclerView.ViewHolder {
        TextView tvSchoolName, tvDistrict, tvSchoolType;


        public SchoolViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSchoolName = itemView.findViewById(R.id.tvSchoolName);
            tvDistrict = itemView.findViewById(R.id.tvDistrict);
            tvSchoolType = itemView.findViewById(R.id.tvSchoolType);


        }
    }
}