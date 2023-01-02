package com.direct2web.citysip.Activities.Lawyer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.direct2web.citysip.Activities.Doctor.DoctorAddServicesActivity;
import com.direct2web.citysip.Adapter.LawyerAdapters.ServicesListAdapterLawyer;
import com.direct2web.citysip.Model.LawyerModels.LawyerServices.ResponseLawyerServices;
import com.direct2web.citysip.Model.LawyerModels.LawyerServices.Service;
import com.direct2web.citysip.R;
import com.direct2web.citysip.Utils.Api;
import com.direct2web.citysip.Utils.BottomButtonClickListner;
import com.direct2web.citysip.Utils.RetrofitClient;
import com.direct2web.citysip.Utils.SessionManager;
import com.direct2web.citysip.Utils.WS_URL_PARAMS;
import com.direct2web.citysip.databinding.ActivityLawyerServicesBinding;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LawyerServicesActivity extends AppCompatActivity {

    ActivityLawyerServicesBinding binding;
    SessionManager sessionManager;
    ProgressDialog pd;
    BottomButtonClickListner bottomButtonClickListner;
    List<Service> serviceList;
    ServicesListAdapterLawyer adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_lawyer_services);
        sessionManager = new SessionManager(this);

        bottomButtonClickListner = new BottomButtonClickListner(this, sessionManager);

        binding.bottomnavigation.bbImgMyBusiness.setColorFilter(getResources().getColor(R.color.clr_0059C8));

        binding.bottomnavigation.bbOrder.setVisibility(View.GONE);
        binding.bottomnavigation.bbAppointment.setVisibility(View.VISIBLE);

        binding.bottomnavigation.bbHome.setOnClickListener(new BottomButtonClickListner(this, sessionManager));
        binding.bottomnavigation.bbMyBusiness.setOnClickListener(new BottomButtonClickListner(this, sessionManager));
        binding.bottomnavigation.bbAppointment.setOnClickListener(new BottomButtonClickListner(this, sessionManager));
        binding.bottomnavigation.bbMenu.setOnClickListener(new BottomButtonClickListner(this, sessionManager));


        pd = new ProgressDialog(LawyerServicesActivity.this);
        pd.setMessage("Please Wait....");
        pd.setCancelable(true);
        pd.show();

        getServiceList(sessionManager.getUserId());

        binding.addCoupons.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LawyerServicesActivity.this, LawyerAddServicesActivity.class);
                startActivity(intent);
            }
        });

    }

    private void getServiceList(String userId) {
        Api api = RetrofitClient.getClient().create(Api.class);

        Call<ResponseLawyerServices> call = api.getLawyerServices("Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
                WS_URL_PARAMS.access_key,userId);
        call.enqueue(new Callback<ResponseLawyerServices>() {
            @Override
            public void onResponse(Call<ResponseLawyerServices> call, Response<ResponseLawyerServices> response) {
                Log.e("responseServiceList", new Gson().toJson(response.body()));

                if (pd.isShowing()) {
                    pd.dismiss();
                }

                if (response.body() != null && response.isSuccessful()) {
                    Log.e("userId", sessionManager.getUserId());

                    if (response.body().getError()) {

                        Toast.makeText(LawyerServicesActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {

                        serviceList = response.body().getServiceList();

                        adapter = new ServicesListAdapterLawyer(serviceList, LawyerServicesActivity.this);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(LawyerServicesActivity.this);
                        binding.rcleAddServices.setLayoutManager(linearLayoutManager);
                        binding.rcleAddServices.setAdapter(adapter);
                    }

                }else {
                    Toast.makeText(LawyerServicesActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseLawyerServices> call, Throwable t) {

                if (pd.isShowing()) {
                    pd.dismiss();
                }
                Log.e("ServiceListError", t.getMessage());
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(LawyerServicesActivity.this, LawyerDashboardActivity.class);
        finish();
        startActivity(i);
    }
}