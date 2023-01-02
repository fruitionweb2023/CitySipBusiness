package com.direct2web.citysip.Activities.Insurance;

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

import com.direct2web.citysip.Activities.Lawyer.LawyerLocationFetchActivity;
import com.direct2web.citysip.Model.InsuranceModel.InsuranceSaveProfile.ResponseInsuranceSaveProfile;
import com.direct2web.citysip.Model.LawyerModels.LawyerSaveProfile.ResponseLawyerSaveProfile;
import com.direct2web.citysip.R;
import com.direct2web.citysip.Utils.Api;
import com.direct2web.citysip.Utils.ConnectionToInternet;
import com.direct2web.citysip.Utils.RetrofitClient;
import com.direct2web.citysip.Utils.SessionManager;
import com.direct2web.citysip.Utils.WS_URL_PARAMS;
import com.direct2web.citysip.databinding.ActivityInsuranceAboutOfficeBinding;
import com.direct2web.citysip.databinding.ActivityLawyerAboutOfficeBinding;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InsuranceAboutOfficeActivity extends AppCompatActivity {

    ActivityInsuranceAboutOfficeBinding binding;
    SessionManager sessionManager;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_insurance_about_office);
        sessionManager = new SessionManager(this);

        binding.edtRestName.addTextChangedListener(tw);
        binding.edtRestNumber.addTextChangedListener(tw);
        binding.edtRestWebsite.addTextChangedListener(tw);
        binding.edtRestDescription.addTextChangedListener(tw);


        binding.txtSetUpLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(InsuranceAboutOfficeActivity.this, InsuranceLocationFetchActivity.class);
                startActivity(i);

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

                    if (new ConnectionToInternet(InsuranceAboutOfficeActivity.this).isConnectingToInternet()) {
                        sendProfile(sessionManager.getUserId());
                    } else {
                        new ConnectionToInternet(InsuranceAboutOfficeActivity.this).ShowDilog(InsuranceAboutOfficeActivity.this);
                    }

                }

            }
        });
    }


    private void sendProfile(String userId) {


        pd = new ProgressDialog(InsuranceAboutOfficeActivity.this);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();

        String name = binding.edtRestName.getText().toString();
        String number = binding.edtRestNumber.getText().toString();
        String website = binding.edtRestWebsite.getText().toString();
        String description = binding.edtRestDescription.getText().toString();

        Api api = RetrofitClient.getClient().create(Api.class);
        Call<ResponseInsuranceSaveProfile> call = api.sendInsuranceSaveProfile("Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
                WS_URL_PARAMS.access_key,userId,"","","1","",name,number,website,description,"","","");
        call.enqueue(new Callback<ResponseInsuranceSaveProfile>() {
            @Override
            public void onResponse(Call<ResponseInsuranceSaveProfile> call, Response<ResponseInsuranceSaveProfile> response) {

                Log.e("responseSendProfile",new Gson().toJson(response.body()));
                pd.dismiss();
                if (response.body() != null && response.isSuccessful()){

                    if (!response.body().getError()){

                        Intent i = new Intent(InsuranceAboutOfficeActivity.this, InsuranceLocationFetchActivity.class);
                        startActivity(i);

                    }else {
                        Toast.makeText(InsuranceAboutOfficeActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(InsuranceAboutOfficeActivity.this, getResources().getString(R.string.error_admin), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseInsuranceSaveProfile> call, Throwable t) {
                pd.dismiss();
                t.printStackTrace();
                Log.e("errorSendProfile", t.getMessage());
            }
        });
    }


    TextWatcher tw = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (binding.edtRestName.getText().length() > 0 && binding.edtRestNumber.getText().length() > 0 && binding.edtRestWebsite.getText().length() > 0 && binding.edtRestDescription.getText().length() > 0) {
                binding.btnVerify.setTextColor(getResources().getColor(R.color.clr_f8f8f8));
                binding.btnVerify.setBackground(getResources().getDrawable(R.drawable.button_insurance));

            } else {

                binding.btnVerify.setTextColor(getResources().getColor(R.color.cle_979592));
                binding.btnVerify.setBackground(getResources().getDrawable(R.drawable.button_disable));
            }
        }

        @Override
        public void afterTextChanged(Editable s) { }
    };
}