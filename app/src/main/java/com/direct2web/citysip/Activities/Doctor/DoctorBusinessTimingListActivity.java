package com.direct2web.citysip.Activities.Doctor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.direct2web.citysip.Activities.Restaurent.SelectTimingActivity;
import com.direct2web.citysip.Adapter.DoctorAdapters.DoctorGetTimeListAdapter;
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
import com.direct2web.citysip.databinding.ActivityDoctorBusinessHoursListBinding;
import com.direct2web.citysip.databinding.ActivityDoctorBusinessSelectTimeBinding;
import com.direct2web.citysip.databinding.ActivitySetUpTimingListBinding;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorBusinessTimingListActivity extends AppCompatActivity implements DoctorGetTimeListAdapter.onClickSwitch, DoctorGetTimeListAdapter.onClickSwitchOff {

    ActivityDoctorBusinessHoursListBinding binding;
    SessionManager sessionManager;
    BottomButtonClickListner bottomButtonClickListner;
    ProgressDialog pd;
    List<Timing> timingList = new ArrayList<>();
    DoctorGetTimeListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_doctor_business_hours_list);

        sessionManager = new SessionManager(this);

        bottomButtonClickListner = new BottomButtonClickListner(this, sessionManager);

        binding.bottomnavigation.bbHome.setOnClickListener(new BottomButtonClickListner(this, sessionManager));
        binding.bottomnavigation.bbMyBusiness.setOnClickListener(new BottomButtonClickListner(this, sessionManager));
        binding.bottomnavigation.bbOrder.setOnClickListener(new BottomButtonClickListner(this, sessionManager));
        binding.bottomnavigation.bbMenu.setOnClickListener(new BottomButtonClickListner(this, sessionManager));

        getData(sessionManager.getUserId());

        binding.imgAdd.setOnClickListener(v -> {

            Intent intent = new Intent(DoctorBusinessTimingListActivity.this, DoctorBusinessSelectTimeActivity.class);
            startActivity(intent);

        });


    }

    private void getData(String userId) {

        pd = new ProgressDialog(DoctorBusinessTimingListActivity.this);
        pd.setMessage("Please Wait....");
        pd.setCancelable(false);
        pd.show();

        Api api = RetrofitClient.getClient().create(Api.class);
        Call<ResponseGetTimingList> call = api.getDoctorTimingList("Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
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

                        Toast.makeText(DoctorBusinessTimingListActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {

                        timingList = response.body().getTimingList();

                        adapter = new DoctorGetTimeListAdapter(timingList, DoctorBusinessTimingListActivity.this, DoctorBusinessTimingListActivity.this, DoctorBusinessTimingListActivity.this);
                        LinearLayoutManager manager = new LinearLayoutManager(DoctorBusinessTimingListActivity.this);
                        binding.rclrTimingList.setLayoutManager(manager);
                        binding.rclrTimingList.setAdapter(adapter);


                    }


                } else {

                    Toast.makeText(DoctorBusinessTimingListActivity.this, getResources().getString(R.string.error_admin), Toast.LENGTH_SHORT).show();

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
        Call<ResponseStatus> call = api.sendDoctorBusinessHoursStatus("Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
                WS_URL_PARAMS.access_key, userId, type, id,status);
        call.enqueue(new Callback<ResponseStatus>() {
            @Override
            public void onResponse(Call<ResponseStatus> call, Response<ResponseStatus> response) {

                Log.e("responsestatus", new Gson().toJson(response.body()));

                if (response.body() != null && response.isSuccessful()) {

                    if (response.body().getError()) {

                        Toast.makeText(DoctorBusinessTimingListActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(DoctorBusinessTimingListActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();


                    }

                } else {

                    Toast.makeText(DoctorBusinessTimingListActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

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