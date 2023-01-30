package com.direct2web.citysip.Activities.Insurance;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.direct2web.citysip.Activities.Doctor.DoctorAboutBusinessActivity;
import com.direct2web.citysip.Model.RestaurentModels.Profile.ResponseSendProfile;
import com.direct2web.citysip.R;
import com.direct2web.citysip.Utils.Api;
import com.direct2web.citysip.Utils.ConnectionToInternet;
import com.direct2web.citysip.Utils.RetrofitClient;
import com.direct2web.citysip.Utils.SessionManager;
import com.direct2web.citysip.Utils.WS_URL_PARAMS;
import com.direct2web.citysip.databinding.ActivityDoctorEditAboutYouBinding;
import com.direct2web.citysip.databinding.ActivityEditInsuranceAboutYouBinding;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InsurenceEditAboutYouActivity extends AppCompatActivity {

    ActivityEditInsuranceAboutYouBinding binding;
    SessionManager sessionManager;
    ProgressDialog pd;
    Calendar calendar = Calendar.getInstance();
    DatePickerDialog picker;
    DatePickerDialog.OnDateSetListener date;

    private static int flag_calander = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_insurance_about_you);
        sessionManager = new SessionManager(this);


        binding.edtRestName.setText(getIntent().getStringExtra("userName"));
        binding.edtRestNumber.setText(getIntent().getStringExtra("contect"));
        binding.edtRestWebsite.setText(getIntent().getStringExtra("dob"));
        binding.edtRestDescription.setText(getIntent().getStringExtra("nationality"));
        binding.edtRestIntro.setText(getIntent().getStringExtra("intro"));
        binding.btnVerify.setText("Edit");


         date = (view, year, monthOfYear, dayOfMonth) -> {
             // TODO Auto-generated method stub
             calendar.set(Calendar.YEAR, year);
             calendar.set(Calendar.MONTH, monthOfYear);
             calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);


             updateLabel();

         };

        binding.edtRestWebsite.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar calendar1 = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(InsurenceEditAboutYouActivity.this, date, calendar1
                        .get(Calendar.YEAR), calendar1.get(Calendar.MONTH),
                        calendar1.get(Calendar.DAY_OF_MONTH)) {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        super.onClick(dialog, which);
                        if (which == dialog.BUTTON_NEGATIVE) {
                            binding.edtRestWebsite.setText("Select Date");
                        }
                    }

                };
                datePickerDialog.show();
                flag_calander = 1;
            }
        });


        binding.btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (binding.edtRestName.getText().toString().equals("")) {

                    binding.edtRestName.setError("Field can not be empty");

                } else if (binding.edtRestNumber.getText().toString().equals("") && binding.edtRestNumber.getText().length() == 10) {

                    binding.edtRestNumber.setError("Peese enter valid phoneNo.");

                } else if (binding.edtRestWebsite.getText().toString().equals("")) {

                    binding.edtRestWebsite.setError("Field can not be empty");

                } else if (binding.edtRestDescription.getText().toString().equals("")) {

                    binding.edtRestDescription.setError("Field can not be empty");

                } else {

                    if (new ConnectionToInternet(InsurenceEditAboutYouActivity.this).isConnectingToInternet()) {
                        editProfile(sessionManager.getUserId());
                    } else {
                        new ConnectionToInternet(InsurenceEditAboutYouActivity.this).ShowDilog(InsurenceEditAboutYouActivity.this);
                    }

                }

            }
        });
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        if (flag_calander == 1) {
            binding.edtRestWebsite.setText(sdf.format(calendar.getTime()));
        }


    }

    private void editProfile(String userId) {


        pd = new ProgressDialog(InsurenceEditAboutYouActivity.this);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();

        String name = binding.edtRestName.getText().toString();
        String number = binding.edtRestNumber.getText().toString();
        String dateOfBirth = binding.edtRestWebsite.getText().toString();
        String nationality = binding.edtRestDescription.getText().toString();
        String intro = binding.edtRestIntro.getText().toString();

        Api api = RetrofitClient.getClient().create(Api.class);
        Call<ResponseSendProfile> call = api.editInsurenceAboutyou("Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
                WS_URL_PARAMS.access_key,userId,name,number,dateOfBirth,nationality,intro);


        call.enqueue(new Callback<ResponseSendProfile>() {
            @Override
            public void onResponse(Call<ResponseSendProfile> call, Response<ResponseSendProfile> response) {

                Log.e("responseEditProfile",new Gson().toJson(response.body()));
                pd.dismiss();
                if (response.body() != null && response.isSuccessful()){

                    if (!response.body().getError()){

                        Intent i = new Intent(InsurenceEditAboutYouActivity.this, InsuranceAboutBusinessActivity.class);
                        finish();
                        startActivity(i);

                    }else {
                        Toast.makeText(InsurenceEditAboutYouActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(InsurenceEditAboutYouActivity.this, getResources().getString(R.string.error_admin), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseSendProfile> call, Throwable t) {
                pd.dismiss();
                t.printStackTrace();
                Log.e("errorEditProfile", t.getMessage());
            }
        });
    }
}