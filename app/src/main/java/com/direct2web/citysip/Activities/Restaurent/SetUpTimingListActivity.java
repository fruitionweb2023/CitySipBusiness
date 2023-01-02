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

import com.direct2web.citysip.Adapter.RestaurentAdapters.GetTimingListAdapter;
import com.direct2web.citysip.Model.RestaurentModels.Delete.ResponseStatus;
import com.direct2web.citysip.Model.RestaurentModels.Timming.ResponseGetTimingList;
import com.direct2web.citysip.Model.RestaurentModels.Timming.Timing;
import com.direct2web.citysip.R;
import com.direct2web.citysip.Utils.Api;
import com.direct2web.citysip.Utils.BottomButtonClickListner;
import com.direct2web.citysip.Utils.RetrofitClient;
import com.direct2web.citysip.Utils.SessionManager;
import com.direct2web.citysip.Utils.WS_URL_PARAMS;
import com.direct2web.citysip.databinding.ActivitySetUpTimingListBinding;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetUpTimingListActivity extends AppCompatActivity implements GetTimingListAdapter.onClickSwitch, GetTimingListAdapter.onClickSwitchOff {

    ActivitySetUpTimingListBinding binding;
    SessionManager sessionManager;
    BottomButtonClickListner bottomButtonClickListner;
    ProgressDialog pd;
    List<Timing> timingList = new ArrayList<>();
    GetTimingListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_set_up_timing_list);

        sessionManager = new SessionManager(this);

        bottomButtonClickListner = new BottomButtonClickListner(this, sessionManager);

        binding.bottomnavigation.bbHome.setOnClickListener(new BottomButtonClickListner(this, sessionManager));
        binding.bottomnavigation.bbMyBusiness.setOnClickListener(new BottomButtonClickListner(this, sessionManager));
        binding.bottomnavigation.bbOrder.setOnClickListener(new BottomButtonClickListner(this, sessionManager));
        binding.bottomnavigation.bbMenu.setOnClickListener(new BottomButtonClickListner(this, sessionManager));

        getData(sessionManager.getUserId());

        binding.imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SetUpTimingListActivity.this, SelectTimingActivity.class);
                startActivity(intent);

            }
        });


    }

    private void getData(String userId) {

        pd = new ProgressDialog(SetUpTimingListActivity.this);
        pd.setMessage("Please Wait....");
        pd.setCancelable(false);
        pd.show();

        Api api = RetrofitClient.getClient().create(Api.class);
        Call<ResponseGetTimingList> call = api.getTimingList("Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
                WS_URL_PARAMS.access_key, userId);
        call.enqueue(new Callback<ResponseGetTimingList>() {
            @Override
            public void onResponse(Call<ResponseGetTimingList> call, Response<ResponseGetTimingList> response) {

                Log.e("responseGetTiming", new Gson().toJson(response.body()));

                if (pd.isShowing()) {
                    pd.dismiss();
                }
                if (response.body() != null && response.isSuccessful()) {

                    if (response.body().getError()) {

                        Toast.makeText(SetUpTimingListActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {

                        timingList = response.body().getTimingList();

                        adapter = new GetTimingListAdapter(timingList, SetUpTimingListActivity.this,SetUpTimingListActivity.this,SetUpTimingListActivity.this);
                        LinearLayoutManager manager = new LinearLayoutManager(SetUpTimingListActivity.this);
                        binding.rclrTimingList.setLayoutManager(manager);
                        binding.rclrTimingList.setAdapter(adapter);


                    }


                } else {

                    Toast.makeText(SetUpTimingListActivity.this, getResources().getString(R.string.error_admin), Toast.LENGTH_SHORT).show();

                }


            }

            @Override
            public void onFailure(Call<ResponseGetTimingList> call, Throwable t) {

                if (pd.isShowing()) {
                    pd.dismiss();
                }
                t.printStackTrace();
                Log.e("errorTiming", t.getMessage());
            }
        });


    }


    @Override
    public void onSwitchItemClick(int position) {

        Timing timing = timingList.get(position);


        sendStatus(sessionManager.getUserId(),"business_time",timing.getDayId(),"1");
    }

    @Override
    public void onSwitchItemClickOff(int position) {

        Timing timing = timingList.get(position);

        sendStatus(sessionManager.getUserId(),"business_time",timing.getDayId(),"0");
    }

    public void sendStatus(String userId,String type,String id,String status){

        Api api = RetrofitClient.getClient().create(Api.class);
        Call<ResponseStatus> call = api.sendStatus("Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
                WS_URL_PARAMS.access_key, userId, type, id,status);
        call.enqueue(new Callback<ResponseStatus>() {
            @Override
            public void onResponse(Call<ResponseStatus> call, Response<ResponseStatus> response) {

                Log.e("responseDelete", new Gson().toJson(response.body()));

                if (response.body() != null && response.isSuccessful()) {

                    if (response.body().getError()) {

                        Toast.makeText(SetUpTimingListActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(SetUpTimingListActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();


                    }

                } else {
                    Toast.makeText(SetUpTimingListActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();


                }

            }

            @Override
            public void onFailure(Call<ResponseStatus> call, Throwable t) {

                t.printStackTrace();
                Log.e("errorDelete", t.getMessage());
            }
        });

    }
}