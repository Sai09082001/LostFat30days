package com.merryblue.femalefitness.ui.adapters;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.merryblue.femalefitness.losefat.R;
import com.merryblue.femalefitness.data.model.DailySectionUser;
import com.merryblue.femalefitness.ui.activities.RestDayActivity;
import com.merryblue.femalefitness.ui.activities.SectionDetailActivity;
import com.merryblue.femalefitness.ui.customViews.DailyProgressView;

import java.util.ArrayList;
import java.util.List;

public class DailySectionAdapter extends RecyclerView.Adapter<DailySectionAdapter.ViewHolder> {
    List<DailySectionUser> data;

    public DailySectionAdapter() {
        data = new ArrayList<>();
    }

    public void setList(List<DailySectionUser> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_daily_section, parent, false);
        return new ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View layout;
        TextView textView, tvStartSection, tvDailyExercises;
        DailyProgressView progressView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            layout = itemView.findViewById(R.id.layout);
            textView = itemView.findViewById(R.id.txt_title);
            tvDailyExercises = itemView.findViewById(R.id.txt_daily_exercises);
         //   tvStartSection = itemView.findViewById(R.id.tv_start_section);
            progressView = itemView.findViewById(R.id.daily_progress);
        }

        void bind(DailySectionUser item) {
            if (item.isLocked()) {
                layout.setBackgroundColor(Color.WHITE);
                textView.setTextColor(Color.BLACK);
                tvDailyExercises.setTextColor(Color.GRAY);
            } else if (item.isCompleted()) {
                layout.setBackgroundColor(Color.WHITE);
                textView.setTextColor(Color.BLACK);
            } else {
                layout.setBackgroundResource(R.drawable.bg_row_daily_section);
                textView.setTextColor(Color.WHITE);
                tvDailyExercises.setTextColor(Color.WHITE);
            }
            textView.setText(item.getData().getData().getData().getTitleDisplay().toUpperCase());
            progressView.setData(item);
        }

        @Override
        public void onClick(View view) {
            DailySectionUser dailySectionUser = data.get(getAdapterPosition());
            if (!dailySectionUser.isLocked()) {
                if (dailySectionUser.isRestDay()) {
                    Intent intent = new Intent(view.getContext(), RestDayActivity.class);
                    intent.putExtra("data", dailySectionUser.getData().getData());
                    intent.putExtra("challenge", dailySectionUser);
                    view.getContext().startActivity(intent);
                } else {
                    Intent intent = new Intent(view.getContext(), SectionDetailActivity.class);
                    intent.putExtra("data", dailySectionUser.getData().getData());
                    intent.putExtra("challenge", dailySectionUser);
                    view.getContext().startActivity(intent);
                }
            }
        }
    }
}
