package com.direct2web.citysip.Activities.Lawyer;

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

import com.direct2web.citysip.Model.LawyerModels.LawyerSaveProfile.ResponseLawyerSaveProfile;
import com.direct2web.citysip.Model.RestaurentModels.Profile.ResponseSendProfile;
import com.direct2web.citysip.R;
import com.direct2web.citysip.Utils.Api;
import com.direct2web.citysip.Utils.ConnectionToInternet;
import com.direct2web.citysip.Utils.RetrofitClient;
import com.direct2web.citysip.Utils.SessionManager;
import com.direct2web.citysip.Utils.WS_URL_PARAMS;
import com.direct2web.citysip.databinding.ActivityLawyerAboutOfficeBinding;
import com.direct2web.citysip.databinding.ActivityLawyerEditAboutOfficeBinding;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LawyerEditAboutOfficeActivity extends AppCompatActivity {

    ActivityLawyerEditAboutOfficeBinding binding;
    SessionManager sessionManager;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_lawyer_edit_about_office);
        sessionManager = new SessionManager(this);


        binding.edtRestName.setText(getIntent().getStringExtra("businessName"));
        binding.edtRestNumber.setText(getIntent().getStringExtra("contect"));
        binding.edtRestWebsite.setText(getIntent().getStringExtra("website"));
        binding.edtRestDescription.setText(getIntent().getStringExtra("about"));
        binding.btnVerify.setText("Edit");

        binding.edtRestName.addTextChangedListener(tw);
        binding.edtRestNumber.addTextChangedListener(tw);
        binding.edtRestWebsite.addTextChangedListener(tw);
        binding.edtRestDescription.addTextChangedListener(tw);

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

                    if (new ConnectionToInternet(LawyerEditAboutOfficeActivity.this).isConnectingToInternet()) {
                        editProfile(sessionManager.getUserId());
                    } else {
                        new ConnectionToInternet(LawyerEditAboutOfficeActivity.this).ShowDilog(LawyerEditAboutOfficeActivity.this);
                    }

                }

            }
        });
    }
    private void editProfile(String userId) {


        pd = new ProgressDialog(LawyerEditAboutOfficeActivity.this);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();

        String name = binding.edtRestName.getText().toString();
        String number = binding.edtRestNumber.getText().toString();
        String website = binding.edtRestWebsite.getText().toString();
        String description = binding.edtRestDescription.getText().toString();

        Api api = RetrofitClient.getClient().create(Api.class);
        Call<ResponseSendProfile> call = api.editLawyerProfile("Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
                WS_URL_PARAMS.access_key,userId,"","","","",name,
                number,website,description,"","","");
        call.enqueue(new Callback<ResponseSendProfile>() {
            @Override
            public void onResponse(Call<ResponseSendProfile> call, Response<ResponseSendProfile> response) {

                Log.e("responseEditProfile",new Gson().toJson(response.body()));
                pd.dismiss();
                if (response.body() != null && response.isSuccessful()){

                    if (!response.body().getError()){

                        Intent i = new Intent(LawyerEditAboutOfficeActivity.this, LawyerAboutBusinessActivity.class);
                        finish();
                        startActivity(i);

                    }else {
                        Toast.makeText(LawyerEditAboutOfficeActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(LawyerEditAboutOfficeActivity.this, getResources().getString(R.string.error_admin), Toast.LENGTH_SHORT).show();

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


    TextWatcher tw = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (binding.edtRestName.getText().length() > 0 && binding.edtRestNumber.getText().length() > 0 && binding.edtRestWebsite.getText().length() > 0 && binding.edtRestDescription.getText().length() > 0) {
                binding.btnVerify.setTextColor(getResources().getColor(R.color.clr_f8f8f8));
                binding.btnVerify.setBackground(getResources().getDrawable(R.drawable.button_doctor));

            } else {

                binding.btnVerify.setTextColor(getResources().getColor(R.color.cle_979592));
                binding.btnVerify.setBackground(getResources().getDrawable(R.drawable.button_disable));
            }
        }

        @Override
        public void afterTextChanged(Editable s) { }
    };
}