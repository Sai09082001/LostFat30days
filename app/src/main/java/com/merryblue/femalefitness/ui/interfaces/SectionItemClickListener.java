package com.merryblue.femalefitness.ui.interfaces;

import android.view.View;

import com.merryblue.femalefitness.data.model.SectionUser;

public interface SectionItemClickListener {
    void onSectionClick(View view, SectionUser sectionUser, int position);
}
