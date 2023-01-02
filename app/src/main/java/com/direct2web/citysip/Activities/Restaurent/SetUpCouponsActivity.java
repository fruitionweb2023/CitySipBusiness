package com.direct2web.citysip.Activities.Restaurent;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.direct2web.citysip.Adapter.RestaurentAdapters.NewCouponsListItemAdapter;
import com.direct2web.citysip.Model.RestaurentModels.Delete.ResponseStatus;
import com.direct2web.citysip.Model.RestaurentModels.offer.Offer;
import com.direct2web.citysip.Model.RestaurentModels.offer.ResponseOfferList;
import com.direct2web.citysip.R;
import com.direct2web.citysip.Utils.Api;
import com.direct2web.citysip.Utils.BottomButtonClickListner;
import com.direct2web.citysip.Utils.RetrofitClient;
import com.direct2web.citysip.Utils.SessionManager;
import com.direct2web.citysip.Utils.WS_URL_PARAMS;
import com.direct2web.citysip.databinding.ActivitySetUpCouponsBinding;
import com.google.gson.Gson;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetUpCouponsActivity extends AppCompatActivity implements NewCouponsListItemAdapter.OnItemClickListner, NewCouponsListItemAdapter.onClickSwitch, NewCouponsListItemAdapter.onClickSwitchOff {

    ActivitySetUpCouponsBinding binding;
    SessionManager sessionManager;
    ProgressDialog pd;
    List<Offer> offerList;
    NewCouponsListItemAdapter adapter;
    BottomButtonClickListner bottomButtonClickListner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_set_up_coupons);
        sessionManager = new SessionManager(this);

        bottomButtonClickListner = new BottomButtonClickListner(this, sessionManager);

        binding.bottomnavigation.bbImgOrder.setColorFilter(getResources().getColor(R.color.clr_f54748));

        binding.bottomnavigation.bbHome.setOnClickListener(new BottomButtonClickListner(this, sessionManager));
        binding.bottomnavigation.bbMyBusiness.setOnClickListener(new BottomButtonClickListner(this, sessionManager));
        binding.bottomnavigation.bbOrder.setOnClickListener(new BottomButtonClickListner(this, sessionManager));
        binding.bottomnavigation.bbMenu.setOnClickListener(new BottomButtonClickListner(this, sessionManager));



        pd = new ProgressDialog(SetUpCouponsActivity.this);
        pd.setMessage("Please Wait....");
        pd.setCancelable(true);
        pd.show();

        getOfferList(sessionManager.getUserId());


        binding.addCoupons.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SetUpCouponsActivity.this, SetUpAddCouponsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getOfferList(String id) {

        Api api = RetrofitClient.getClient().create(Api.class);

        Call<ResponseOfferList> call = api.getOffers("Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
                WS_URL_PARAMS.access_key, id);
        call.enqueue(new Callback<ResponseOfferList>() {
            @Override
            public void onResponse(Call<ResponseOfferList> call, Response<ResponseOfferList> response) {
                Log.e("responseOfferList", new Gson().toJson(response.body()));

                if (pd.isShowing()) {
                    pd.dismiss();
                }

                if (response.body() != null && response.isSuccessful()) {
                    offerList = response.body().getOfferList();
                    Log.e("userId", sessionManager.getUserId());

                    if (response.body().getError()) {

                        Toast.makeText(SetUpCouponsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {

                        adapter = new NewCouponsListItemAdapter(offerList, SetUpCouponsActivity.this,SetUpCouponsActivity.this,SetUpCouponsActivity.this,SetUpCouponsActivity.this);

                        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

                        binding.rvCouponsListItem.setLayoutManager(layoutManager);
                        binding.rvCouponsListItem.setAdapter(adapter);
                    }

                }else {
                        Toast.makeText(SetUpCouponsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

            @Override
            public void onFailure(Call<ResponseOfferList> call, Throwable t) {

                if (pd.isShowing()) {
                    pd.dismiss();
                }
                Log.e("erro", t.getMessage());
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onEditButtonClicked(int postion) {

        Intent intent = new Intent(SetUpCouponsActivity.this, SetUpEditCoupons.class);
        startActivity(intent);
    }

    @Override
    public void onSwitchItemClick(int position) {

        Offer timing = offerList.get(position);

//        Toast.makeText(SetUpTimingListActivity.this, timing.getDayId()+", "+timing.getDay(), Toast.LENGTH_SHORT).show();

        sendStatus(sessionManager.getUserId(),"offer",timing.getId(),"1");
    }

    @Override
    public void onSwitchItemClickOff(int position) {

        Offer timing = offerList.get(position);

//        Toast.makeText(SetUpTimingListActivity.this, timing.getDayId()+", "+timing.getDay()+" Off", Toast.LENGTH_SHORT).show();

        sendStatus(sessionManager.getUserId(),"offer",timing.getId(),"0");
    }

    public void sendStatus(String userId,String type,String id,String status){

       /* pd = new ProgressDialog(SetUpCouponsActivity.this);
        pd.setMessage("Please Wait....");
        pd.setCancelable(false);
        pd.show();
*/
        Api api = RetrofitClient.getClient().create(Api.class);
        Call<ResponseStatus> call = api.sendStatus("Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
                WS_URL_PARAMS.access_key, userId, type, id,status);
        call.enqueue(new Callback<ResponseStatus>() {
            @Override
            public void onResponse(Call<ResponseStatus> call, Response<ResponseStatus> response) {

                Log.e("responseDelete", new Gson().toJson(response.body()));

               /* if (pd.isShowing()) {
                    pd.dismiss();
                }*/
                if (response.body() != null && response.isSuccessful()) {

                    if (response.body().getError()) {

                        Toast.makeText(SetUpCouponsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } /*else {

                        Toast.makeText(SetUpCouponsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();


                    }*/

                } else {
                    Toast.makeText(SetUpCouponsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();


                }

            }

            @Override
            public void onFailure(Call<ResponseStatus> call, Throwable t) {

               /* if (pd.isShowing()) {
                    pd.dismiss();
                }*/
                t.printStackTrace();
                Log.e("errorDelete", t.getMessage());
            }
        });
    }
}