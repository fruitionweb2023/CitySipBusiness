package com.direct2web.citysip.Activities.Restaurent;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.direct2web.citysip.Model.DoctorModels.DoctorServices.ResponseEditDoctorService;
import com.direct2web.citysip.Model.RestaurentModels.offer.ResponseAddOffer;
import com.direct2web.citysip.R;
import com.direct2web.citysip.Utils.Api;
import com.direct2web.citysip.Utils.RetrofitClient;
import com.direct2web.citysip.Utils.SessionManager;
import com.direct2web.citysip.Utils.WS_URL_PARAMS;
import com.direct2web.citysip.databinding.ActivitySetUpAddCouponsBinding;
import com.google.gson.Gson;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetUpAddCouponsActivity extends AppCompatActivity {

    ActivitySetUpAddCouponsBinding binding;
    SessionManager sessionManager;
    ProgressDialog pd;
    String strOffersId;
    String offerId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_set_up_add_coupons);
        sessionManager = new SessionManager(this);

        pd = new ProgressDialog(SetUpAddCouponsActivity.this);
        pd.setMessage("Please Wait....");
        pd.setCancelable(true);

        offerId = getIntent().getStringExtra("offerId");

        if (getIntent().getStringExtra("flag").equals("1")) {
            binding.txtTitleCoupons.setText("Edit Coupons");
            binding.edtCouponOffers.setText(getIntent().getStringExtra("precentage"));
            binding.edtCouponsCode.setText(getIntent().getStringExtra("couponCode"));
            binding.edtMinPurchase.setText(getIntent().getStringExtra("minAmount"));
            binding.edtMaxDiscount.setText(getIntent().getStringExtra("maxAmount"));
            binding.edtTermsConditions.setText(getIntent().getStringExtra("termsAndCondition"));
            binding.btnAddCoupons.setText("Edit");

            offerId = getIntent().getStringExtra("offerId");
        } else  {
            binding.txtTitleCoupons.setText("Add Coupons");
            binding.edtCouponOffers.setText("");
            binding.edtCouponsCode.setText("");
            binding.edtMinPurchase.setText("");
            binding.edtMaxDiscount.setText("");
            binding.edtTermsConditions.setText("");
            binding.btnAddCoupons.setText("Add");

        }

        binding.btnAddCoupons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (binding.edtCouponOffers.getText().toString().equals("percentage")) {

                    Toast.makeText(getApplicationContext(), "Enter Coupons offer ", Toast.LENGTH_SHORT).show();

                } else if (binding.edtCouponsCode.getText().toString().equals("coupn_code")) {

                    Toast.makeText(getApplicationContext(), "Enter Coupons Code", Toast.LENGTH_SHORT).show();

                } else if (binding.edtMinPurchase.getText().toString().equals("min_amount")) {

                    Toast.makeText(getApplicationContext(), "Enter Minimum Purchase", Toast.LENGTH_SHORT).show();

                } else if (binding.edtMaxDiscount.getText().toString().equals("max_amount")) {

                    Toast.makeText(getApplicationContext(), "Enter Maximum Discount", Toast.LENGTH_SHORT).show();

                } else if (binding.edtTermsConditions.getText().toString().equals("terms_condition")) {

                    Toast.makeText(getApplicationContext(), "Enter Terms and Conditions", Toast.LENGTH_SHORT).show();

                } else {
                    if (getIntent().getStringExtra("flag").equals("1")) {
                        editSetUpOffer(sessionManager.getUserId());

                    } else {

                        sendSetUpOffer(sessionManager.getUserId());
                    }
                }
            }
        });
    }

    private void sendSetUpOffer(String userId) {

        pd = new ProgressDialog(SetUpAddCouponsActivity.this);
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

        RequestBody t1 = RequestBody.create(MediaType.parse("multipart/form-data"), authHeader);
        RequestBody t2 = RequestBody.create(MediaType.parse("multipart/form-data"), accesskey);
        RequestBody t3 = RequestBody.create(MediaType.parse("multipart/form-data"), coupnCode);
        RequestBody t4 = RequestBody.create(MediaType.parse("multipart/form-data"), maxAmount);
        RequestBody t5 = RequestBody.create(MediaType.parse("multipart/form-data"), termsCondition);
        RequestBody t6 = RequestBody.create(MediaType.parse("multipart/form-data"), minAmount);
        RequestBody t7 = RequestBody.create(MediaType.parse("multipart/form-data"), percentage);
        RequestBody t8 = RequestBody.create(MediaType.parse("multipart/form-data"), userId);

        Log.e("Coupon Add  Data : ", "precentage" +percentage +
                "\ncouponCode : " + coupnCode +
                "\nmaxAmount : " + maxAmount +
                "\nminAmount : " + minAmount +
                "\ntermsAndCondition : " + termsCondition);

        Api api = RetrofitClient.getClient().create(Api.class);
        Call<ResponseAddOffer> call = api.sendSetUpOffers(authHeader,t2,t8,t3,t6,t4,t5,t7);
        call.enqueue(new Callback<ResponseAddOffer>() {
            @Override
            public void onResponse(Call<ResponseAddOffer> call, Response<ResponseAddOffer> response) {
                Log.e("responseOfferList", new Gson().toJson(response.body()));
                if (pd.isShowing()) {
                    pd.dismiss();
                }
                if (response.body() != null && response.isSuccessful()) {

                    if (response.body().getError()) {

                        Toast.makeText(SetUpAddCouponsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {

                        Intent intent = new Intent(SetUpAddCouponsActivity.this, SetUpCouponsActivity.class);
                        startActivity(intent);
                        Log.e("userId", sessionManager.getUserId());
                    }

                } else {
                    Toast.makeText(SetUpAddCouponsActivity.this, getResources().getString(R.string.error_admin), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseAddOffer> call, Throwable t) {
                if (pd.isShowing()) {
                    pd.dismiss();
                }
                t.printStackTrace();
                Log.e("error", t.getMessage());
            }
        });

    /*private void getdata(String userId) {
        Api api = RetrofitClient.getClient().create(Api.class);
        Call<ResponseOfferList> call = api.getOffers("Bearer " +  WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
                WS_URL_PARAMS.access_key,userId);
        call.enqueue(new Callback<ResponseOfferList>() {
            @Override
            public void onResponse(Call<ResponseOfferList> call, Response<ResponseOfferList> response) {
                Log.e("ResponseOfferList", new Gson().toJson(response.body()));
                if (response.body() != null && response.isSuccessful()) {
                    offerList = response.body().getOfferList();
                }
            }
            @Override
            public void onFailure(Call<ResponseOfferList> call, Throwable t) {
                t.printStackTrace();
                Log.e("error", t.getMessage());
            }
        });
    }*/
    }

    private void editSetUpOffer(String userId) {

        pd = new ProgressDialog(SetUpAddCouponsActivity.this);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();

        String authHeader = "Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject);
        String accesskey = WS_URL_PARAMS.access_key;
      //  String businessId = sessionManager.getUserId();
        String coupnCode = binding.edtCouponsCode.getText().toString();
        String minAmount = binding.edtMinPurchase.getText().toString();
        String maxAmount = binding.edtMaxDiscount.getText().toString();
        String termsCondition = binding.edtTermsConditions.getText().toString();
        String percentage = binding.edtCouponOffers.getText().toString();
        String id = offerId;

        RequestBody t1 = RequestBody.create(MediaType.parse("multipart/form-data"), authHeader);
        RequestBody t2 = RequestBody.create(MediaType.parse("multipart/form-data"), accesskey);
        RequestBody t3 = RequestBody.create(MediaType.parse("multipart/form-data"), userId);
        RequestBody t4 = RequestBody.create(MediaType.parse("multipart/form-data"), coupnCode);
        RequestBody t5 = RequestBody.create(MediaType.parse("multipart/form-data"), minAmount);
        RequestBody t6 = RequestBody.create(MediaType.parse("multipart/form-data"), maxAmount);
        RequestBody t7 = RequestBody.create(MediaType.parse("multipart/form-data"), termsCondition);
        RequestBody t8 = RequestBody.create(MediaType.parse("multipart/form-data"), percentage);
        RequestBody t9 = RequestBody.create(MediaType.parse("multipart/form-data"), id);

        Log.e("CouponGetAndSendData : ",
                "businessId : " + userId +
                "\nprecentage" + percentage +
                "\ncouponCode : " +  coupnCode +
                "\nmaxAmount : " + maxAmount +
                "\nminAmount : " + minAmount +
                "\ntermsAndCondition : " + termsCondition +
                "\nofferId : " +   id);


        Api api = RetrofitClient.getClient().create(Api.class);
        Call<ResponseEditDoctorService> call = api.editSetUpOffers(authHeader,t2,t3,t4,t5,t6,t7,t8,t9);
        call.enqueue(new Callback<ResponseEditDoctorService>() {
            @Override
            public void onResponse(Call<ResponseEditDoctorService> call, Response<ResponseEditDoctorService> response) {
                Log.e("ResponseEditOfferList", new Gson().toJson(response.body()));
                if (pd.isShowing()) {
                    pd.dismiss();
                }
                if (response.body() != null && response.isSuccessful()) {

                    if (response.body().getError()) {

                        Toast.makeText(SetUpAddCouponsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {

                        Intent intent = new Intent(SetUpAddCouponsActivity.this, SetUpCouponsActivity.class);
                        startActivity(intent);
                        Log.e("userId", sessionManager.getUserId());
                    }

                } else {
                    Toast.makeText(SetUpAddCouponsActivity.this, getResources().getString(R.string.error_admin), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseEditDoctorService> call, Throwable t) {
                if (pd.isShowing()) {
                    pd.dismiss();
                }
                t.printStackTrace();
                Log.e("error", t.getMessage());
            }
        });

    /*private void getdata(String userId) {
        Api api = RetrofitClient.getClient().create(Api.class);
        Call<ResponseOfferList> call = api.getOffers("Bearer " +  WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
                WS_URL_PARAMS.access_key,userId);
        call.enqueue(new Callback<ResponseOfferList>() {
            @Override
            public void onResponse(Call<ResponseOfferList> call, Response<ResponseOfferList> response) {
                Log.e("ResponseOfferList", new Gson().toJson(response.body()));
                if (response.body() != null && response.isSuccessful()) {
                    offerList = response.body().getOfferList();
                }
            }
            @Override
            public void onFailure(Call<ResponseOfferList> call, Throwable t) {
                t.printStackTrace();
                Log.e("error", t.getMessage());
            }
        });
    }*/
    }
}