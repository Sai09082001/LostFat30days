package com.lubuteam.sellsource.losefat.ui.customViews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lubuteam.sellsource.losefat.R;
import com.lubuteam.sellsource.losefat.data.model.ChallengeDayUser;
import com.lubuteam.sellsource.losefat.data.repositories.SectionRepository;
import com.lubuteam.sellsource.losefat.ui.activities.SectionDetailActivity;

public class CheckDayView extends FrameLayout {

    private int state = 0;  // 0: not finish    1: prepare   2: finished
    private int day = 1;
    private View finished, not_finished;
    private TextView txtDay;
    private ChallengeDayUser data;
    private boolean lockClick = false;

    public CheckDayView(Context context) {
        super(context);
        init();
    }

    public CheckDayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CheckDayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public CheckDayView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @SuppressLint("CheckResult")
    private void init() {
        inflate(getContext(), R.layout.check_day_layout, this);
        finished = findViewById(R.id.finished);
        not_finished = findViewById(R.id.not_finished);
        txtDay = findViewById(R.id.txt_value);
        findViewById(R.id.container).setOnClickListener(view -> {
            if (state != 0 && !lockClick) {
                lockClick = true;
                SectionRepository.getInstance().getSectionUserByIdWithFullData(data.getData().getSectionId())
                        .subscribe(response -> {
                            Intent intent = new Intent(getContext(), SectionDetailActivity.class);
                            intent.putExtra("data", response);
                            intent.putExtra("challenge", data);
                            getContext().startActivity(intent);
                            lockClick = false;
                        });
            }
        });
        updateState();
    }

    public void setData(ChallengeDayUser challengeDayUser) {
//        Log.e("status", "set data: " + challengeDayUser.toString());
        this.data = challengeDayUser;
        this.state = challengeDayUser.getState();
        updateState();
    }

    public void reset() {
        state = 0;
        day = 1;
        updateState();
    }

    private void updateState() {
//        Log.e("status", "update state");
        switch (state) {
            case 0:
                finished.setVisibility(GONE);
                not_finished.setVisibility(VISIBLE);
                txtDay.setText(day + "");
                txtDay.setTextColor(getResources().getColor(R.color.text_gray_light));
                not_finished.setBackgroundResource(R.drawable.bg_circle_check_large_disable);
                break;
            case 1: // Prepare
//                Log.e("status", "isPrepare: " + day);
                finished.setVisibility(GONE);
                not_finished.setVisibility(VISIBLE);
                txtDay.setText(day + "");
                txtDay.setTextColor(getResources().getColor(R.color.colorAccent));
                not_finished.setBackgroundResource(R.drawable.bg_circle_check_large_prepare);
                break;
            case 2:
//                Log.e("status", "is finished");
                finished.setVisibility(VISIBLE);
                not_finished.setVisibility(GONE);
                break;
        }
    }
}
