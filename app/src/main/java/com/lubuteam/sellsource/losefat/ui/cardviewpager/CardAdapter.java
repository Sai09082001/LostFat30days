package com.lubuteam.sellsource.losefat.ui.cardviewpager;


import android.view.View;

public interface CardAdapter {

    public final int MAX_ELEVATION_FACTOR = 4;

    float getBaseElevation();

    View getCardViewAt(int position);

    int getCount();
}
