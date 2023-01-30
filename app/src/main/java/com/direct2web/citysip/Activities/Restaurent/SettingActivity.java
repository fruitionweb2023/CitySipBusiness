package com.direct2web.citysip.Activities.Restaurent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.direct2web.citysip.Activities.Doctor.DoctorAddCouponsActivity;
import com.direct2web.citysip.Activities.Doctor.DoctorCouponsActivity;
import com.direct2web.citysip.Activities.Doctor.DoctorDeshboardActivity;
import com.direct2web.citysip.Adapter.DoctorAdapters.CouponListDoctorAdapter;
import com.direct2web.citysip.Adapter.SettingsAdapter;
import com.direct2web.citysip.Model.DoctorModels.DoctorCouponsOffers.Offer;
import com.direct2web.citysip.Model.DoctorModels.DoctorCouponsOffers.ResponseDoctorCoupons;
import com.direct2web.citysip.Model.NotificationSetting;
import com.direct2web.citysip.Model.ResponseSettings;
import com.direct2web.citysip.R;
import com.direct2web.citysip.Utils.Api;
import com.direct2web.citysip.Utils.BottomButtonClickListner;
import com.direct2web.citysip.Utils.RetrofitClient;
import com.direct2web.citysip.Utils.SessionManager;
import com.direct2web.citysip.Utils.WS_URL_PARAMS;
import com.direct2web.citysip.databinding.ActivitySettingBinding;
import com.google.gson.Gson;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingActivity extends AppCompatActivity {

    ActivitySettingBinding binding;
    SessionManager sessionManager;
    ProgressDialog pd;
    SettingsAdapter adapter;
    List<NotificationSetting> offerList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_setting);
        sessionManager = new SessionManager(this);

        if (Objects.equals(sessionManager.getBusinessType(), "1")) {
            binding.toolbarLogo.setBackgroundResource(R.drawable.ic_lawyer_logo);
        }else if (Objects.equals(sessionManager.getBusinessType(), "2")){
            binding.toolbarLogo.setBackgroundResource(R.drawable.spa_salon_logo);
        }else if (Objects.equals(sessionManager.getBusinessType(), "3")){
            binding.toolbarLogo.setBackgroundResource(R.drawable.ic_logo_insurance);
        }else if (Objects.equals(sessionManager.getBusinessType(), "4")){
            binding.toolbarLogo.setBackgroundResource(R.drawable.ic_doctor_logo);
        }else if (Objects.equals(sessionManager.getBusinessType(), "5")){
            binding.toolbarLogo.setBackgroundResource(R.drawable.ic_lawyer_logo);
        }else if (Objects.equals(sessionManager.getBusinessType(), "6")){
            binding.toolbarLogo.setBackgroundResource(R.drawable.restaurant_logo_new);
        }

        pd = new ProgressDialog(SettingActivity.this);
        pd.setMessage("Please Wait....");
        pd.setCancelable(true);
        pd.show();
        getOfferList(sessionManager.getUserId(),"2");

    }

    private void getOfferList(String id,String catId) {

        Api api = RetrofitClient.getClient().create(Api.class);
        Log.e("Settings : ", "businessId : " + id + "\nCatId : " + catId);
        Call<ResponseSettings> call = api.getSettings("Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
                WS_URL_PARAMS.access_key,id,catId);
        call.enqueue(new Callback<ResponseSettings>() {
            @Override
            public void onResponse(Call<ResponseSettings> call, Response<ResponseSettings> response) {
                Log.e("responseSettings", new Gson().toJson(response.body()));

                if (pd.isShowing()) {
                    pd.dismiss();
                }

                if (response.body() != null && response.isSuccessful()) {
                    offerList = response.body().getNotificationSetting();
                    Log.e("userId", sessionManager.getUserId());

                    if (response.body().getError()) {
                        Toast.makeText(SettingActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        adapter = new SettingsAdapter(offerList, SettingActivity.this);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                        binding.rclrNotification.setLayoutManager(layoutManager);
                        binding.rclrNotification.setAdapter(adapter);
                    }
                } else {
                    Toast.makeText(SettingActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseSettings> call, Throwable t) {

                if (pd.isShowing()) {
                    pd.dismiss();
                }
                Log.e("erro", t.getMessage());
                t.printStackTrace();
            }
        });
    }
}