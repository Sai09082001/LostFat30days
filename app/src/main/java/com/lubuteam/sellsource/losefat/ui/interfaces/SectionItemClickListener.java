package com.lubuteam.sellsource.losefat.ui.interfaces;

import android.view.View;

import com.lubuteam.sellsource.losefat.data.model.SectionUser;

public interface SectionItemClickListener {
    void onSectionClick(View view, SectionUser sectionUser, int position);
}
