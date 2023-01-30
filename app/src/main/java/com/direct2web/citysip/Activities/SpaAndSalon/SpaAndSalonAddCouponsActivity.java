package com.direct2web.citysip.Activities.SpaAndSalon;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.direct2web.citysip.Model.SpaAndSalon.CouponsOffer.ResponseEditOffer;
import com.direct2web.citysip.Model.SpaAndSalon.CouponsOffer.ResponseSpaAndSalonAddCoupons;
import com.direct2web.citysip.R;
import com.direct2web.citysip.Utils.Api;
import com.direct2web.citysip.Utils.RetrofitClient;
import com.direct2web.citysip.Utils.SessionManager;
import com.direct2web.citysip.Utils.WS_URL_PARAMS;
import com.direct2web.citysip.databinding.ActivityDoctorAddCouponsBinding;
import com.direct2web.citysip.databinding.ActivitySpaSalonAddCouponsBinding;
import com.google.gson.Gson;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpaAndSalonAddCouponsActivity extends AppCompatActivity {

    ActivitySpaSalonAddCouponsBinding binding;
    SessionManager sessionManager;
    ProgressDialog pd;
    String couponId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_spa_salon_add_coupons);
        sessionManager = new SessionManager(this);

        pd = new ProgressDialog(SpaAndSalonAddCouponsActivity.this);
        pd.setMessage("Please Wait....");
        pd.setCancelable(true);

        couponId = getIntent().getStringExtra("couponId");

        if (Objects.equals(getIntent().getStringExtra("flag"), "1")) {
            binding.txtTitleCoupons.setText("Edit Coupon");
            binding.edtCouponsCode.setText(getIntent().getStringExtra("couponCode"));
            binding.edtMaxDiscount.setText(getIntent().getStringExtra("maxAmount"));
            binding.edtTermsConditions.setText(getIntent().getStringExtra("terms"));
            binding.edtMinPurchase.setText(getIntent().getStringExtra("minAmount"));
             binding.edtCouponOffers.setText(getIntent().getStringExtra("percentage"));
             binding.btnAddCoupons.setText("Edit");
             couponId = getIntent().getStringExtra("couponId");
        }

        binding.btnAddCoupons.setOnClickListener(view -> {

            if (binding.edtCouponOffers.getText().toString().equals("")) {

                binding.edtCouponOffers.setError("Field can not be empty");

            } else if (binding.edtCouponsCode.getText().toString().equals("")) {

                binding.edtCouponsCode.setError("Field can not be empty");

            } else if (binding.edtMinPurchase.getText().toString().equals("")) {

                binding.edtMinPurchase.setError("Field can not be empty");

            } else if (binding.edtMaxDiscount.getText().toString().equals("")) {

                binding.edtMaxDiscount.setError("Field can not be empty");

            } else if (binding.edtTermsConditions.getText().toString().equals("")) {

                binding.edtTermsConditions.setError("Field can not be empty");

            } else {

                if (Objects.equals(getIntent().getStringExtra("flag"), "1")) {
                    editSetUpOffer(sessionManager.getUserId(),couponId);
                } else {
                    sendSetUpOffer(sessionManager.getUserId());
                }

            }
        });
    }


    private void sendSetUpOffer(String userId) {

        pd = new ProgressDialog(SpaAndSalonAddCouponsActivity.this);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();

        String authHeader = "Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject);
        String accesskey = WS_URL_PARAMS.access_key;
        String coupnCode = binding.edtCouponsCode.getText().toString();
        String maxAmount = binding.edtMaxDiscount.getText().toString();
        String termsCondition = binding.edtTermsConditions.getText().toString();
        String minAmount = binding.edtMinPurchase.getText().toString();
        String percentage = binding.edtCouponOffers.getText().toString();

        
        Api api = RetrofitClient.getClient().create(Api.class);
        Call<ResponseSpaAndSalonAddCoupons> call = api.sendSpaAndSalonCouponsDetails(authHeader,accesskey,userId,coupnCode,minAmount,maxAmount,termsCondition,percentage);
        call.enqueue(new Callback<ResponseSpaAndSalonAddCoupons>() {
            @Override
            public void onResponse(Call<ResponseSpaAndSalonAddCoupons> call, Response<ResponseSpaAndSalonAddCoupons> response) {
                Log.e("responseOfferListDoctor", new Gson().toJson(response.body()));
                if (pd.isShowing()) {
                    pd.dismiss();
                }
                if (response.body() != null && response.isSuccessful()) {

                    if (response.body().getError()) {

                        Toast.makeText(SpaAndSalonAddCouponsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {

                        Intent intent = new Intent(SpaAndSalonAddCouponsActivity.this, SpaAndSalonCouponsActivity.class);
                        startActivity(intent);
                    }

                } else {
                    Toast.makeText(SpaAndSalonAddCouponsActivity.this, getResources().getString(R.string.error_admin), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseSpaAndSalonAddCoupons> call, Throwable t) {
                if (pd.isShowing()) {
                    pd.dismiss();
                }
                t.printStackTrace();
                Log.e("error", t.getMessage());
            }
        });
    }

    private void editSetUpOffer(String userId,String couponId) {

        pd = new ProgressDialog(SpaAndSalonAddCouponsActivity.this);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();

        String authHeader = "Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject);
        String accesskey = WS_URL_PARAMS.access_key;
        String coupnCode = binding.edtCouponsCode.getText().toString();
        String maxAmount = binding.edtMaxDiscount.getText().toString();
        String termsCondition = binding.edtTermsConditions.getText().toString();
        String minAmount = binding.edtMinPurchase.getText().toString();
        String percentage = binding.edtCouponOffers.getText().toString();


        Api api = RetrofitClient.getClient().create(Api.class);
        Call<ResponseEditOffer> call = api.editSpaAndSalonCouponsDetails(authHeader,accesskey,userId,coupnCode,minAmount,maxAmount,termsCondition,percentage,couponId);
        call.enqueue(new Callback<ResponseEditOffer>() {
            @Override
            public void onResponse(Call<ResponseEditOffer> call, Response<ResponseEditOffer> response) {
                Log.e("responseeditoffer", new Gson().toJson(response.body()));
                if (pd.isShowing()) {
                    pd.dismiss();
                }
                if (response.body() != null && response.isSuccessful()) {

                    if (response.body().getError()) {

                        Toast.makeText(SpaAndSalonAddCouponsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {

                        Intent intent = new Intent(SpaAndSalonAddCouponsActivity.this, SpaAndSalonCouponsActivity.class);
                        startActivity(intent);
                    }

                } else {
                    Toast.makeText(SpaAndSalonAddCouponsActivity.this, getResources().getString(R.string.error_admin), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseEditOffer> call, Throwable t) {
                if (pd.isShowing()) {
                    pd.dismiss();
                }
                t.printStackTrace();
                Log.e("error", t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(SpaAndSalonAddCouponsActivity.this, SpaAndSalonCouponsActivity.class);
        finish();
        startActivity(i);
    }
}