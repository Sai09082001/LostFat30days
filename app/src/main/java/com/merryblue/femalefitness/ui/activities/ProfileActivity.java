package com.merryblue.femalefitness.ui.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.Toolbar;

import com.ads.control.AdmobHelp;
import com.merryblue.femalefitness.losefat.R;
import com.merryblue.femalefitness.data.shared.AppSettings;
import com.merryblue.femalefitness.ui.base.BaseActivity;
import com.merryblue.femalefitness.ui.dialogs.BMIDialogFragment;
import com.merryblue.femalefitness.ui.dialogs.BirthdayDialog;
import com.merryblue.femalefitness.ui.interfaces.DialogResultListener;
import com.merryblue.femalefitness.utils.DateUtils;
import com.merryblue.femalefitness.utils.Utils;

import java.math.RoundingMode;
import java.text.DecimalFormat;


public class ProfileActivity extends BaseActivity implements DialogResultListener {

    private AppCompatRadioButton rbKg, rbLb;
    private TextView txtWeight, txtHeight, txtBirthday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        setContentView(R.layout.activity_profile);

        initViews();
        initEvents();
        refreshData();
    }

    private void initEvents() {
        rbKg.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                AppSettings.getInstance().setUnitType(0);
                refreshData();
            }
        });
        rbLb.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                AppSettings.getInstance().setUnitType(1);
                refreshData();
            }
        });
        View.OnClickListener onClickListener = view -> {
            new BMIDialogFragment(this).show(getSupportFragmentManager(), null);
        };
        findViewById(R.id.row_height).setOnClickListener(onClickListener);
        findViewById(R.id.row_weight).setOnClickListener(onClickListener);

        findViewById(R.id.row_birthday).setOnClickListener(view -> {
            new BirthdayDialog(this).show(getSupportFragmentManager(), null);
        });

    }

    private void refreshData() {
        int type = AppSettings.getInstance().getUnitType();
        float weight = AppSettings.getInstance().getWeightDefault();
        float height = AppSettings.getInstance().getHeightDefault();
        long birthday = AppSettings.getInstance().getBirthday();
        if (type == 1) {
            weight = Utils.convertKgToLbs(weight);
            height = Utils.convertCmToFt(height);
            rbLb.setChecked(true);
        } else {
            rbKg.setChecked(true);
        }
        DecimalFormat df = new DecimalFormat("#.##");
        DecimalFormat df2 = new DecimalFormat("#");
        df.setRoundingMode(RoundingMode.HALF_UP);
        df2.setRoundingMode(RoundingMode.HALF_UP);

        txtWeight.setText(df.format(weight) + (type == 1 ? " LB" : " KG"));
        txtHeight.setText((type == 0 ? df2.format(height) : df.format(height)) + (type == 1 ? " FT" : " CM"));
        txtBirthday.setText(DateUtils.formatBirthday(birthday));
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        txtBirthday = findViewById(R.id.txt_birthday);
        txtHeight = findViewById(R.id.txt_height);
        txtWeight = findViewById(R.id.txt_weight);

        rbKg = findViewById(R.id.rb_kg);
        rbLb = findViewById(R.id.rb_lb);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    @Override
    public void onResult(int type, Object value) {
        refreshData();
    }

    @Override
    public void onBackPressed() {
        AdmobHelp.getInstance().showInterstitialAd(this, () -> finish());

    }
}
