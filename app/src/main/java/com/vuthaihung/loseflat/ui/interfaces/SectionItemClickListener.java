package com.vuthaihung.loseflat.ui.interfaces;

import android.view.View;

import com.vuthaihung.loseflat.data.model.SectionUser;

public interface SectionItemClickListener {
    void onSectionClick(View view, SectionUser sectionUser, int position);
}
