package com.example.mumoblieappsproject.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mumoblieappsproject.R;
import com.example.mumoblieappsproject.model.SchoolFeature;

import java.util.List;

public class SchoolAdapter extends RecyclerView.Adapter<SchoolAdapter.SchoolViewHolder> {
    private List<SchoolFeature> schoolList;


    public SchoolAdapter(List<SchoolFeature> schoolList) {
        this.schoolList = schoolList;
    }

    // Create a card view (convert XML into a View)
    @NonNull
    @Override
    public SchoolViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_school, parent, false);
        return new SchoolViewHolder(view);
    }

    // Bind the data to the view (fill in the text).
    @Override
    public void onBindViewHolder(@NonNull SchoolViewHolder holder, int position) {
        SchoolFeature currentSchool = schoolList.get(position);

        // Bind Chinese school name, region and type
        holder.tvSchoolName.setText(currentSchool.properties.schoolNameTc);
        holder.tvDistrict.setText(currentSchool.properties.districtTc);
        holder.tvSchoolType.setText(currentSchool.properties.schoolTypeTc);
        // Added: Set a click event for the entire card.
        holder.itemView.setOnClickListener(v -> {
            // Prepare the Intent logic for the next phase three
            // Intent intent = new Intent(v.getContext(), SchoolDetailActivity.class);
            // intent.putExtra("SCHOOL_ID", currentSchool.properties.id);
            // v.getContext().startActivity(intent);

            // For now, let's use a log to prove that clicking the button takes effect.
            android.util.Log.d("ClickTest", "点击了: " + currentSchool.properties.schoolNameTc);
        });
    }

    //  Tell the total number of records in the list.
    @Override
    public int getItemCount() {
        return schoolList == null ? 0 : schoolList.size();
    }

    // ViewHolder: Responsible for locating the individual TextView controls within the card.
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
