package com.direct2web.citysip.Activities.SpaAndSalon;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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
import com.direct2web.citysip.databinding.ActivityDoctorEditAboutHospitalBinding;
import com.direct2web.citysip.databinding.ActivitySpaSalonEditAboutSpaSalonBinding;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpaAndSalonEditAboutSpaAndSalonActivity extends AppCompatActivity {

    ActivitySpaSalonEditAboutSpaSalonBinding binding;
    SessionManager sessionManager;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_spa_salon_edit_about_spa_salon);
        sessionManager = new SessionManager(this);


        binding.edtRestName.setText(getIntent().getStringExtra("businessName"));
        binding.edtRestNumber.setText(getIntent().getStringExtra("contect"));
        binding.edtRestWebsite.setText(getIntent().getStringExtra("website"));
        binding.edtRestDescription.setText(getIntent().getStringExtra("about"));
        binding.btnVerify.setText("Edit");

        binding.btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (binding.edtRestName.getText().toString().equals("")) {

                    binding.edtRestName.setError("Field can not be empty");

                } else if (binding.edtRestNumber.getText().toString().equals("") && binding.edtRestNumber.getText().length() == 10) {

                    binding.edtRestNumber.setError("Please enter valid phoneNo.");

                } else if (binding.edtRestWebsite.getText().toString().equals("")) {

                    binding.edtRestWebsite.setError("Field can not be empty");

                } else if (binding.edtRestDescription.getText().toString().equals("")) {

                    binding.edtRestDescription.setError("Field can not be empty");

                } else {

                    if (new ConnectionToInternet(SpaAndSalonEditAboutSpaAndSalonActivity.this).isConnectingToInternet()) {
                        editProfile(sessionManager.getUserId());
                    } else {
                        new ConnectionToInternet(SpaAndSalonEditAboutSpaAndSalonActivity.this).ShowDilog(SpaAndSalonEditAboutSpaAndSalonActivity.this);
                    }
                }
            }
        });
    }

    private void editProfile(String userId) {


        pd = new ProgressDialog(SpaAndSalonEditAboutSpaAndSalonActivity.this);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();

        String name = binding.edtRestName.getText().toString();
        String number = binding.edtRestNumber.getText().toString();
        String website = binding.edtRestWebsite.getText().toString();
        String description = binding.edtRestDescription.getText().toString();

        Api api = RetrofitClient.getClient().create(Api.class);
        Call<ResponseSendProfile> call = api.editSpaAndSalonProfile("Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
                WS_URL_PARAMS.access_key,userId,"","","","",name,
                number,website,description,"","","");
        call.enqueue(new Callback<ResponseSendProfile>() {
            @Override
            public void onResponse(Call<ResponseSendProfile> call, Response<ResponseSendProfile> response) {

                Log.e("responseEditProfile",new Gson().toJson(response.body()));
                pd.dismiss();
                if (response.body() != null && response.isSuccessful()){

                    if (!response.body().getError()){

                        Intent i = new Intent(SpaAndSalonEditAboutSpaAndSalonActivity.this, SpaAndSalonAboutBusinessActivity.class);
                        finish();
                        startActivity(i);

                    }else {
                        Toast.makeText(SpaAndSalonEditAboutSpaAndSalonActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(SpaAndSalonEditAboutSpaAndSalonActivity.this, getResources().getString(R.string.error_admin), Toast.LENGTH_SHORT).show();

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