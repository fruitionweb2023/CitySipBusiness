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
import com.direct2web.citysip.Model.SpaAndSalon.Services.ResponseSpaAndSalonServices;
import com.direct2web.citysip.Model.SpaAndSalon.Services.Service;
import com.direct2web.citysip.R;
import com.direct2web.citysip.Utils.Api;
import com.direct2web.citysip.Utils.BottomButtonClickListner;
import com.direct2web.citysip.Utils.RetrofitClient;
import com.direct2web.citysip.Utils.SessionManager;
import com.direct2web.citysip.Utils.WS_URL_PARAMS;
import com.direct2web.citysip.databinding.ActivityDoctorServicesBinding;
import com.direct2web.citysip.databinding.ActivitySpaSalonServicesBinding;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpaAndSalonServicesActivity extends AppCompatActivity implements ServicesListSpaAndSalonAdapter.OnItemClickListner {

    ActivitySpaSalonServicesBinding binding;
    SessionManager sessionManager;
    ProgressDialog pd;
    BottomButtonClickListner bottomButtonClickListner;
    List<Service> serviceList;
    ServicesListSpaAndSalonAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_spa_salon_services);
        sessionManager = new SessionManager(this);

        bottomButtonClickListner = new BottomButtonClickListner(this, sessionManager);

        binding.bottomnavigation.bbImgMyBusiness.setColorFilter(getResources().getColor(R.color.clr_spa_salon));

        binding.bottomnavigation.bbHome.setOnClickListener(new BottomButtonClickListner(this, sessionManager));
        binding.bottomnavigation.bbMyBusiness.setOnClickListener(new BottomButtonClickListner(this, sessionManager));
        binding.bottomnavigation.bbOrder.setOnClickListener(new BottomButtonClickListner(this, sessionManager));
        binding.bottomnavigation.bbMenu.setOnClickListener(new BottomButtonClickListner(this, sessionManager));


        pd = new ProgressDialog(SpaAndSalonServicesActivity.this);
        pd.setMessage("Please Wait....");
        pd.setCancelable(true);
        pd.show();

        getServiceList(sessionManager.getUserId());

        binding.addCoupons.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SpaAndSalonServicesActivity.this, SpaAndSalonAddServicesActivity.class);
                startActivity(intent);
            }
        });

    }

    private void getServiceList(String userId) {
        Api api = RetrofitClient.getClient().create(Api.class);

        Call<ResponseSpaAndSalonServices> call = api.getSpaAndSalonServices("Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
                WS_URL_PARAMS.access_key,userId);
        call.enqueue(new Callback<ResponseSpaAndSalonServices>() {
            @Override
            public void onResponse(Call<ResponseSpaAndSalonServices> call, Response<ResponseSpaAndSalonServices> response) {
                Log.e("responseServiceList", new Gson().toJson(response.body()));

                if (pd.isShowing()) {
                    pd.dismiss();
                }

                if (response.body() != null && response.isSuccessful()) {
                    Log.e("userId", sessionManager.getUserId());

                    if (response.body().getError()) {

                        Toast.makeText(SpaAndSalonServicesActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {

                        serviceList = response.body().getServiceList();

                        adapter = new ServicesListSpaAndSalonAdapter(serviceList, SpaAndSalonServicesActivity.this,SpaAndSalonServicesActivity.this);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SpaAndSalonServicesActivity.this);
                        binding.rcleAddServices.setLayoutManager(linearLayoutManager);
                        binding.rcleAddServices.setAdapter(adapter);
                    }

                }else {
                    Toast.makeText(SpaAndSalonServicesActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseSpaAndSalonServices> call, Throwable t) {

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
        Intent i = new Intent(SpaAndSalonServicesActivity.this, SapAndSalonDeshboardActivity.class);
        finish();
        startActivity(i);
    }

    @Override
    public void onAddButtonClick(int postion) {
        Intent i = new Intent(SpaAndSalonServicesActivity.this, SpaAndSalonAddServicesActivity.class);
        i.putExtra("flag","1");
        i.putExtra("serviceName", serviceList.get(postion).getServiceName());
        i.putExtra("doctorName", serviceList.get(postion).getDoctorName());
        i.putExtra("amount", serviceList.get(postion).getAmount());
        i.putExtra("description", serviceList.get(postion).getDescription());
        i.putExtra("offer", serviceList.get(postion).getOffer());
       // i.putExtra("name", serviceList.get(postion).getName());
        i.putExtra("image", serviceList.get(postion).getImage());
        i.putExtra("serviceId", serviceList.get(postion).getId());
        finish();
        startActivity(i);
    }
}