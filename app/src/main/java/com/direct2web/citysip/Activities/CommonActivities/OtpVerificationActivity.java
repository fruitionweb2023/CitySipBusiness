package com.direct2web.citysip.Activities.CommonActivities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.direct2web.citysip.Model.RestaurentModels.CreateAccount.ResponseCreateAccount;
import com.direct2web.citysip.Model.RestaurentModels.OtpSend.ResponseOtpSend;
import com.direct2web.citysip.Model.RestaurentModels.Profile.ResponseSendProfile;
import com.direct2web.citysip.R;
import com.direct2web.citysip.Utils.Api;
import com.direct2web.citysip.Utils.ConnectionToInternet;
import com.direct2web.citysip.Utils.CustomTextChangeListener;
import com.direct2web.citysip.Utils.RetrofitClient;
import com.direct2web.citysip.Utils.SessionManager;
import com.direct2web.citysip.Utils.WS_URL_PARAMS;
import com.direct2web.citysip.databinding.ActivityOtpVerificationBinding;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpVerificationActivity extends AppCompatActivity {

    ActivityOtpVerificationBinding binding;

    String strOtp, otp;

    public int counter = 30;

    SessionManager sessionManager;
    String strUsername, strEmail = "", strPassword = "";
    ProgressDialog pd;

    int flag = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_otp_verification);

        sessionManager = new SessionManager(this);

        strUsername = getIntent().getStringExtra("username");
//        strEmail = getIntent().getStringExtra("email");
//        strPassword = getIntent().getStringExtra("password");
        otp = getIntent().getStringExtra("otp");

        binding.edtNumber.setText(strUsername);


//        binding.edtOne.addTextChangedListener(new GenericTextWatcher(binding.edtOne, binding.edtTwo));
//        binding.edtTwo.addTextChangedListener(new GenericTextWatcher(binding.edtTwo, binding.edtThree));
//        binding.edtThree.addTextChangedListener(new GenericTextWatcher(binding.edtThree, binding.edtFour));
//        binding.edtFour.addTextChangedListener(new GenericTextWatcher(binding.edtFour, null));
//
//        binding.edtTwo.setOnKeyListener(new GenericKeyEvent(binding.edtTwo, binding.edtOne));
//        binding.edtThree.setOnKeyListener(new GenericKeyEvent(binding.edtThree, binding.edtTwo));
//        binding.edtFour.setOnKeyListener(new GenericKeyEvent(binding.edtFour, binding.edtThree));


        binding.edtOne.addTextChangedListener(new CustomTextChangeListener(OtpVerificationActivity.this,binding.edtOne, binding.edtTwo, binding.edtOne,false,binding.btnVerify));
        binding.edtTwo.addTextChangedListener(new CustomTextChangeListener(OtpVerificationActivity.this,binding.edtTwo, binding.edtThree, binding.edtOne,false,binding.btnVerify));
        binding.edtThree.addTextChangedListener(new CustomTextChangeListener(OtpVerificationActivity.this,binding.edtThree, binding.edtFour, binding.edtTwo,false,binding.btnVerify));
        binding.edtFour.addTextChangedListener(new CustomTextChangeListener(OtpVerificationActivity.this,binding.edtFour, binding.edtFour, binding.edtThree,true,binding.btnVerify));

        /*if (binding.edtOne.getText().toString().length() > 0) {
            binding.edtOne.setBackground(getResources().getDrawable(R.drawable.four_dp_corner_box_green));
        } else {
            binding.edtOne.setBackground(getResources().getDrawable(R.drawable.four_dp_corner_box_two_dp_thik));

        }
        if (binding.edtTwo.getText().toString().length() > 0) {
            binding.edtTwo.setBackground(getResources().getDrawable(R.drawable.four_dp_corner_box_green));
        } else {
            binding.edtTwo.setBackground(getResources().getDrawable(R.drawable.four_dp_corner_box_two_dp_thik));

        }
        if (binding.edtThree.getText().toString().length() > 0) {
            binding.edtThree.setBackground(getResources().getDrawable(R.drawable.four_dp_corner_box_green));
        } else {
            binding.edtThree.setBackground(getResources().getDrawable(R.drawable.four_dp_corner_box_two_dp_thik));

        }
        if (binding.edtFour.getText().toString().length() > 0) {
            binding.edtFour.setBackground(getResources().getDrawable(R.drawable.four_dp_corner_box_green));
        } else {
            binding.edtFour.setBackground(getResources().getDrawable(R.drawable.four_dp_corner_box_two_dp_thik));

        }*/

        reverseTimer(counter,binding.txtTime,binding.btnResend);

        /*new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                binding.txtTime.setText(String.valueOf(counter));
                counter--;
            }

            @Override
            public void onFinish() {
                binding.txtTime.setVisibility(View.GONE);
                binding.btnResend.setClickable(true);
            }
        }.start();*/




        binding.btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (new ConnectionToInternet(OtpVerificationActivity.this).isConnectingToInternet()) {
                    sendData();
                } else {
                    new ConnectionToInternet(OtpVerificationActivity.this).ShowDilog(OtpVerificationActivity.this);
                }
            }
        });

        binding.btnResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (new ConnectionToInternet(OtpVerificationActivity.this).isConnectingToInternet()) {

                    reverseTimer(counter,binding.txtTime,binding.btnResend);
                    resendotp();
                } else {
                    new ConnectionToInternet(OtpVerificationActivity.this).ShowDilog(OtpVerificationActivity.this);
                }
            }
        });


    }

    public void reverseTimer(int Seconds, final TextView tv,final TextView tv2) {

        new CountDownTimer(Seconds * 1000 + 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);

                tv.setVisibility(View.VISIBLE);
                tv2.setVisibility(View.GONE);
                int hours = seconds / (60 * 60);
                int tempMint = (seconds - (hours * 60 * 60));
                int minutes = tempMint / 60;
                seconds = tempMint - (minutes * 60);

                tv.setText(String.format("%02d", minutes)
                        + ":" + String.format("%02d", seconds )+ " resend OTP after");
            }

            public void onFinish() {
                tv.setVisibility(View.GONE);
                tv2.setVisibility(View.VISIBLE);
            }
        }.start();
    }

    private void resendotp() {

        pd = new ProgressDialog(OtpVerificationActivity.this);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();

        Api api = RetrofitClient.getClient().create(Api.class);
        Call<ResponseOtpSend> call = api.sendOtp("Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
                WS_URL_PARAMS.access_key, strUsername, otp);
        call.enqueue(new Callback<ResponseOtpSend>() {
            @Override
            public void onResponse(@NonNull Call<ResponseOtpSend> call, @NonNull Response<ResponseOtpSend> response) {

                Log.e("response_otpsend", new Gson().toJson(response.body()));

                pd.dismiss();
                if (response.body() != null) {

                    if (!response.body().getError()) {

                        /*Intent i = new Intent(VerificationActivity.this, VerificationActivity.class);
                        i.putExtra("username", strUsername);
                        i.putExtra("msg", response.body().getMessage());
                        i.putExtra("otp", otp);
                        startActivity(i);*/

                        Toast.makeText(OtpVerificationActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(OtpVerificationActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();


                    }

                }

            }

            @Override
            public void onFailure(@NonNull Call<ResponseOtpSend> call, @NonNull Throwable t) {
                pd.dismiss();
                t.printStackTrace();
            }
        });


    }

    private void sendData() {

        pd = new ProgressDialog(OtpVerificationActivity.this);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();


            strOtp = binding.edtOne.getText().toString() + binding.edtTwo.getText().toString() + binding.edtThree.getText().toString() + binding.edtFour.getText().toString();

            if (!strOtp.equals(otp)) {

                //binding.editTextCode.setError("Please Enter Valid OTP");
                Toast.makeText(OtpVerificationActivity.this, "Please Enter Valid OTP", Toast.LENGTH_SHORT).show();
                pd.dismiss();

            } else if (strOtp.equals(getIntent().getStringExtra("otp"))) {

//                sendUserData(strUsername);

                sessionManager.setLogin(true);
                sessionManager.setUserContact(strUsername);

                Intent intent =new Intent(OtpVerificationActivity.this, CategoryActivity.class);
                intent.putExtra("flag", 1);
                finish();
                startActivity(intent);
                pd.dismiss();
            }


    }



    private void sendProfileData(String userId) {

//        pd = new ProgressDialog(OtpVerificationActivity.this);
//        pd.setMessage("Loading...");
//        pd.setCancelable(false);
//        pd.show();

        Api api = RetrofitClient.getClient().create(Api.class);
        Call<ResponseSendProfile> call = api.sendProfile("Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
                WS_URL_PARAMS.access_key,userId,"","","1",strEmail,"",
                "", strUsername,"","","","",strPassword);
        call.enqueue(new Callback<ResponseSendProfile>() {
            @Override
            public void onResponse(Call<ResponseSendProfile> call, Response<ResponseSendProfile> response) {

                Log.e("responseSendProfile",new Gson().toJson(response.body()));
                if (pd.isShowing()){
                    pd.dismiss();
                }
                if (response.body() != null && response.isSuccessful()){

                    if (!response.body().getError()){
                        sessionManager.setLogin(true);

                        Intent i = new Intent(OtpVerificationActivity.this, AllSetActivity.class);
                        finish();
                        startActivity(i);

                    }else {
                        Toast.makeText(OtpVerificationActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(OtpVerificationActivity.this, getResources().getString(R.string.error_admin), Toast.LENGTH_SHORT).show();

                }



            }

            @Override
            public void onFailure(Call<ResponseSendProfile> call, Throwable t) {
                if (pd.isShowing()){
                    pd.dismiss();
                }
                t.printStackTrace();
                Log.e("errorSendProfile", t.getMessage());
                sessionManager.setLogin(false);
            }
        });







    }


    private class GenericTextWatcher implements TextWatcher {
        private EditText currentView;
        private EditText nextView;

        private GenericTextWatcher(EditText currentView, EditText nextView) {
            this.currentView = currentView;
            this.nextView = nextView;

        }

        @Override
        public void afterTextChanged(Editable editable) {
            // TODO Auto-generated method stub
            String text = editable.toString();
            if (nextView != null && text.length() == 1) {
                nextView.requestFocus();

            }
            if (text.length() > 1) {
                currentView.setText(String.valueOf(text.charAt(text.length() - 1)));
                currentView.setSelection(1);
            }

            /*if (text.length() > 0) {
                currentView.setBackground(getResources().getDrawable(R.drawable.four_dp_corner_box_green));
            } else {
                currentView.setBackground(getResources().getDrawable(R.drawable.four_dp_corner_box_two_dp_thik));

            }*/

        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }
    }

    private static class GenericKeyEvent implements View.OnKeyListener {

        private EditText currentView;
        private EditText previousView;

        public GenericKeyEvent(EditText currentView, EditText previousView) {
            this.currentView = currentView;
            this.previousView = previousView;
        }

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_DEL && currentView.getText().toString().isEmpty()) {
                if (previousView != null) {
                    previousView.requestFocus();
                }
                return true;
            }
            return false;
        }
    }

    private class GenericTextWatcher2 implements TextWatcher {

        private View view;
        EditText etNext, etPrev;
        EditText currentView;
        boolean flag=false;


        public GenericTextWatcher2(EditText currentView, EditText etNext, EditText etPrev, boolean flag) {
            this.etPrev = etPrev;
            this.etNext = etNext;
            this.currentView = currentView;
            this.flag=flag;

        }


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {


        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            String text = s.toString();
            if (text.length() == 1) {
                etNext.requestFocus();
            } else {
                etPrev.requestFocus();
            }


            if (flag){
                closeKeyboard();
                binding.btnVerify.setTextColor(getResources().getColor(R.color.clr_f8f8f8));
                binding.btnVerify.setBackground(getResources().getDrawable(R.drawable.button));

            }else {

                binding.btnVerify.setTextColor(getResources().getColor(R.color.cle_979592));
                binding.btnVerify.setBackground(getResources().getDrawable(R.drawable.button_disable));


            }

            /*if (text.length() > 0) {
                currentView.setBackground(getResources().getDrawable(R.drawable.four_dp_corner_box_green));
            } else {
                currentView.setBackground(getResources().getDrawable(R.drawable.four_dp_corner_box_two_dp_thik));

            }*/


        }


    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


}