package com.vuthaihung.loseflat.ui.activities;


import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.vuthaihung.loseflat.R;
import com.vuthaihung.loseflat.data.model.FeedBackResponse;
import com.vuthaihung.loseflat.service.ApiService;
import com.vuthaihung.loseflat.ui.base.BaseActivity;
import com.vuthaihung.loseflat.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedBackActivity extends BaseActivity {
    private EditText edtEmail,edtFullName,edtDeviceId,edtContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor(getResources().getColor(R.color.white));
        setContentView(R.layout.activity_feed_back);

        initViews();
        initEvents();
    }

    private void initEvents() {
        findViewById(R.id.iv_back_feedback).setOnClickListener(view -> onBackPressed());
        findViewById(R.id.btn_send).setOnClickListener(view -> handleSendFeedBack());
    }

    private void handleSendFeedBack() {
        if (Utils.isValidateEmail(edtEmail.getText().toString())){
            clickCallApiFeedBack();
        } else {
            Toast toast = Toast.makeText(this, "Please re-enter your email", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void initViews() {
       edtEmail = findViewById(R.id.edt_email_feedback);
       edtFullName = findViewById(R.id.edt_full_name);
       edtContent = findViewById(R.id.edt_content_report);
    }

    private void clickCallApiFeedBack() {
        ApiService.apiService.reportFeedBack(edtEmail.getText().toString(),edtFullName.getText().toString(),"losefat30","324234",
                        edtContent.getText().toString(),"0")
                .enqueue(new Callback<FeedBackResponse>() {
                    @Override
                    public void onResponse(Call<FeedBackResponse> call, Response<FeedBackResponse> response) {
                        Log.i("KMFG", "onResponse: success"+response);
                        Toast toast = Toast.makeText(getApplicationContext(), "Send feedback success", Toast.LENGTH_SHORT);
                        toast.show();
                    }

                    @Override
                    public void onFailure(Call<FeedBackResponse> call, Throwable t) {
                        Log.i("KMFG", "onResponse: fail");
                    }
                });
    }
}
