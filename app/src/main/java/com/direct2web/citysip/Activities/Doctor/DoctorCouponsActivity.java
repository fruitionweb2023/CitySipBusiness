package com.direct2web.citysip.Activities.Doctor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.direct2web.citysip.Activities.Restaurent.SetUpAddCouponsActivity;
import com.direct2web.citysip.Activities.Restaurent.SetUpCouponsActivity;
import com.direct2web.citysip.Adapter.DoctorAdapters.CouponListDoctorAdapter;
import com.direct2web.citysip.Adapter.RestaurentAdapters.NewCouponsListItemAdapter;
import com.direct2web.citysip.Model.DoctorModels.DoctorCouponsOffers.Offer;
import com.direct2web.citysip.Model.DoctorModels.DoctorCouponsOffers.ResponseDoctorCoupons;
import com.direct2web.citysip.Model.RestaurentModels.offer.ResponseOfferList;
import com.direct2web.citysip.R;
import com.direct2web.citysip.Utils.Api;
import com.direct2web.citysip.Utils.BottomButtonClickListner;
import com.direct2web.citysip.Utils.RetrofitClient;
import com.direct2web.citysip.Utils.SessionManager;
import com.direct2web.citysip.Utils.WS_URL_PARAMS;
import com.direct2web.citysip.databinding.ActivityDoctorCouponsBinding;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorCouponsActivity extends AppCompatActivity {

    ActivityDoctorCouponsBinding binding;
    SessionManager sessionManager;
    ProgressDialog pd;
    BottomButtonClickListner bottomButtonClickListner;
    CouponListDoctorAdapter adapter;
    List<Offer> offerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_doctor_coupons);
        sessionManager = new SessionManager(this);

        bottomButtonClickListner = new BottomButtonClickListner(this, sessionManager);

        binding.bottomnavigation.bbImgOrder.setColorFilter(getResources().getColor(R.color.clr_0059C8));

        binding.bottomnavigation.bbHome.setOnClickListener(new BottomButtonClickListner(this, sessionManager));
        binding.bottomnavigation.bbMyBusiness.setOnClickListener(new BottomButtonClickListner(this, sessionManager));
        binding.bottomnavigation.bbOrder.setOnClickListener(new BottomButtonClickListner(this, sessionManager));
        binding.bottomnavigation.bbMenu.setOnClickListener(new BottomButtonClickListner(this, sessionManager));



        pd = new ProgressDialog(DoctorCouponsActivity.this);
        pd.setMessage("Please Wait....");
        pd.setCancelable(true);
        pd.show();
        getOfferList(sessionManager.getUserId());

        binding.addCoupons.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DoctorCouponsActivity.this, DoctorAddCouponsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getOfferList(String id) {

        Api api = RetrofitClient.getClient().create(Api.class);

        Call<ResponseDoctorCoupons> call = api.getCouponsList("Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
                WS_URL_PARAMS.access_key, id);
        call.enqueue(new Callback<ResponseDoctorCoupons>() {
            @Override
            public void onResponse(Call<ResponseDoctorCoupons> call, Response<ResponseDoctorCoupons> response) {
                Log.e("responseOfferListDoctor", new Gson().toJson(response.body()));

                if (pd.isShowing()) {
                    pd.dismiss();
                }

                if (response.body() != null && response.isSuccessful()) {
                    offerList = response.body().getOfferList();
                    Log.e("userId", sessionManager.getUserId());

                    if (response.body().getError()) {

                        Toast.makeText(DoctorCouponsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {

                        adapter = new CouponListDoctorAdapter(offerList, DoctorCouponsActivity.this);

                        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                        binding.rvCouponsListItem.setLayoutManager(layoutManager);
                        binding.rvCouponsListItem.setAdapter(adapter);
                    }

                }else {
                    Toast.makeText(DoctorCouponsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseDoctorCoupons> call, Throwable t) {

                if (pd.isShowing()) {
                    pd.dismiss();
                }
                Log.e("erro", t.getMessage());
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(DoctorCouponsActivity.this, DoctorDeshboardActivity.class);
        finish();
        startActivity(i);
    }
}