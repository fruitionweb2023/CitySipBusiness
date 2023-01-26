package com.direct2web.citysip.Activities.Insurance;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.direct2web.citysip.Activities.Doctor.DoctorAboutBusinessActivity;
import com.direct2web.citysip.Activities.Doctor.DoctorEditAboutHospitalActivity;
import com.direct2web.citysip.Activities.Doctor.DoctorEditAboutYouActivity;
import com.direct2web.citysip.Activities.Doctor.DoctorEditLocationFetchActivity;
import com.direct2web.citysip.Activities.Lawyer.LawyerMenuActivity;
import com.direct2web.citysip.Model.InsuranceModel.InsuranceBusinessDetails.ResponseInsuranceBusinessDetails;
import com.direct2web.citysip.Model.RestaurentModels.Delete.ResponseStatus;
import com.direct2web.citysip.R;
import com.direct2web.citysip.Utils.Api;
import com.direct2web.citysip.Utils.RetrofitClient;
import com.direct2web.citysip.Utils.SessionManager;
import com.direct2web.citysip.Utils.WS_URL_PARAMS;
import com.direct2web.citysip.databinding.ActivityInsuranceAboutBusinessBinding;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InsuranceAboutBusinessActivity extends AppCompatActivity {

    ActivityInsuranceAboutBusinessBinding binding;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_insurance_about_business);
        sessionManager = new SessionManager(this);

        binding.toolbar.toolbarBack.setVisibility(View.VISIBLE);


        binding.toolbar.toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

            }
        });

        getDoctorBusinessDetails(sessionManager.getUserId());

        binding.switchOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) {
                    binding.restauraneLayout.setAlpha(0.25f);
                    binding.locationLayout.setAlpha(0.25f);
                    binding.llAboutYou.setAlpha(0.25f);
                    binding.imgEdit.setClickable(false);
                    binding.edtAboutYou.setClickable(false);
                    binding.edtLocation.setClickable(false);
                    sendStatus(sessionManager.getUserId(), "business_detail", sessionManager.getUserId(), "0");

                } else {

                    binding.restauraneLayout.setAlpha(1.0f);
                    binding.locationLayout.setAlpha(1.0f);
                    binding.llAboutYou.setAlpha(1.0f);
                    binding.imgEdit.setClickable(true);
                    binding.edtAboutYou.setClickable(true);
                    binding.edtLocation.setClickable(true);
                    sendStatus(sessionManager.getUserId(), "business_detail", sessionManager.getUserId(), "1");

                }
            }
        });


    }

    private void getDoctorBusinessDetails(String userId) {

        Api api = RetrofitClient.getClient().create(Api.class);

        Call<ResponseInsuranceBusinessDetails> call = api.getInsuranceBusinessDetails("Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
                WS_URL_PARAMS.access_key, userId);
        call.enqueue(new Callback<ResponseInsuranceBusinessDetails>() {
            @Override
            public void onResponse(Call<ResponseInsuranceBusinessDetails> call, Response<ResponseInsuranceBusinessDetails> response) {
                Log.e("responseAboutRestaurant", new Gson().toJson(response.body()));

                if (response.body() != null && response.isSuccessful()) {

                    if (response.body().getError()) {

                        Toast.makeText(InsuranceAboutBusinessActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {
                        Log.e("userId", sessionManager.getUserId());

                        binding.txtAboutYouName.setText(response.body().getAboutYouName());
                        binding.txtContectNo.setText(response.body().getAboutYouMobile());
                        binding.txtBirthDate.setText(response.body().getAboutYouDOB());
                        binding.txtNationality.setText(response.body().getAboutYouNationality());
                        binding.txtPhoneNo.setText(response.body().getPhoneNo());
                        binding.txtWebsite.setText(response.body().getWebsite());
                        binding.txtDescription.setText(response.body().getDescription());
                        binding.txtBusignessName.setText(response.body().getBusinessName());
                        binding.txtBusignessName2.setText(response.body().getBusinessName());
                        String address = response.body().getAddressLine1() + "\n" + response.body().getAddressLine2();
                        binding.txtAddress.setText(address);

                        if (response.body().getStatus().equals("1")){

                            binding.switchOnOff.setChecked(true);
                            binding.restauraneLayout.setAlpha(1.0f);
                            binding.llAboutYou.setAlpha(1.0f);
                            binding.imgEdit.setClickable(true);
                            binding.edtAboutYou.setClickable(true);
                            binding.edtLocation.setClickable(true);
                            binding.locationLayout.setAlpha(1.0f);


                        }else {
                            binding.switchOnOff.setChecked(false);
                            binding.restauraneLayout.setAlpha(0.25f);
                            binding.llAboutYou.setAlpha(0.25f);
                            binding.imgEdit.setClickable(false);
                            binding.edtAboutYou.setClickable(false);
                            binding.edtLocation.setClickable(false);
                            binding.locationLayout.setAlpha(0.25f);


                        }

                        binding.edtLocation.setOnClickListener(v -> {

                            Intent i = new Intent(InsuranceAboutBusinessActivity.this, InsurenceEditLocationFetchActivity.class);
                            i.putExtra("lat",response.body().getLatitude());
                            i.putExtra("lang",response.body().getLongitude());
                            i.putExtra("address1",response.body().getAddressLine1());
                            i.putExtra("address2",response.body().getAddressLine2());
                            startActivity(i);
                        });

                        binding.imgEdit.setOnClickListener(v -> {

                            Intent i = new Intent(InsuranceAboutBusinessActivity.this, InsurenceEditAboutOfficeActivity.class);
                            i.putExtra("contect",response.body().getPhoneNo());
                            i.putExtra("website",response.body().getWebsite());
                            i.putExtra("about",response.body().getDescription());
                            i.putExtra("businessName",response.body().getBusinessName());
                            startActivity(i);

                        });

                        binding.edtAboutYou.setOnClickListener(v -> {
                            Intent i = new Intent(InsuranceAboutBusinessActivity.this, InsurenceEditAboutYouActivity.class);
                            i.putExtra("userName",response.body().getAboutYouName());
                            i.putExtra("contect",response.body().getAboutYouMobile());
                            i.putExtra("dob",response.body().getAboutYouDOB());
                            i.putExtra("intro",response.body().getAboutYouIntro());
                            i.putExtra("nationality",response.body().getAboutYouNationality());
                            startActivity(i);
                        });
                    }

                } else {
                    Toast.makeText(InsuranceAboutBusinessActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseInsuranceBusinessDetails> call, Throwable t) {

                Log.e("error", t.getMessage());
                t.printStackTrace();
            }
        });
    }

    public void sendStatus(String userId, String type, String id, String status) {

        Api api = RetrofitClient.getClient().create(Api.class);
        Call<ResponseStatus> call = api.sendInsuranceStatus("Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
                WS_URL_PARAMS.access_key, userId, type, id, status);
        call.enqueue(new Callback<ResponseStatus>() {
            @Override
            public void onResponse(Call<ResponseStatus> call, Response<ResponseStatus> response) {

                Log.e("responseDelete", new Gson().toJson(response.body()));


                if (response.body() != null && response.isSuccessful()) {

                    if (response.body().getError()) {

                        Toast.makeText(InsuranceAboutBusinessActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(InsuranceAboutBusinessActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<ResponseStatus> call, Throwable t) {

                t.printStackTrace();
                Log.e("errorDelete", t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(InsuranceAboutBusinessActivity.this, InsuranceMenuActivity.class);
        finish();
        startActivity(i);
    }
}