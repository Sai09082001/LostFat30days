package com.applikeysolutions.cosmocalendar.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.applikeysolutions.cosmocalendar.adapter.DaysAdapter;
import com.applikeysolutions.cosmocalendar.model.Month;
import com.applikeysolutions.cosmocalendar.utils.Constants;

public class MonthView extends FrameLayout {

    private RecyclerView rvDays;

    public MonthView(@NonNull Context context) {
        super(context);
        init();
    }

    public MonthView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MonthView(@NonNull Context context,
                     @Nullable AttributeSet attrs,
                     @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MonthView(@NonNull Context context,
                     @Nullable AttributeSet attrs,
                     @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        rvDays = new RecyclerView(getContext());
        rvDays.setHasFixedSize(true);
        rvDays.setNestedScrollingEnabled(false);
        rvDays.setLayoutParams(generateLayoutParams());
        final GridLayoutManager manager = new GridLayoutManager(getContext(), Constants.DAYS_IN_WEEK){
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }

            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        rvDays.setLayoutManager(manager);
        addView(rvDays);
    }

    public void setAdapter(DaysAdapter adapter) {
        rvDays.setAdapter(adapter);
    }

    public DaysAdapter getAdapter() {
        return (DaysAdapter) rvDays.getAdapter();
    }

    public void initAdapter(Month month) {
        getAdapter().setMonth(month);
    }

    private FrameLayout.LayoutParams generateLayoutParams() {
        return new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}
