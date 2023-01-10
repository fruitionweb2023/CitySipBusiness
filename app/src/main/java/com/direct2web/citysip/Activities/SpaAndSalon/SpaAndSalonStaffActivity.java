package com.direct2web.citysip.Activities.SpaAndSalon;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.direct2web.citysip.Adapter.SpaAndSalonAdapter.ServicesListSpaAndSalonAdapter;
import com.direct2web.citysip.Adapter.SpaAndSalonAdapter.StaffListSpaAndSalonAdapter;
import com.direct2web.citysip.Model.SpaAndSalon.Services.ResponseSpaAndSalonServices;
import com.direct2web.citysip.Model.SpaAndSalon.Services.Service;
import com.direct2web.citysip.Model.SpaAndSalon.Staff.ResponseStaffList;
import com.direct2web.citysip.Model.SpaAndSalon.Staff.Staff;
import com.direct2web.citysip.R;
import com.direct2web.citysip.Utils.Api;
import com.direct2web.citysip.Utils.BottomButtonClickListner;
import com.direct2web.citysip.Utils.RetrofitClient;
import com.direct2web.citysip.Utils.SessionManager;
import com.direct2web.citysip.Utils.WS_URL_PARAMS;
import com.direct2web.citysip.databinding.ActivitySpaSalonServicesBinding;
import com.direct2web.citysip.databinding.ActivitySpaSalonStaffBinding;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpaAndSalonStaffActivity extends AppCompatActivity {

    ActivitySpaSalonStaffBinding binding;
    SessionManager sessionManager;
    ProgressDialog pd;
    BottomButtonClickListner bottomButtonClickListner;
    List<Staff> serviceList;
    StaffListSpaAndSalonAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_spa_salon_staff);
        sessionManager = new SessionManager(this);

        bottomButtonClickListner = new BottomButtonClickListner(this, sessionManager);

        binding.bottomnavigation.bbImgMyBusiness.setColorFilter(getResources().getColor(R.color.clr_spa_salon));

        binding.bottomnavigation.bbHome.setOnClickListener(new BottomButtonClickListner(this, sessionManager));
        binding.bottomnavigation.bbMyBusiness.setOnClickListener(new BottomButtonClickListner(this, sessionManager));
        binding.bottomnavigation.bbOrder.setOnClickListener(new BottomButtonClickListner(this, sessionManager));
        binding.bottomnavigation.bbMenu.setOnClickListener(new BottomButtonClickListner(this, sessionManager));


        pd = new ProgressDialog(SpaAndSalonStaffActivity.this);
        pd.setMessage("Please Wait....");
        pd.setCancelable(true);
        pd.show();

        getServiceList(sessionManager.getUserId());

        binding.addCoupons.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SpaAndSalonStaffActivity.this, SpaAndSalonAddStaffActivity.class);
                startActivity(intent);
            }
        });

    }

    private void getServiceList(String userId) {
        Api api = RetrofitClient.getClient().create(Api.class);

        Call<ResponseStaffList> call = api.getSpaAndSalonStaffList("Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
                WS_URL_PARAMS.access_key,userId);
        call.enqueue(new Callback<ResponseStaffList>() {
            @Override
            public void onResponse(Call<ResponseStaffList> call, Response<ResponseStaffList> response) {
                Log.e("responseServiceList", new Gson().toJson(response.body()));

                if (pd.isShowing()) {
                    pd.dismiss();
                }

                if (response.body() != null && response.isSuccessful()) {
                    Log.e("userId", sessionManager.getUserId());

                    if (response.body().getError()) {

                        Toast.makeText(SpaAndSalonStaffActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {

                        serviceList = response.body().getStaffList();

                        adapter = new StaffListSpaAndSalonAdapter(serviceList, SpaAndSalonStaffActivity.this);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SpaAndSalonStaffActivity.this);
                        binding.rcleAddServices.setLayoutManager(linearLayoutManager);
                        binding.rcleAddServices.setAdapter(adapter);
                    }

                }else {
                    Toast.makeText(SpaAndSalonStaffActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseStaffList> call, Throwable t) {

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
        Intent i = new Intent(SpaAndSalonStaffActivity.this, SapAndSalonDeshboardActivity.class);
        finish();
        startActivity(i);
    }
}