package com.direct2web.citysip.Activities.CommonActivities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.direct2web.citysip.Model.RestaurentModels.OtpSend.ResponseVerifyMobile;
import com.direct2web.citysip.R;
import com.direct2web.citysip.Utils.Api;
import com.direct2web.citysip.Utils.RetrofitClient;
import com.direct2web.citysip.Utils.WS_URL_PARAMS;
import com.direct2web.citysip.databinding.ActivityNumberVerificationBinding;
import com.google.gson.Gson;

import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NumberVerificationActivity extends AppCompatActivity {

    ActivityNumberVerificationBinding binding;
    String otp = "1234";

    ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_number_verification);


        Random random = new Random();
//        otp = String.format("%04d", random.nextInt(10000));
        Log.e("id", otp);

        binding.edtNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.toString().length() == 10) {
                    closeKeyboard();
                    binding.btnGetStarted.setTextColor(getResources().getColor(R.color.clr_f8f8f8));
                    binding.btnGetStarted.setBackground(getResources().getDrawable(R.drawable.button));
                    //Toast.makeText(NumberVerifyActivity.this, s.toString().length(), Toast.LENGTH_SHORT).show();
                }else {

                    binding.btnGetStarted.setTextColor(getResources().getColor(R.color.cle_979592));
                    binding.btnGetStarted.setBackground(getResources().getDrawable(R.drawable.button_disable));

                }

            }
        });


        binding.btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent = new Intent(NumberVerificationActivity.this,VerificationActivity.class);
//                startActivity(intent);

                if (binding.edtNumber.getText().toString().equals("")) {

                    binding.edtNumber.setError("Please Enter Number");


                } else {

                    sendMobile(binding.edtNumber.getText().toString());

                }

            }
        });


    }

    private void sendMobile(String number) {

        pd = new ProgressDialog(NumberVerificationActivity.this);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();

        Api api = RetrofitClient.getClient().create(Api.class);
        Call<ResponseVerifyMobile> call = api.sendVerifiMobile("Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject), WS_URL_PARAMS.access_key, number);
        call.enqueue(new Callback<ResponseVerifyMobile>() {
            @Override
            public void onResponse(Call<ResponseVerifyMobile> call, Response<ResponseVerifyMobile> response) {

                Log.e("response_otpsend", new Gson().toJson(response.body()));

                pd.dismiss();
                if (response.body() != null) {

                    if (!response.body().getError()) {

                        Intent i = new Intent(NumberVerificationActivity.this, VerificationActivity.class);
                        i.putExtra("username", number);
                        i.putExtra("msg", response.body().getMessage());
                        i.putExtra("otp", otp);
                        startActivity(i);

                        Toast.makeText(NumberVerificationActivity.this, otp, Toast.LENGTH_SHORT).show();

                    } else {

                        Intent i = new Intent(NumberVerificationActivity.this, IntroRegisterNowActivity.class);
                        i.putExtra("username", number);
                        i.putExtra("msg", response.body().getMessage());
                        i.putExtra("otp", otp);
                        startActivity(i);

                    }

                }

            }

            @Override
            public void onFailure(Call<ResponseVerifyMobile> call, Throwable t) {

                t.printStackTrace();
                pd.dismiss();

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