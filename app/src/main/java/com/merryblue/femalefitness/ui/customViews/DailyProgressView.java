package com.merryblue.femalefitness.ui.customViews;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.merryblue.femalefitness.losefat.R;
import com.merryblue.femalefitness.data.model.DailySectionUser;

public class DailyProgressView extends FrameLayout {
    View locked, checked, progressLayout, restLayout;
    ProgressBar progressBar;
    TextView textView;
    int progress = 0;

    public DailyProgressView(Context context) {
        super(context);
        init();
    }

    public DailyProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DailyProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DailyProgressView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.daily_progress_view, this);
        locked = findViewById(R.id.locked);
        checked = findViewById(R.id.checked);
        progressLayout = findViewById(R.id.layout_progress);
        restLayout = findViewById(R.id.layout_rest);
        textView = findViewById(R.id.txt_progress);
        progressBar = findViewById(R.id.progress);
        progressBar.setMax(100);
        setProgress();
    }

    private void setProgress() {
        textView.setText(progress + "%");
        progressBar.setProgress(progress);
    }

    public void setData(DailySectionUser data) {
        if (data.isLocked()) {
            locked.setVisibility(View.VISIBLE);
            checked.setVisibility(View.GONE);
            progressLayout.setVisibility(View.GONE);
            restLayout.setVisibility(View.GONE);
        } else if (data.isCompleted()) {
            locked.setVisibility(View.GONE);
            checked.setVisibility(View.VISIBLE);
            progressLayout.setVisibility(View.GONE);
            restLayout.setVisibility(View.GONE);
        } else if (data.isRestDay()) {
            locked.setVisibility(View.GONE);
            checked.setVisibility(View.GONE);
            progressLayout.setVisibility(View.GONE);
            restLayout.setVisibility(View.VISIBLE);
        } else {
            locked.setVisibility(View.GONE);
            checked.setVisibility(View.GONE);
            progressLayout.setVisibility(View.VISIBLE);
            restLayout.setVisibility(View.GONE);
            progress = (int) data.getProgress();
            setProgress();
        }
    }

}
