package com.lubuteam.sellsource.losefat.ui.customViews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lubuteam.sellsource.losefat.R;
import com.lubuteam.sellsource.losefat.data.shared.AppSettings;
import com.lubuteam.sellsource.losefat.ui.interfaces.UnitTypeChangeListener;
import com.lubuteam.sellsource.losefat.utils.Constants;

public class WeightUnitSwitchView extends FrameLayout {
    private TextView txtKG, txtLB;
    private int typeUnit = Constants.UNIT_TYPE;
    private UnitTypeChangeListener listener;

    public WeightUnitSwitchView(Context context) {
        super(context);
        init();
    }

    public WeightUnitSwitchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WeightUnitSwitchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public WeightUnitSwitchView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.unit_switch_view, this);
        txtKG = findViewById(R.id.btn_kg);
        txtLB = findViewById(R.id.btn_lb);
        txtKG.setOnClickListener(view -> {
            if (typeUnit != 0) {
                typeUnit = 0;
                if (listener != null) {
                    listener.onChange(typeUnit);
                }
                refreshView();
                AppSettings.getInstance().setUnitType(typeUnit);
            }
        });
        txtLB.setOnClickListener(view -> {
            if (typeUnit != 1) {
                typeUnit = 1;
                if (listener != null) {
                    listener.onChange(typeUnit);
                }
                refreshView();
                AppSettings.getInstance().setUnitType(typeUnit);
            }
        });
        typeUnit = AppSettings.getInstance().getUnitType();
        refreshView();
    }

    private void refreshView() {
        if (typeUnit == 0) { // KG
            txtKG.setTextColor(getResources().getColor(R.color.white));
            txtKG.setBackgroundResource(R.drawable.bg_unit_fill);
            txtLB.setTextColor(getResources().getColor(R.color.text_gray));
            txtLB.setBackgroundResource(R.drawable.bg_unit_border);
        } else {
            txtKG.setTextColor(getResources().getColor(R.color.text_gray));
            txtKG.setBackgroundResource(R.drawable.bg_unit_border);
            txtLB.setTextColor(getResources().getColor(R.color.white));
            txtLB.setBackgroundResource(R.drawable.bg_unit_fill);
        }
    }

    public void setTypeUnit(int type) {
        typeUnit = type;
        refreshView();
    }

    public void setListener(UnitTypeChangeListener listener) {
        this.listener = listener;
        listener.onChange(typeUnit);
    }
}
