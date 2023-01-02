package com.direct2web.citysip.Activities.CommonActivities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.direct2web.citysip.R;
import com.direct2web.citysip.databinding.ActivityIntroRegisterNowBinding;

public class IntroRegisterNowActivity extends AppCompatActivity {

    ActivityIntroRegisterNowBinding binding;
    String otp = "", userName = "";

    ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_intro_register_now);

        otp = getIntent().getStringExtra("otp");
        userName = getIntent().getStringExtra("username");




        binding.btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(IntroRegisterNowActivity.this,OtpVerificationActivity.class);
                intent.putExtra("otp",otp);
                intent.putExtra("username",userName);
                startActivity(intent);
//

            }
        });


    }


    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}