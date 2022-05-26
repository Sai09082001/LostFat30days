package com.lubuteam.sellsource.losefat.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.lubuteam.sellsource.losefat.R;
import com.lubuteam.sellsource.losefat.data.model.Section;
import com.lubuteam.sellsource.losefat.data.model.SectionUser;
import com.lubuteam.sellsource.losefat.data.repositories.SectionRepository;
import com.lubuteam.sellsource.losefat.ui.activities.SectionDetailActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TrainingAdapter extends RecyclerView.Adapter<TrainingAdapter.ViewHolder> {
    List<SectionUser> data;

    public TrainingAdapter() {
        data = new ArrayList<>();
    }

    public void setList(List<SectionUser> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TrainingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_training_section, parent, false);
        return new TrainingAdapter.ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull TrainingAdapter.ViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
        View btnOption;
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            textView = itemView.findViewById(R.id.txt_title);
            btnOption = itemView.findViewById(R.id.btn_option);
            btnOption.setOnClickListener(this);
        }

        void bind(SectionUser item) {
            textView.setText(item.getData().getTitleDisplay());
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.btn_option) {
                showPopup(view);
            } else {
                SectionUser sectionUser = data.get(getAdapterPosition());
                Intent intent = new Intent(view.getContext(), SectionDetailActivity.class);
                intent.putExtra("data", sectionUser);
                view.getContext().startActivity(intent);
            }
        }

        private void showPopup(View view) {
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            MenuInflater inflater = popupMenu.getMenuInflater();
            inflater.inflate(R.menu.training_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.show();
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (item.getItemId() == R.id.menu_rename) {
                rename();
                return true;
            } else if (item.getItemId() == R.id.menu_delete) {
                delete();
                return true;
            }
            return false;
        }

        @SuppressLint("CheckResult")
        private void delete() {
            SectionRepository.getInstance().delete(data.get(getAdapterPosition()).getData()).subscribe(() -> {
                SectionRepository.getInstance().delete(data.get(getAdapterPosition())).subscribe();
            });
        }

        @SuppressLint("CheckResult")
        private void rename() {
            Section section = data.get(getAdapterPosition()).getData();
            View view = LayoutInflater.from(itemView.getContext()).inflate(R.layout.enter_name_training, null);
            AppCompatEditText editText = view.findViewById(R.id.edt_name);
            editText.setText(section.getTitle());
            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
            builder.setTitle(itemView.getResources().getString(R.string.title_dialog_rename_training));
            builder.setView(view);
            builder.setPositiveButton(itemView.getResources().getString(R.string.ok), (dialogInterface, i) -> {
                // rename training
                String txtValue = editText.getText().toString();
                if (!TextUtils.isEmpty(txtValue)) {
                    dialogInterface.dismiss();
                    section.setTitle(editText.getText().toString());
                    SectionRepository.getInstance().updateSection(section).subscribe(() -> {
                        SectionUser sectionUser = data.get(getAdapterPosition());
                        sectionUser.setUpdated(Calendar.getInstance().getTime());
                        SectionRepository.getInstance().updateSectionUser(sectionUser).subscribe();
                    });
                } else {
                    Toast.makeText(itemView.getContext(), itemView.getResources().getString(R.string.required_missing), Toast.LENGTH_LONG).show();
                }
            });
            builder.setNegativeButton(itemView.getResources().getString(R.string.cancel), (dialogInterface, i) -> dialogInterface.dismiss());
            builder.create().show();
        }
    }
}
