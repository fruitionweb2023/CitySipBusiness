package com.direct2web.citysip.Activities.Doctor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.direct2web.citysip.Activities.Restaurent.SetUpAddCouponsActivity;
import com.direct2web.citysip.Activities.Restaurent.SetUpCouponsActivity;
import com.direct2web.citysip.Model.DoctorModels.DoctorCouponsOffers.ResponseDoctorAddCoupons;
import com.direct2web.citysip.Model.RestaurentModels.offer.ResponseAddOffer;
import com.direct2web.citysip.R;
import com.direct2web.citysip.Utils.Api;
import com.direct2web.citysip.Utils.RetrofitClient;
import com.direct2web.citysip.Utils.SessionManager;
import com.direct2web.citysip.Utils.WS_URL_PARAMS;
import com.direct2web.citysip.databinding.ActivityDoctorAddCouponsBinding;
import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorAddCouponsActivity extends AppCompatActivity {

    ActivityDoctorAddCouponsBinding binding;
    SessionManager sessionManager;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_doctor_add_coupons);
        sessionManager = new SessionManager(this);

        pd = new ProgressDialog(DoctorAddCouponsActivity.this);
        pd.setMessage("Please Wait....");
        pd.setCancelable(true);

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

                    sendSetUpOffer(sessionManager.getUserId());
                }
            }
        });
    }


    private void sendSetUpOffer(String userId) {

        pd = new ProgressDialog(DoctorAddCouponsActivity.this);
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
        Call<ResponseDoctorAddCoupons> call = api.sendCouponsDetails(authHeader,accesskey,userId,coupnCode,minAmount,maxAmount,termsCondition,percentage);
        call.enqueue(new Callback<ResponseDoctorAddCoupons>() {
            @Override
            public void onResponse(Call<ResponseDoctorAddCoupons> call, Response<ResponseDoctorAddCoupons> response) {
                Log.e("responseOfferListDoctor", new Gson().toJson(response.body()));
                if (pd.isShowing()) {
                    pd.dismiss();
                }
                if (response.body() != null && response.isSuccessful()) {

                    if (response.body().getError()) {

                        Toast.makeText(DoctorAddCouponsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {

                        Intent intent = new Intent(DoctorAddCouponsActivity.this, DoctorCouponsActivity.class);
                        startActivity(intent);
                    }

                } else {
                    Toast.makeText(DoctorAddCouponsActivity.this, getResources().getString(R.string.error_admin), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseDoctorAddCoupons> call, Throwable t) {
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
        Intent i = new Intent(DoctorAddCouponsActivity.this, DoctorCouponsActivity.class);
        finish();
        startActivity(i);
    }
}