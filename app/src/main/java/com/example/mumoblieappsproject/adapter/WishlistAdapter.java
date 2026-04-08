package com.example.mumoblieappsproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mumoblieappsproject.R;
import com.example.mumoblieappsproject.db.FavoriteSchool;
import java.util.List;
import java.util.Locale;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.WishViewHolder> {

    private List<FavoriteSchool> favList;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onMemoClick(FavoriteSchool school);
    }

    public WishlistAdapter(Context context, List<FavoriteSchool> favList, OnItemClickListener listener) {
        this.context = context;
        this.favList = favList;
        this.listener = listener;
    }

    public void updateData(List<FavoriteSchool> newList) {
        this.favList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, parent, false);
        return new WishViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WishViewHolder holder, int position) {
        FavoriteSchool school = favList.get(position);
        boolean isEnglish = Locale.getDefault().getLanguage().equals("en");

        if (isEnglish) {
            holder.tvName.setText(school.schoolNameEn != null ? school.schoolNameEn : school.schoolNameTc);
        } else {
            holder.tvName.setText(school.schoolNameTc != null ? school.schoolNameTc : school.schoolNameEn);
        }

        String memoText;
        if (school.memo != null && !school.memo.isEmpty()) {
            //  Use context.getString() to get string resources
            String notePrefix = isEnglish ? context.getString(R.string.note_prefix_en) : context.getString(R.string.note_prefix);
            memoText = notePrefix + ": " + school.memo;
        } else {
            memoText = isEnglish ? context.getString(R.string.click_add_note_en) : context.getString(R.string.click_add_note);
        }
        holder.tvMemo.setText(memoText);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onMemoClick(school);
        });
    }

    @Override
    public int getItemCount() {
        return favList == null ? 0 : favList.size();
    }

    //  Correction: All controls must be declared inside ViewHolder.
    static class WishViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvMemo;

        public WishViewHolder(@NonNull View itemView) {
            super(itemView);
            //android.R.layout.simple_list_item_2  ID
            tvName = itemView.findViewById(android.R.id.text1);
            tvMemo = itemView.findViewById(android.R.id.text2);

            tvName.setTextSize(18f);
        }
    }
}