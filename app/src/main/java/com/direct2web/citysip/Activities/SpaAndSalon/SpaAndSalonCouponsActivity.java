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

import com.direct2web.citysip.Adapter.SpaAndSalonAdapter.CouponListSpaAndSalonAdapter;
import com.direct2web.citysip.Model.SpaAndSalon.CouponsOffer.Offer;
import com.direct2web.citysip.Model.SpaAndSalon.CouponsOffer.ResponseSpaAndSalonCoupons;
import com.direct2web.citysip.R;
import com.direct2web.citysip.Utils.Api;
import com.direct2web.citysip.Utils.BottomButtonClickListner;
import com.direct2web.citysip.Utils.RetrofitClient;
import com.direct2web.citysip.Utils.SessionManager;
import com.direct2web.citysip.Utils.WS_URL_PARAMS;
import com.direct2web.citysip.databinding.ActivityDoctorCouponsBinding;
import com.direct2web.citysip.databinding.ActivitySpaSalonCouponsBinding;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpaAndSalonCouponsActivity extends AppCompatActivity implements CouponListSpaAndSalonAdapter.OnItemClickListner {

    ActivitySpaSalonCouponsBinding binding;
    SessionManager sessionManager;
    ProgressDialog pd;
    BottomButtonClickListner bottomButtonClickListner;
    CouponListSpaAndSalonAdapter adapter;
    List<Offer> offerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_spa_salon_coupons);
        sessionManager = new SessionManager(this);

        bottomButtonClickListner = new BottomButtonClickListner(this, sessionManager);

        binding.bottomnavigation.bbImgOrder.setColorFilter(getResources().getColor(R.color.clr_spa_salon));

        binding.bottomnavigation.bbHome.setOnClickListener(new BottomButtonClickListner(this, sessionManager));
        binding.bottomnavigation.bbMyBusiness.setOnClickListener(new BottomButtonClickListner(this, sessionManager));
        binding.bottomnavigation.bbOrder.setOnClickListener(new BottomButtonClickListner(this, sessionManager));
        binding.bottomnavigation.bbMenu.setOnClickListener(new BottomButtonClickListner(this, sessionManager));



        pd = new ProgressDialog(SpaAndSalonCouponsActivity.this);
        pd.setMessage("Please Wait....");
        pd.setCancelable(true);
        pd.show();
        getOfferList(sessionManager.getUserId());

        binding.addCoupons.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SpaAndSalonCouponsActivity.this, SpaAndSalonAddCouponsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getOfferList(String id) {

        Api api = RetrofitClient.getClient().create(Api.class);

        Call<ResponseSpaAndSalonCoupons> call = api.getSpaAndSalonCouponsList("Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
                WS_URL_PARAMS.access_key, id);
        call.enqueue(new Callback<ResponseSpaAndSalonCoupons>() {
            @Override
            public void onResponse(Call<ResponseSpaAndSalonCoupons> call, Response<ResponseSpaAndSalonCoupons> response) {
                Log.e("responseOfferListDoctor", new Gson().toJson(response.body()));

                if (pd.isShowing()) {
                    pd.dismiss();
                }

                if (response.body() != null && response.isSuccessful()) {
                    offerList = response.body().getOfferList();
                    Log.e("userId", sessionManager.getUserId());

                    if (response.body().getError()) {

                        Toast.makeText(SpaAndSalonCouponsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {

                        adapter = new CouponListSpaAndSalonAdapter(offerList, SpaAndSalonCouponsActivity.this,SpaAndSalonCouponsActivity.this);

                        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                        binding.rvCouponsListItem.setLayoutManager(layoutManager);
                        binding.rvCouponsListItem.setAdapter(adapter);
                    }

                }else {
                    Toast.makeText(SpaAndSalonCouponsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseSpaAndSalonCoupons> call, Throwable t) {

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
        Intent i = new Intent(SpaAndSalonCouponsActivity.this, SapAndSalonDeshboardActivity.class);
        finish();
        startActivity(i);
    }

    @Override
    public void onAddButtonClick(int postion) {
        Intent i = new Intent(SpaAndSalonCouponsActivity.this, SpaAndSalonAddCouponsActivity.class);
        i.putExtra("flag","1");
        i.putExtra("couponCode", offerList.get(postion).getCoupnCode());
        i.putExtra("maxAmount", offerList.get(postion).getMaxAmount());
        i.putExtra("percentage", offerList.get(postion).getPercentage());
        i.putExtra("minAmount", offerList.get(postion).getMinAmount());
        i.putExtra("terms", offerList.get(postion).getTermsCondition());
        i.putExtra("couponId", offerList.get(postion).getId());
        finish();
        startActivity(i);
    }
}